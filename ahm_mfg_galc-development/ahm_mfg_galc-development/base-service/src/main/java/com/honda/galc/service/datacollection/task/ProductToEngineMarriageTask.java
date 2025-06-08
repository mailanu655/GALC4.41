package com.honda.galc.service.datacollection.task;

import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.ProductionLotStatus;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

public class ProductToEngineMarriageTask extends CollectorTask {

	DiecastDao installedProductDao;
	DieCast installedProduct;
	String installedProductType;
	ProductType ipType;
	
	
	protected InProcessProductDao inProcessProductDao = null;
	protected ProcessPointDao processPointDao = null;
	
	public ProductToEngineMarriageTask(
			HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
		this.context = context;
	}

	@Override
	public void execute() {
		ipType = ProductType.getType(PropertyService.getProperty(processPointId, TagNames.INSTALLED_PRODUCT.name(), ""));
		
		marryProductToEngine();
	}

	@SuppressWarnings("unchecked")
	private void marryProductToEngine() {
		String ein = context.get(TagNames.PRODUCT_ID.name()).toString();
		if (ipType == null) {
			throw new TaskException("Invalid Installed Product Type of NULL." );
		}
		
		if (ipType.equals(ProductType.BLOCK)) {
			installedProductDao = ServiceFactory.getDao(BlockDao.class); 
		} else if (ipType.equals(ProductType.HEAD)) {
			installedProductDao = ServiceFactory.getDao(HeadDao.class); 
		} else if (ipType.equals(ProductType.CRANKSHAFT)) {
			installedProductDao = ServiceFactory.getDao(CrankshaftDao.class);
		} else if (ipType.equals(ProductType.CONROD)) {
			installedProductDao = ServiceFactory.getDao(ConrodDao.class);
		} else {
			throw new TaskException("Invalid Installed Product Type: " + ipType.getProductName());
		}

		for(ProductBuildResult buildResult : context.getBuildResults()) {
			String partSN = buildResult.getPartSerialNumber();
			try{
				installedProduct = (DieCast) installedProductDao.findByMCDCNumber(partSN);
			} catch (Exception e) {  //In the case of exception, continue.
			}
			
			if (installedProduct != null) {
				if (!performInstalledProductChecks(buildResult)) {
					buildResult.setInstalledPartStatus(InstalledPartStatus.NG);
					buildResult.setErrorCode(getProductCheckPropertyBean().getInstalledProductErrorCode());
					buildResult.setDefectLocation("");
					getLogger().info(buildResult.getPartName() + " status changed to NG, because failed Installed Product Check.");
				}

				String ipPP = PropertyService.getProperty(processPointId, "INSTALLED_PRODUCT_PROCESS_POINT", "");
				if (!ipPP.equals("")) {
					getTrackingService().track(ipType, installedProduct.getProductId(), ipPP);
				}

				installedProduct.setDunnage("");
				installedProduct.setLastPassingProcessPointId(processPointId);
				installedProductDao.save(installedProduct);

				getLogger().info("Successfully updated product information of " + installedProduct.getProductId());
				
			} else {
				getLogger().info(partSN + " not found.  There is no data to update.");
			}
		}
	}

	private boolean performInstalledProductChecks(ProductBuildResult buildResult) {
		boolean overallResult = true;
		
		//Retrieve Process Point of where product is last check for checks like RequiredParts to use last check PP, not current PP.
		String installedProductCheckPoint = getProductCheckPropertyBean().getInstalledProductLastCheckPoint();
		if (null == installedProductCheckPoint || installedProductCheckPoint == "") {
			installedProductCheckPoint = context.getProcessPointId();
		}
			//List of Checks
			String[] installedProductCheckTypes = getProductCheckPropertyBean().getInstalledProductCheckTypes();
			Map<String, Object> checkResults = ProductCheckUtil.check(installedProduct,	getProcessPointDao().findByKey(installedProductCheckPoint), installedProductCheckTypes);
	
			for(String checkType: installedProductCheckTypes){
				Boolean typeCheckResult = !checkResults.keySet().contains(checkType);
				overallResult &= typeCheckResult;
				
				context.put(buildResult.getPartName() + "_"+ checkType, typeCheckResult);
				if(!typeCheckResult) getLogger().warn(checkType, checkResults.get(checkType).toString());
			}
			
			context.put(TagNames.INSTALLED_PRODUCT_TYPE_CHECK_RESULT.name(), overallResult);
		
		return overallResult;
	}

	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) {
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	public ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}

	protected ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class,context.getProcessPointId());
	}
	
}
