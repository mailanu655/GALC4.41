package com.honda.galc.client.dc.event;

import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.ui.IEvent;

public class PsetErrorEvent extends TorqueDeviceErrorEvent implements IEvent {

	public PsetErrorEvent(String deviceId, TorqueDeviceStatusInfo statusInfo) {
		super(deviceId, statusInfo);
	}

}