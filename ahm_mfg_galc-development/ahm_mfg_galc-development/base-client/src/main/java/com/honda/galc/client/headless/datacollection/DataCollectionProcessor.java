package com.honda.galc.client.headless.datacollection;

import java.util.Map;


import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PlcDataCollectionRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class DataCollectionProcessor extends PlcDataReadyEventProcessorBase 
	implements IPlcDataReadyEventProcessor<PlcDataCollectionRequest>{
	
	public DataCollectionProcessor() {
	}
	
	public synchronized boolean execute(PlcDataCollectionRequest deviceData)
	{
		// unexpected null or empty vin
		if (deviceData.getProductId() == null || deviceData.getProductId().equals("")) {
			getLogger().info("Received null or empty vin");
			return false;
		}
		
		//unexpected null or empty device id
		if (deviceData.getPlcDeviceId() == null || deviceData.getPlcDeviceId().equals("")) {
			getLogger().info("Received null or empty device id");
			return false;
		}
		
		getLogger().info("Invoking Data Collection Service with product:", deviceData.getProductId());
		invokeDataCollectionService(deviceData.getProductId(), deviceData.getPlcDeviceId());
		return true;
		
	}
	
	private Map<Object, Object> invokeDataCollectionService(String productId, String clientId) {
		DataCollectionService service = ServiceFactory.getService(DataCollectionService.class);
		DataContainer request = new DefaultDataContainer();
		request.put(TagNames.PRODUCT_ID.name(), productId);
		request.put(TagNames.PROCESS_POINT_ID.name(), getApplicationId());
		request.put(TagNames.CLIENT_ID.name(), clientId);
		request.put(TagNames.PRODUCT_TYPE, getApplicationPropertyBean().getProductType());
		return service.execute(request);
	}
	
	protected SystemPropertyBean getApplicationPropertyBean() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class,getApplicationId());
	}
	
	public void postPlcWrite(boolean writeSucceeded) {

	}
	
	public void validate() {

	}
}
