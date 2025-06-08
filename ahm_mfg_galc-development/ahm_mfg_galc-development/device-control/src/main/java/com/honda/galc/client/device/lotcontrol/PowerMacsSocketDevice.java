/**
 * 
 */
package com.honda.galc.client.device.lotcontrol;

import com.honda.galc.openprotocol.OPMessageType;

/**
 * @author vcc44349
 *
 */
public class PowerMacsSocketDevice extends NewTorqueSocketDevice {

	/**
	 * 
	 */
	public PowerMacsSocketDevice() {
		super();
	}
	
	/**
	 * Subscribes this device to the last tightening result data published
	 * by the torque controller
	 */
	public void subscribeToTighteningResultData() {
		try	{
			if(isMultiSpindle())
				send(getOpMessageHelper().createMessage(OPMessageType.multiSpindleResultSubscribe));
			else
				send(getOpMessageHelper().createMessage(OPMessageType.lastTighteningPowerMACSResultDataSubscribe));
		} catch(Exception ex)	{
			getLogger().error(ex.getMessage());
			getLogger().error("Unable to Subscribe to last tightening result data from the Torque device");
			ex.printStackTrace();
		}
	}


}
