package com.honda.galc.device;


/**
 * @author Subu Kathiresan
 * Mar 24, 2009
 */
public interface IDeviceUser 
{
	public String getApplicationName();
	
	public Integer getDeviceAccessKey(String deviceId);
	
	public void controlGranted(String deviceId);
	
	public void controlRevoked(String deviceId);
}
