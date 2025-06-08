package com.honda.galc.client.paint;

import org.bushe.swing.event.EventBus;
import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.enumtype.RfidErrorCodes;
import com.honda.galc.client.events.PaintRfidWriteRequest;
import com.honda.galc.client.events.RfidProductInspectionRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.PaintOnLookUpService;
import static com.honda.galc.service.ServiceFactory.getService;

public class PaintRfidValidationProcessor extends
		PlcDataReadyEventProcessorBase implements
		IPlcDataReadyEventProcessor<PaintRfidWriteRequest> {

	private PaintRfidWriteRequest deviceData;
	private DataContainer dc;
	
	public synchronized boolean execute(PaintRfidWriteRequest deviceData) {
		getLogger().info("Enter into execute method of PaintRfidValidationProcessor");
		this.deviceData = deviceData;
		getBean().setProductId(deviceData.getVin());
		getBean().setProductSpecCode(
				deviceData.getAttribute("mtoc").toString().trim());
		
		return true;
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
		try {
			dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, getBean().getProductId());
			if (deviceData.getAttributes().size() > 0) {
				dc.put("DeviceAttributes", deviceData.getAttributes());
				dc.put("DeviceSubstitutionList", getBean().getSubstitutionList());

				dc = getService(PaintOnLookUpService.class)
						.validateRfidAttributes(dc, getApplicationId());

				if (dc.containsKey("errorCode")) {
					EventBus.publish(new RfidProductInspectionRequest(dc.get(
							DataContainerTag.PRODUCT_ID).toString(), false));
					getLogger().info("RFID validation process failed");
				} else {
					EventBus.publish(new RfidProductInspectionRequest(dc.get(
							DataContainerTag.PRODUCT_ID).toString(), true));
					dc.put("errorCode", new StringBuilder(RfidErrorCodes.NO_ERRORS.getValue()));
					dc.put("errorMessage", new StringBuilder(RfidErrorCodes.NO_ERRORS.getErrorMessage()));
					getLogger().info("RFID validation process successfull");
				}
				
				getBean().put("galcDataReady", new StringBuilder("1"),
						DeviceTagType.PLC_GALC_DATA_READY);
				getBean().put("errorCode", new StringBuilder(dc.get("errorCode").toString()));
				getBean().put("errorMessage", new StringBuilder(dc.get("errorMessage").toString()));
				getLogger().info("Sent error code "+dc.get("errorCode").toString()+" to PLC");
				getLogger().info("Sent message "+dc.get("errorMessage").toString()+" to PLC");
			} else
				getLogger().info("RFID Attributes not found at PLC");
			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0"));
			
			
		}
	}

}
