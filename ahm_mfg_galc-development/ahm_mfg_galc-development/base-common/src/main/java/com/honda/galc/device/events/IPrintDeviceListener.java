package com.honda.galc.device.events;

import com.honda.galc.device.IDeviceUser;

/**
 * @author Subu Kathiresan
 * Feb 28, 2014
 */
public interface IPrintDeviceListener extends IDeviceUser {
	
	public String getId();
	
	public void handleStatusChange(PrintDeviceStatusInfo statusInfo);
}
