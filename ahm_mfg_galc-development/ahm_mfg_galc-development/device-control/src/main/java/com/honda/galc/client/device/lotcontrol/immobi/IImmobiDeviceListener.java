package com.honda.galc.client.device.lotcontrol.immobi;

import com.honda.galc.device.IDeviceUser;




/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public interface IImmobiDeviceListener extends IDeviceUser
{
	public String getListenerId();
		
	public void handleStatusChange(ImmobiDeviceStatusInfo statusInfo);
}
