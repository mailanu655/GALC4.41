package com.honda.galc.device.events;

import java.util.Date;

import com.honda.galc.constant.DeviceMessageSeverity;

/**
 * @author Subu Kathiresan
 * Feb 15, 2009
 */
public abstract class DeviceStatusInfo 
{
	protected String _msg = null;
	protected String _deviceId = null;
	public String getDeviceId() {
		return _deviceId;
	}

	public void setDeviceId(String id) {
		_deviceId = id;
	}

	public String getDeviceName() {
		return _deviceName;
	}

	public void setDeviceName(String name) {
		_deviceName = name;
	}

	protected String _deviceName = null;
	protected Date _lastPingAt = null;
	protected DeviceMessageSeverity _msgSeverity = DeviceMessageSeverity.warning;
		
	/**
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return _msg;
	}
	
	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message)
	{
		_msg = message;
	}
	
	/**
	 * 
	 * @return
	 */
	public DeviceMessageSeverity getMessageSeverity()
	{
		return _msgSeverity;
	}
	
	/**
	 * 
	 * @param msgSeverity
	 */
	public void setMessageSeverity(DeviceMessageSeverity msgSeverity)
	{
		_msgSeverity = msgSeverity;
	}
}
