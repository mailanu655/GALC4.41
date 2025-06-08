/**
 * 
 */
package com.honda.galc.client.plastics;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PlasticsIPRfidWriteRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @date Nov 5, 2012
 */
public class PlasticsIPRfidWriteProcessor extends PlcDataReadyEventProcessorBase 
	implements IPlcDataReadyEventProcessor <PlasticsIPRfidWriteRequest> {

	public static String MODEL_CODE = "PLASTIC_MODEL_CODE";
	public static String EXT_COLOR_CODE = "PLASTIC_COLOR_CODE";

	public synchronized boolean execute(PlasticsIPRfidWriteRequest deviceData) {

		String carrierNum = "-1";
		String errorCode = "0";
		try {
			// 1. Carrier Data from PLC
			carrierNum = String.valueOf(deviceData.getCarrierNumber());
			// Two (Bumper) or Four (IP), whatever number capacity
			int carrierCapacity = deviceData.getCarrierCapacity();

			List<LoadedProductData> productList = null;
			LoadedProductData productData = null;
			LoadedProductData firstProductData = null;

			if (productList == null){
				// 2. Get Product list from the FIFO (PRODUCT_PRIORITY_PLAN_TBX) table based on carrier capacity
				productList = getProductList(carrierNum, carrierCapacity);

				// 3. No products on the queue table
				if (productList.size() < carrierCapacity) {
					errorCode = "1";							// indicates no products are available to be loaded
					getLogger().error("No products on the queue table");
					return false;
				}
			}

			if(productList.size()>0) {
				// 4. Send empty carrier signal if color change and bumper type change on first productList
				firstProductData = (LoadedProductData) productList.get(0);
			}

			// Validate remainder of list to see if there is another empty carrier.
			// This is to check for an odd load usually at the end the same exterior color lots.  
			// Odd plastic will be allowed to be loaded (ie, validProductList less than carrier capacity).
			List<LoadedProductData> validProductList = new ArrayList<LoadedProductData>();	
			for (int i = 0; i < productList.size(); i++) {
				productData = (LoadedProductData) productList.get(i);
				if (!productData.getProductId().trim().equalsIgnoreCase("EEEEEEEEEEEEEEEEE")) {
					validProductList.add(productData);
				} else {
					break;
				}
			}

			String bumperType = firstProductData.getPlasticsTypeCode();

			// 5. Verify that all the products to be loaded are of the same bumper type
			for (int i = 0; i < validProductList.size(); i++) {
				productData = (LoadedProductData) validProductList.get(i);
				String tagPlasticType = productData.getPlasticsTypeCode();
				if (!(tagPlasticType.equalsIgnoreCase(bumperType) || tagPlasticType.equalsIgnoreCase(bumperType))) {
					errorCode = "2";							// indicates products to be loaded have different product spec codes
					getLogger().error("All the products to be loaded are not of the same bumper type");
					return false;
				}
			}

			// 6. Get the model code and validate
			for (int i = 0; i < validProductList.size(); i++) {
				productData = (LoadedProductData) validProductList.get(i);
				if (productData.getModelCode() == null || productData.getModelCode().equals("") ) {
					errorCode = "3";							// indicates model code is invalid
					getLogger().error("Invalid Model Code");
					return false;
				}
			}

			// 7. Get the exterior color code and validate
			for (int i = 0; i < validProductList.size(); i++) {
				productData = (LoadedProductData) validProductList.get(i);
				if (productData.getExtColourCode() == null || productData.getExtColourCode().equals("") ) {
					errorCode = "4";							// indicates exterior color code is invalid
					getLogger().error("Invalid Exterior Color Code");
					return false;
				}
			}

			// 8. Verify that all the products have the same model code and exterior color 
			String tagModelCode = firstProductData.getModelCode();
			String tagExtColorCode = firstProductData.getExtColourCode();
			for (int i = 0; i < validProductList.size(); i++) {
				productData = (LoadedProductData) validProductList.get(i);
				if (!((tagModelCode.equalsIgnoreCase(productData.getModelCode())) && (tagExtColorCode.equalsIgnoreCase(productData.getExtColourCode())))) {
					errorCode = "5";							// indicates all products do not have same model/exterior color code
					getLogger().error("All the products do not have the same model code and exterior color");
					return false;
				}
			}

			// 9. Generate the product(s) for DB update and tagDataLoadPattern
			LoadedProductData loadProduct = null;
			for (int i = 0; i < validProductList.size(); i++) {
				loadProduct = (LoadedProductData) validProductList.get(i);
				loadProduct.setCarrierPosition(String.valueOf(i + 1));
				// DB update carrier number to the product(s)
				doLoad(loadProduct);
			}

			// 10. Write data to PLC
			getBean().put("modelCode", new StringBuilder(tagModelCode),DeviceDataType.INTEGER);
			getBean().put("exteriorColorCode", new StringBuilder(tagExtColorCode),DeviceDataType.INTEGER);
			
			getLogger().info("Rfid Write Process Successful");
			return true;
		} catch(Exception ex) {
			errorCode = "6";									// indicates unexpected error
			ex.printStackTrace();
			getLogger().error("Error processing Carrier load");
			return false;
		} finally {
			try {
				getBean().put("galcDataError", new StringBuilder(errorCode),DeviceDataType.INTEGER);
				getBean().put("carrierNumberAck", new StringBuilder(carrierNum),DeviceDataType.INTEGER);
				getBean().put("galcDataReady", new StringBuilder("1"),DeviceDataType.INTEGER);
			} catch(Exception ex) {}
		}
	}

	private List<LoadedProductData> getProductList(String carrierId, int carrierCapacity) throws Exception {
		List<LoadedProductData> productList = new ArrayList<LoadedProductData>();
		try {
			ProductPriorityPlanDao productPriorityPlanDao = ServiceFactory.getDao(ProductPriorityPlanDao.class);
			List<Object[]> list = productPriorityPlanDao.getAvailableProductsToLoad(carrierCapacity);

			if (list.size() > 0) {
				for (Object[] array : list) {
					LoadedProductData data = new LoadedProductData();
					data.setProductId(array[0] == null ? null: (String) array[0]);
					data.setPlasticsTypeCode(array[1] == null ? null: (String) array[1]);					
					data.setSequenceNo(array[2] == null ? null: (Double) array[2]);
					data.setCarrierId(carrierId);
					String mtoc=array[3] == null ? null: (String) array[3];
					data.setModelCode(getBuildAttributeValue(mtoc,MODEL_CODE));
					data.setExtColourCode(getBuildAttributeValue(mtoc,EXT_COLOR_CODE));					
					productList.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Error retrieving product ID from Bumper table");
		}

		return productList;
	}

	private boolean doLoad(LoadedProductData data) throws Exception {
		boolean isUpdated = false;
		try {
			Double sequenceNo = data.getSequenceNo();
			String containerId = data.getCarrierId();
			String containerPos = data.getCarrierPosition();

			if (data.getProductId().trim().equalsIgnoreCase("EEEEEEEEEEEEEEEEE")) {
				containerPos = "1";
			}

			ProductPriorityPlanDao productPriorityPlanDao = ServiceFactory.getDao(ProductPriorityPlanDao.class);
			int returnValue = productPriorityPlanDao.doLoad(containerId, containerPos, sequenceNo);

			if (returnValue == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Error updating the carrier position code to product ID");
		}
		return isUpdated;
	}

	public String getPlantNameByPPID(String processPointID) throws Exception {
		String plantName = null;
		try {
			ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
			ProcessPoint processPoint = processPointDao.findByKey(processPointID);
			plantName = processPoint.getPlantName();
			if (plantName == null) {
				getLogger().error("Can't find Plant Name or find more than 1");
				throw new Exception("Can't determine plant name by process point id.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Exception raised while retrieving actual paint line id");
		}

		return plantName;
	}

	private String getBuildAttributeValue( String mtoc,String attribute) {
		String attributeValue = null;
		BuildAttribute buildAttribute = null;
		BuildAttributeDao buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		buildAttribute = buildAttributeDao.findById(attribute, mtoc);
		if(buildAttribute != null) {
			attributeValue = buildAttribute.getAttributeValue();
		}
		return attributeValue;
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}
}
