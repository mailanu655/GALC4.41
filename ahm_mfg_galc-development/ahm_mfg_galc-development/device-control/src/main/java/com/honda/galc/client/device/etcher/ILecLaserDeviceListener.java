package com.honda.galc.client.device.etcher;

import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.IDeviceUser;

/**
 * @author Subu Kathiresan
 * @date Apr 21, 2017
 */
public interface ILecLaserDeviceListener extends IDeviceUser {

	public String getId();
	
	public void etchCompleted(String deviceId, String productId);
	
	public void handleDeviceStatusChange(String errMsg, DeviceMessageSeverity severity);
}
