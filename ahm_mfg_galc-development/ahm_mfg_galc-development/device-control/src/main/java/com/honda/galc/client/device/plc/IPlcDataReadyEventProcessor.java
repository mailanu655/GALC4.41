/**
 * 
 */
package com.honda.galc.client.device.plc;

import com.honda.galc.device.IDeviceData;

/**
 * @author Subu Kathiresan
 * @date Nov 2, 2012
 */
public interface IPlcDataReadyEventProcessor<T extends IDeviceData> {

	public String getApplicationId();	
	
	public void setApplicationId(String applicationId);

	public String getPlcDeviceId();

	public void setPlcDeviceId(String deviceId);
	
	public boolean execute(T deviceData);
	
	public void validate();
	
	public void postPlcWrite(boolean writeSucceeded);
}
