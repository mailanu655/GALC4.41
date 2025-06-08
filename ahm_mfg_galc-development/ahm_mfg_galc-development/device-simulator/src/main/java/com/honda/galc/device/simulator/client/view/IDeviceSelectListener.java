package com.honda.galc.device.simulator.client.view;

import com.honda.galc.entity.conf.Device;

public interface IDeviceSelectListener {

    public void addDeviceSelectListener(IDeviceSelectable deviceSelectable);

	public void setDeviceSelection(String deviceId);
	
	public Device getDevice(String clientId);

}
