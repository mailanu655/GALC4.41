package com.honda.galc.device;



public interface DeviceListener {
	public IDeviceData received(String clientId, IDeviceData deviceData);
}
