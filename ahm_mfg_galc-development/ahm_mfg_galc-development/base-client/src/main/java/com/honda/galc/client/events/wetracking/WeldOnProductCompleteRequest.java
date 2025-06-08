package com.honda.galc.client.events.wetracking;

import java.util.ArrayList;

import com.honda.galc.client.events.AbstractPlcDataReadyEvent;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.util.KeyValue;

public class WeldOnProductCompleteRequest extends AbstractPlcDataReadyEvent implements WeldTrackingRequest{
	private String productId;
	private String currentLotNumber;
	private String actualTime;
	private String machineType;
	
	public WeldOnProductCompleteRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCurrentLotNumber() {
		return currentLotNumber;
	}

	public void setCurrentLotNumber(String currentLotNumber) {
		this.currentLotNumber = currentLotNumber;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	@Override
	public DataContainer convertToDataContainer() {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(getPlcDeviceId());
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		dc.put(DataContainerTag.CURRENT_LOT, currentLotNumber);
		dc.put(DataContainerTag.ACTUAL_TIME, actualTime);
		dc.put(DataContainerTag.MACHINE_TYPE, machineType);
		ArrayList<KeyValue<String,StringBuilder>> attributes = getAttributes();
		for(KeyValue<String, StringBuilder> attribute : attributes) {
			dc.put(attribute.getKey(), attribute.getValue());
		}
		return dc;
	}
}
