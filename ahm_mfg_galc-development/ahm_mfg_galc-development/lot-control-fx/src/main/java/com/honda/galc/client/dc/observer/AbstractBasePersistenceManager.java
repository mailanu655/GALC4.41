package com.honda.galc.client.dc.observer;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.ManufacturingControlPropertyBean;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;


public abstract class AbstractBasePersistenceManager extends AbstractPersistenceManager {

	public AbstractBasePersistenceManager(DataCollectionController dcController) {
		super(dcController);
	}

	public void saveCollectedData(MCOperationRevision operation) {
		if (getModel().getProductModel().isTrainingMode()) return;
		List<ProductBuildResult> preparedList = prepareForSave(operation);
		if(preparedList!=null && !preparedList.isEmpty()){
			saveProductBuildResults(preparedList);
		}
	}
	/**
	 * Prepare the product build result of a MCStructure for save.
	 *
	 * @param structure the structure
	 * @return the list
	 */
	protected List<ProductBuildResult> prepareForSave(MCOperationRevision operation) {
		List<ProductBuildResult> resultList = new ArrayList<ProductBuildResult>();
		List<ProductBuildResult> list = getModel().getBuildResultList(operation.getId().getOperationName());
		if (list == null) {
			// no product build result for the structure. 
			return resultList;
		}
		for (ProductBuildResult pbr : list) {
			// update the installed part status and other information
			pbr.setAssociateNo(getController().getProductModel().getApplicationContext().getUserId());
			if (pbr instanceof InstalledPart) {
				prepareInstalledPart(operation, (InstalledPart) pbr);
			}
			resultList.add(pbr);
		}
		return resultList;
	}
	
	/**
	 * Prepare installed part. The function will update the status of the
	 * installed part to OK if part serial no is OK and overall status of
	 * measurement is OK. If not, the installed part status is NG
	 * 
	 * @param structure
	 *            the structure
	 * @param installedPart
	 *            the installed part
	 */
	protected void prepareInstalledPart(MCOperationRevision operation, InstalledPart installedPart) {
		boolean torqueStatus = prepareTorquesForPart(operation, installedPart);
		if (torqueStatus && installedPart.isValidPartSerialNumber() && !installedPart.isSkipped()) {
			installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		} else {
			installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
		}
	}
	
	/**
	 * Prepare torques for the installed part. The function calculate the overall measurement status
	 *
	 * @param operation the operation
	 * @param installedPart the installed part
	 * @return true, if all measurements are OK and no missing torques
	 */
	protected boolean prepareTorquesForPart(MCOperationRevision operation, InstalledPart installedPart) {
		boolean torqueStatus = true;
		int count = installedPart.getMeasurements() == null ? 0 : installedPart
				.getMeasurements().size();

		MCOperationPartId partId = new MCOperationPartId();
		partId.setOperationName(installedPart.getPartName());
		partId.setPartId(installedPart.getPartId());
		MCOperationPartDao partDao = ServiceFactory
				.getDao(MCOperationPartDao.class);
		MCOperationPart part = partDao.findByKey(partId);

		//If no part or part rev is available, the validation of measurement count is ignored
		if (part != null) {
			MCOperationPartRevisionId partRevId = new MCOperationPartRevisionId();
			partRevId.setOperationName(installedPart.getPartName());
			partRevId.setPartId(installedPart.getPartId());
			partRevId.setPartRevision(part.getPartRevision());
			MCOperationPartRevisionDao partRevDao = ServiceFactory
					.getDao(MCOperationPartRevisionDao.class);
			MCOperationPartRevision partRev = partRevDao.findByKey(partRevId);
			if (partRev != null && partRev.getMeasurementCount() > count) {
				// Not enough measurement.
				torqueStatus = false;
			}
		}
		
		//check if all measurement status is OK or not. Only when all measurement is OK, the overall status is OK
		if (torqueStatus && installedPart.getMeasurements() != null) {
			for (Measurement m : installedPart.getMeasurements()) {
				if (m.getMeasurementStatus() != MeasurementStatus.OK) {
					torqueStatus = false;
					break;
				}
			}
		}
		return torqueStatus;

	}


	


	/**
	 * Save product build results to GALC DB.
	 *
	 * @param result the result
	 */
	protected void saveProductBuildResults(List<ProductBuildResult> result) {
		if (result != null && !result.isEmpty()) {
			ProductBuildResultDao<? extends ProductBuildResult, ?> dao = ProductTypeUtil
					.getProductBuildResultDao(getController().getProductModel().getProductType());
			if(dao instanceof InstalledPartDao){
				//InstalledPart and measurements don't have entity relationship, 
				//so ProductBuildResultDao.saveAllResults() cannot save measurements
				//We have to call the save function of installedPartDao  in order to save measurements
				InstalledPartDao installedPartDao = (InstalledPartDao) dao;
				List<InstalledPart> partList = new ArrayList<InstalledPart>(result.size());
				for(ProductBuildResult pbr : result){
					if(pbr instanceof InstalledPart){
						partList.add((InstalledPart) pbr);
					}
				}
				installedPartDao.saveAll(partList);
			}
			else{
				dao.saveAllResults(result);
			}
		}
	}
	
	
	public void deleteInstalledPart(InstalledPartId id) {
		ServiceFactory.getDao(InstalledPartDao.class).removeByKey(id);
	}
	
	
	public void deleteMeasurement(MeasurementId id) {
		ServiceFactory.getDao(MeasurementDao.class).removeByKey(id);
	}
	
	protected ManufacturingControlPropertyBean getApplicationProperty() {
		return PropertyService.getPropertyBean(ManufacturingControlPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
	}
}
