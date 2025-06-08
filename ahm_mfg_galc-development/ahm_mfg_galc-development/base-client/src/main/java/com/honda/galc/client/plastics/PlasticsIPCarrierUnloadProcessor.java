/**
 * 
 */
package com.honda.galc.client.plastics;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PlasticsIPCarrierUnloadRequest;
import com.honda.galc.client.events.ProductInspectionComplete;
import com.honda.galc.client.events.ProductInspectionRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 20, 2012
 */
public class PlasticsIPCarrierUnloadProcessor extends PlcDataReadyEventProcessorBase 
    implements IPlcDataReadyEventProcessor <PlasticsIPCarrierUnloadRequest>, EventSubscriber<ProductInspectionComplete>  {

	private static final String CARRIER_UNLOAD_MAX_WAIT_TIME_KEY = "CARRIER_UNLOAD_MAX_WAIT_TIME";
	
	private String _carrierNum = "";
	private List<ProductPriorityPlan> _productList = null;
	private ConcurrentHashMap<String, Boolean> _productMap = new ConcurrentHashMap<String, Boolean>();
	
	public synchronized boolean execute(PlasticsIPCarrierUnloadRequest deviceData) {
		String errorCode = "0";
		try {
			EventBus.subscribe(ProductInspectionComplete.class, this);
			_carrierNum = deviceData.getCarrierNumber().toString();
			createProductMap();
			
			// publish events to indicate products in the carrier are ready to be inspected
			broadcastCarrierArrivalEvent();

			long expirationTime = getExpirationtime();
			try {
				while (System.currentTimeMillis() < expirationTime) {
					if (isAllProductsInspected()) {
						break;
					}
					Thread.sleep(100);
				}
			} catch (Exception e) {}

			if (!isAllProductsInspected()) {
				errorCode = "1";				// indicate all products were not inspected
				getLogger().warn("Releasing carrier though not all products for carrier " + getCarrierNum() + " have not been inspected");
			}
			
			getProductPriorityPlanDao().doUnload(getCarrierNum());
			getLogger().info("Carrier Unload Process Successful");
			return true;
		} catch(Exception ex) {
			errorCode = "2";					// indicates unexpected error
			ex.printStackTrace();
			getLogger().error("Error processing Carrier unload");
			return false;
		} finally {
			try {
				getBean().put("galcDataError", new StringBuilder(errorCode),DeviceDataType.INTEGER);
				getBean().put("carrierNumberAck", new StringBuilder(getCarrierNum()),DeviceDataType.INTEGER);
				getBean().put("galcDataReady", new StringBuilder("1"),DeviceDataType.INTEGER);
			} catch(Exception ex) {}
		}
	}

	private List<ProductPriorityPlan> getProductList() {
		if (_productList == null) {
			try {
				_productList = getProductPriorityPlanDao().getLastLoadedProducts(getCarrierNum());
			} catch(Exception ex) {}
		}
		return _productList;
	}

	private long getExpirationtime() {
		String carrierUnloadTimeDelay;
		try {
			carrierUnloadTimeDelay  = PropertyService.getProperty(getApplicationId(), CARRIER_UNLOAD_MAX_WAIT_TIME_KEY);
		} catch(Exception ex) {
			ex.printStackTrace();
			carrierUnloadTimeDelay = "0";
		}
		return System.currentTimeMillis() + Long.parseLong(carrierUnloadTimeDelay);
	}
	
	private void broadcastCarrierArrivalEvent() {
		try {
			for(ProductPriorityPlan product : getProductList()) {
				EventBus.publish(new ProductInspectionRequest(StringUtils.trimToEmpty(product.getProductId()), getCarrierNum()));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing Tracking for loaded carriers");
		}
	}

	public void onEvent(ProductInspectionComplete pInspectionComplete) {
		// set status to completed only for products that are already in the map 
		if (getProductMap().containsKey(pInspectionComplete.getProductId())){
			getProductMap().put(pInspectionComplete.getProductId(), true);
		}
	}
	
	private void createProductMap() {
		for(ProductPriorityPlan ppp: getProductList()) {
			if (!getProductMap().containsKey(ppp.getProductId())) {
				getProductMap().put(ppp.getProductId(), false);
			}
		}
	}
	
	private boolean isAllProductsInspected() {
		for (Entry<String, Boolean> entry : getProductMap().entrySet()) {   
			if (!entry.getValue()) {
				return false;
			}
		}
		return true;
	}

	private ConcurrentHashMap<String, Boolean> getProductMap() {
		return _productMap;
	}
	
	private String getCarrierNum() {
		return _carrierNum;
	}
	
	public void postPlcWrite(boolean writeSucceeded) {}

	public void validate() {}
}
