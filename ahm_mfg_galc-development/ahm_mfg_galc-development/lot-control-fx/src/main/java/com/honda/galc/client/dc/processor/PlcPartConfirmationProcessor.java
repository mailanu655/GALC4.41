package com.honda.galc.client.dc.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class PlcPartConfirmationProcessor extends OperationProcessor {

	protected Map<String, ProductBuildResult> productBuildResults;
		
	
	public PlcPartConfirmationProcessor(DataCollectionController controller,
			MCOperationRevision operation) {
		super(controller, operation);
		initialize();
	}
	

		protected void initialize() {
		}

		public String getProductId() {
			return getController().getProductModel().getProductId();
		}

		public Map<String,ProductBuildResult> getProductBuildResults() {
			return productBuildResults;
		}

		public void setProductBuildResults(Map<String, ProductBuildResult> productBuildResults) {
			this.productBuildResults = productBuildResults;
		}


		/**
		 * Process the scanned product id. The function will try to read the product build result for the scanned product id.
		 */
		@SuppressWarnings("unchecked")
		public void processProductId() {
			ProductBuildResultDao<? extends ProductBuildResult, ?> productBuildResultDao = ProductTypeUtil
					.getProductBuildResultDao(getController().getProductModel().getProductType());
			this.productBuildResults = new HashMap<String,ProductBuildResult>();
			List<ProductBuildResult> installedParts =  null;
			if(getPlcMeasurements() != null && !getPlcMeasurements().isEmpty()){
				installedParts = (List<ProductBuildResult>) productBuildResultDao.findAllByProductIdAndPartNames(getProductId(), getPlcMeasurements());
			}
			if(installedParts != null && !installedParts.isEmpty()){
				for(ProductBuildResult installedPart : installedParts){
						this.productBuildResults.put(installedPart.getPartName(), installedPart);
						setMeasurements((InstalledPart)installedPart);
				}
			}
		}
		
		private void setMeasurements(InstalledPart installedPart){
			MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
			List<Measurement> measurements = measurementDao.findAll(getProductId(), installedPart.getPartName());
			installedPart.setMeasurements(measurements);
		}

		/**
		 * Gets the measurement overall status of the current product.
		 * <p>
		 * the overall status is OK only when <br/>
		 * <li>The current part is already installed(a record in GAL185TBX).
		 * <li>All of measurements(records in GAL195TBX) of the installed part is
		 * OK.
		 * <li>The number of the measurements(record count in GAL195TBX) equals to
		 * MC_OP_PART_REV_TBX.MEASUREMENT_COUNT.
		 * </p>
		 * <br/>
		 * <p>
		 * If any of the above criteria fails, the overall status will be NG(Not
		 * Good).
		 * </p>
		 * 
		 * @return the measurement overall status
		 */
		public String getMeasurementOverallStatus() {
			if (productBuildResults == null || productBuildResults.isEmpty()) {
				//Part not installed
				return "There are no parts installed for : "+getOperation().getId().getOperationName();
			}
			String message = "";
			//If the part confirmation is for nut runner parts, check all the parts are installed in GAL185TBX
			if(getPlcMeasurements() != null && !getPlcMeasurements().isEmpty()){
				for(String partName : getPlcMeasurements()){
					if(!productBuildResults.containsKey(partName)) message = setMissingOrWrongMeasurementMessage(partName, message);
				}
			}

			for(Entry<String,ProductBuildResult> productBuildResult : productBuildResults.entrySet()){
				if (productBuildResult.getValue() instanceof InstalledPart) {
					InstalledPart installedPart = (InstalledPart) productBuildResult.getValue();
					List<Measurement> measurements = installedPart.getMeasurements();
					//If the status in the GAL185TBX is anything other than OK, or no measurements means missing measurements
					if (installedPart.getInstalledPartStatus() == null || 
							installedPart.getInstalledPartStatus() != InstalledPartStatus.OK || 
							measurements == null) {
						//Missing Measurements
						message = setMissingOrWrongMeasurementMessage(productBuildResult.getKey(), message);
						
					}else if (measurements != null) {
						for (Measurement m : measurements) {
							if (m.getMeasurementStatus() != MeasurementStatus.OK) {
								//Wrong measurement status, so overall status is NG
								message = setMissingOrWrongMeasurementMessage(installedPart.getPartName(), message);
								break;
							}
						}
					}
				}
			}
			
			return message;
		}
		
		private String setMissingOrWrongMeasurementMessage(String partName, String message){
			if(StringUtils.isEmpty(message)) return "Missing/wrong measuremts : "+partName;
			else return message+","+partName;
		}

		
		/**
		 * Finish work unit and notify the data collection framework
		 */
		public void finish() {
			completeOperation(true);
		}
		
		public List<String> getPlcMeasurements() {
			return PropertyService.getPropertyList(getController().getProductModel().getProcessPointId(), getController().getModel().getCurrentOperationName());
		}
		
		public boolean hasInstalledPart(){
			ProductBuildResultDao<? extends ProductBuildResult, ?> productBuildResultDao = ProductTypeUtil
					.getProductBuildResultDao(getController().getProductModel().getProductType());
			ProductBuildResult installedPart = productBuildResultDao.findById(getProductId(), getController().getModel().getCurrentOperationName());
			if(installedPart == null) return false;
			else return true;
		}
}
