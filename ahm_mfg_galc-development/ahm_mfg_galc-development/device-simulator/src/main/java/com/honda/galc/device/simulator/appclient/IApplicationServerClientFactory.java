package com.honda.galc.device.simulator.appclient;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.device.simulator.client.EquipmentApplicationServerClient;


public interface IApplicationServerClientFactory {
	public abstract EquipmentApplicationServerClient getApplicationServerClient(String srvUrl, int clntype) throws SystemException;

}
