package com.honda.galc.client.product.processor;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.ProductPlcData;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.DeviceDataType;

import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.MbpnProductOnService;

public class MbpnProcessor extends PlcDataReadyEventProcessorBase implements
		IPlcDataReadyEventProcessor<ProductPlcData> {

	public synchronized boolean execute(ProductPlcData deviceData) {
		String errorCode = "1";

		try {
			DataContainer dc = new DefaultDataContainer();
			dc.setClientID(getPlcDeviceId());
			dc.put(DataContainerTag.PRODUCT_ID.toString(), deviceData
					.getProductId().toString());
			dc.put(DataContainerTag.PRODUCTION_LOT.toString(), deviceData
					.getProductionLot().toString());
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE.toString(), deviceData
					.getProductSpecCode().toString());
			
			DataContainer dc1 = new DefaultDataContainer();

			dc1 = ServiceFactory.getService(MbpnProductOnService.class)
					.execute(dc);
			if (dc1.containsKey(DataContainerTag.ERROR_CODE)) {
				errorCode = dc1.getString(DataContainerTag.ERROR_CODE);
			}
			getLogger().info("MBPN OnService Process Successful");
		} catch (Exception ex) {
			errorCode = "99";
			ex.printStackTrace();
			getLogger().info("Error occur at MBPN OnService Process");
		} finally {
			try {
				getBean().put("galcDataError", new StringBuilder(errorCode),
						DeviceDataType.INTEGER);
				getBean().put("galcDataReady", new StringBuilder("1"),
						DeviceDataType.INTEGER);
			} catch (Exception ex) {
			}
		}
		return true;
	}

	public void validate() {

	}

	public void postPlcWrite(boolean writeSucceeded) {

	}

}
