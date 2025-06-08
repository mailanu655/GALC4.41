package com.honda.galc.client.events.wetracking;

import java.util.ArrayList;

import com.honda.galc.client.events.AbstractPlcDataReadyEvent;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.util.KeyValue;

public class WeldOnNextLotRequest extends AbstractPlcDataReadyEvent implements WeldTrackingRequest {
	
	private String currentLotNumber = "";
	
	public WeldOnNextLotRequest(String applicationId, String plcDeviceId) {
		super(applicationId, plcDeviceId);
	}
	
	public String getCurrentLotNumber() {
		return currentLotNumber;
	}

	public void setCurrentLotNumber(String currentLotNumber) {
		this.currentLotNumber = currentLotNumber;
	}

	@Override
	public DataContainer convertToDataContainer() {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(getPlcDeviceId());
		dc.put(DataContainerTag.CURRENT_LOT, currentLotNumber);
		ArrayList<KeyValue<String,StringBuilder>> attributes = getAttributes();
		for(KeyValue<String, StringBuilder> attribute : attributes) {
			dc.put(attribute.getKey(), attribute.getValue());
		}
		return dc;
	}
		
}
