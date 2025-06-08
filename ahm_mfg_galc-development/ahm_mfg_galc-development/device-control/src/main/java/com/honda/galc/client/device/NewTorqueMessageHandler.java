/**
 * 
 */
package com.honda.galc.client.device;

import java.util.Date;

import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.openprotocol.model.AbstractOPMessage;
import com.honda.galc.openprotocol.model.CommandError;
import com.honda.galc.openprotocol.model.CommunicationStartAck;
import com.honda.galc.openprotocol.model.ILastTighteningResult;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.PowerMacsLastTighteningResult;

/**
 * @author vcc44349
 *
 */
public class NewTorqueMessageHandler extends TorqueMessageHandler {

	/* (non-Javadoc)
	 * @see com.honda.galc.client.device.TorqueMessageHandler#processItem(java.lang.String)
	 */
	@Override
	public void processItem(String opMessage) {
		AbstractOPMessage msg = null;
		device.setLastPingReply(new Date().getTime());

		try {
			
			msg = getOpMessageParser().convertToBean(opMessage);
			msg.setCreatedTime(new Date());
			if (msg instanceof LastTighteningResult) 
				handleLastTighteningResult(msg);
			else if (msg instanceof PowerMacsLastTighteningResult) 
				handlePowerMacsLastTighteningResult(msg);
			else if(msg instanceof CommandError)
				handleCommandErrors(msg);
			else if(msg instanceof MultiSpindleResultUpload){
				handleMultiSpindleResult((MultiSpindleResultUpload)msg);
			}
			else if (msg instanceof CommunicationStartAck)  {
				device.setCommunicationStart(true);
			}
				
		} catch (Exception ex) {
			ex.printStackTrace();
			device.getLogger().error(ex,"Invalid message " + msg.getMessageId() + " received from Torque device ");
			
			TorqueDeviceStatusInfo status = new TorqueDeviceStatusInfo();
			status.setMessage(DEVICE_MESSAGE_INVALID);
			status.setMessageSeverity(DeviceMessageSeverity.error);
			
			device.notifyListeners(status);
			return;
		}
	}

	/**
	 * @param device
	 */
	public NewTorqueMessageHandler(TorqueSocketDevice device) {
		super(device);
	}

}
