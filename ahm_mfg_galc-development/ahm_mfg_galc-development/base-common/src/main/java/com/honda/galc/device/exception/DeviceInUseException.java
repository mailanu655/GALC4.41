package com.honda.galc.device.exception;

/**
 * @author Subu Kathiresan
 * Mar 24, 2009
 */
@SuppressWarnings("serial")
public class DeviceInUseException extends Exception 
{
	private String _deviceName = null;
	private String _applicationName = null;
	
	/**
	 * 
	 *
	 */
	public DeviceInUseException()
	{
		
	}
	
	/**
	 * 
	 * @param deviceName
	 * @param applicationName
	 */
	public DeviceInUseException(String deviceName, String applicationName)
	{
		_deviceName = deviceName;
		_applicationName = applicationName;		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeviceName()
	{
		return _deviceName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getApplicationName()
	{
		return _applicationName;
	}
}
