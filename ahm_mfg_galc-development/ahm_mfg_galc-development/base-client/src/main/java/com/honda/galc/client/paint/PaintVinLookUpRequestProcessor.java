package com.honda.galc.client.paint;

import static com.honda.galc.service.ServiceFactory.getService;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PaintRfidWriteRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.PaintOnLookUpService;
import com.honda.galc.service.property.PropertyService;

public class PaintVinLookUpRequestProcessor extends PlcDataReadyEventProcessorBase implements IPlcDataReadyEventProcessor<PaintRfidWriteRequest> {

	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String PRODUCT_SPEC_CODE = "PRODUCT_SPEC_CODE";
	public static final String PROCESS_POINT_ID = "VIN_LOOKUP_PPID";
	public static final String PLANT_NAME = "VIN_LOOKUP_PLANT_NAME"; 
	
	public synchronized boolean execute(PaintRfidWriteRequest deviceData) {

		try {
			
			String sequenceNumber = deviceData.getSequenceNumber().trim();
			if(StringUtils.isEmpty(sequenceNumber)){
				getLogger().info("sequence number empty");
				return false;
			}
			
			String processPointId = PropertyService.getProperty(getApplicationId(), PROCESS_POINT_ID);
			if(StringUtils.isEmpty(processPointId)){
				getLogger().info("process point id is empty");
				return false;
			}
				
			String plantName = PropertyService.getProperty(getApplicationId(), PLANT_NAME);
			if(StringUtils.isEmpty(plantName)){
				getLogger().info("plant name is empty");
				return false;
			}
			getLogger().info("Received Sequence Number:"+sequenceNumber);
			DataContainer dc = new DefaultDataContainer();
			
			try{
				dc = getService(PaintOnLookUpService.class).vinLookUpBySeqNum(sequenceNumber.trim(), processPointId.trim(), plantName.trim());
			} catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
			
			checkDataContainer(dc);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing Paint Vin Rewrite process");
			return false;
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0"));
		}
	}

	private void checkDataContainer(DataContainer dc){
		String errorCode= "";
		if (dc.size() == 0) {
			errorCode = "1";
			getBean().put("errorCode", new StringBuilder(errorCode));
			getBean().put("galcDataReady", new StringBuilder("1"),DeviceTagType.PLC_GALC_DATA_READY);
			getLogger().info("Could not find any products for provided Model and Sequence Number combination");
		} else if (dc.size() > 1) {
			getLogger().info("Found multiple products for given Sequence Number ");
			Iterator<Object> iterator = dc.keySet().iterator();
			int i=0;
		    while(iterator.hasNext()) {
		        Object key = iterator.next();
		        if(key.toString().contains(PRODUCT_ID))
		        getBean().put(PRODUCT_ID+i, new StringBuilder(dc.get(key).toString()));
		        if(key.toString().contains(PRODUCT_SPEC_CODE))
				getBean().put(PRODUCT_SPEC_CODE+i, new StringBuilder(dc.get(key).toString()));
		       i++;
		    }
			
			getBean().put("galcDataReady", new StringBuilder("1"),DeviceTagType.PLC_GALC_DATA_READY);
			getLogger().info("Paint Vin re-write Process Successful");
			
		} 
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}
}

