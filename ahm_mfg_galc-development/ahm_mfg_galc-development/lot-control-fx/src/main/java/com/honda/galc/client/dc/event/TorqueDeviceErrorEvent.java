package com.honda.galc.client.dc.event;

import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.ui.IEvent;

public class TorqueDeviceErrorEvent implements IEvent {

	private String deviceId;

	private TorqueDeviceStatusInfo statusInfo;

	public TorqueDeviceErrorEvent(String deviceId, TorqueDeviceStatusInfo statusInfo) {
		this.deviceId = deviceId;
		this.statusInfo = statusInfo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public TorqueDeviceStatusInfo getStatusInfo() {
		return statusInfo;
	}
}