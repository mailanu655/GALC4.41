package com.honda.galc.client.device.lotcontrol;

import com.honda.galc.openprotocol.model.OPCommandError;
import com.honda.galc.device.events.DeviceStatusInfo;

/**
 * @author Subu Kathiresan
 * Feb 22, 2009
 */
public class TorqueDeviceStatusInfo extends DeviceStatusInfo
{
	private OPCommandError _commandError = null;
	
	/**
	 * 
	 * @return
	 */
	public OPCommandError getCommandError()
	{
		return _commandError;
	}
	
	/**
	 * 
	 * @param commandError
	 */
	public void setCommandError(OPCommandError commandError)
	{
		_commandError = commandError;
	}
}
