package com.honda.galc.client.device;

import java.util.Date;

import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.net.MessageHandler;
import com.honda.galc.openprotocol.OPMessageHelper;
import com.honda.galc.openprotocol.OPMessageParser;
import com.honda.galc.openprotocol.OPMessageType;
import com.honda.galc.openprotocol.model.AbstractOPMessage;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.CommandError;
import com.honda.galc.openprotocol.model.ILastTighteningResult;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.OPCommandError;
import com.honda.galc.openprotocol.model.PowerMacsLastTighteningResult;
import com.honda.galc.openprotocol.model.SpindleStatus;

public class TorqueMessageHandler extends MessageHandler<String>{
	
	public static final int REVERSE_TORQUE = 2;
	public static final String DEVICE_MESSAGE_INVALID = "Invalid message received";

	protected TorqueSocketDevice device;
	
	private String _lastTighteningId = "";
	
	private OPMessageParser opMessageParser;
	
	private OPMessageHelper opMessageHelper;
	
	public TorqueMessageHandler(TorqueSocketDevice device) {
		this.device = device;
	}

	@Override
	public void processItem(String opMessage) {
		AbstractOPMessage msg = null;
		device.setLastPingReply(new Date().getTime());

		try {
			msg = getOpMessageParser().convertToBean(opMessage);
			msg.setCreatedTime(new Date());
			if (msg instanceof LastTighteningResult) 
				handleLastTighteningResult(msg);
			else if(msg instanceof CommandError)
				handleCommandErrors(msg);
			else if(msg instanceof MultiSpindleResultUpload){
				handleMultiSpindleResult((MultiSpindleResultUpload)msg);
			}if(msg instanceof CommandAccepted) {
				handleCommandAccepted(msg);
			}
				
		} catch (Exception ex) {
			ex.printStackTrace();
			device.getLogger().error(ex,"Invalid message " + msg == null ? "null" : msg.getMessageId() + " received from Torque device ");
			
			TorqueDeviceStatusInfo status = new TorqueDeviceStatusInfo();
			status.setMessage(DEVICE_MESSAGE_INVALID);
			status.setMessageSeverity(DeviceMessageSeverity.error);
			
			device.notifyListeners(status);
			return;
		}
	}

	protected void handleMultiSpindleResult(MultiSpindleResultUpload multiSpindleResult) {
		device.send(getOpMessageHelper().createMessage(OPMessageType.multiSpindleResultUploadAck, device.getTags()));
		
		if (multiSpindleResult.getSyncTighteningId().equalsIgnoreCase(_lastTighteningId))
			return;

		adjustMultiSpindleResult(multiSpindleResult);
		_lastTighteningId = multiSpindleResult.getSyncTighteningId();
		device.notifyListeners(multiSpindleResult);
	}

	
	/**
	 * handles last tightening results received from the torque device
	 * 
	 * @param msg
	 */
	protected void handleLastTighteningResult(AbstractOPMessage msg) {
		// notify if last tightening result was received
		if (msg instanceof LastTighteningResult) {
			device.send(getOpMessageHelper().createMessage(OPMessageType.lastTighteningResultDataAck));
			LastTighteningResult tighteningResult = (LastTighteningResult) msg;
			tighteningResult.setDeviceId(device.getDeviceId());
			adjustTighteningResult(tighteningResult);

			// skip notification if tightening result is a duplicate
			if (tighteningResult.getTighteningId().equalsIgnoreCase(_lastTighteningId)) {
				device.getLogger().check("Duplicate Torque " + tighteningResult.getTighteningId() + " received from Torque device " + device.getId());
				return;
			}
			// skip notification if tightening result is a reverse
			if (tighteningResult.getTighteningStatus() == REVERSE_TORQUE){
				device.getLogger().check("Reverse Torque " + tighteningResult.getTighteningId() + " received from Torque device " + device.getId());
				return;
			}

			_lastTighteningId = tighteningResult.getTighteningId();
			device.notifyListeners(tighteningResult);
		}
	}
	
	
	/**
	 * handles last tightening results received from the torque device
	 * 
	 * @param msg
	 */
	protected void handlePowerMacsLastTighteningResult(AbstractOPMessage msg) {
		// notify if last tightening result was received
		if (msg instanceof PowerMacsLastTighteningResult) {
			device.send(getOpMessageHelper().createMessage(OPMessageType.lastTighteningPowerMACSResultDataAck));
			PowerMacsLastTighteningResult powerTighteningResult = (PowerMacsLastTighteningResult) msg;
			
			LastTighteningResult tighteningResult = createLastTighteningResult(powerTighteningResult);
			tighteningResult.setTorque(tighteningResult.getTorque() * 100.0);
			adjustTighteningResult(tighteningResult);

			// skip notification if tightening result is a duplicate
			if (tighteningResult.getTighteningId().equalsIgnoreCase(_lastTighteningId)) {
				device.getLogger().check("Duplicate Torque " + tighteningResult.getTighteningId() + " received from Torque device " + device.getId());
				return;
			}
			// skip notification if tightening result is a reverse
			if (tighteningResult.getTighteningStatus() == REVERSE_TORQUE){
				device.getLogger().check("Reverse Torque " + tighteningResult.getTighteningId() + " received from Torque device " + device.getId());
				return;
			}

			_lastTighteningId = tighteningResult.getTighteningId();
			device.notifyListeners(tighteningResult);
		}
	}
	
	
	private void adjustTighteningResult(ILastTighteningResult lastTighteningResult) {
		
		// To conform with torque data from EI device, change value format
		lastTighteningResult.setTorque(lastTighteningResult.getTorque()/100.0);
		lastTighteningResult.setAngle(lastTighteningResult.getAngle());
		
	}
	
	private LastTighteningResult createLastTighteningResult(ILastTighteningResult iResult) {
		
		LastTighteningResult lastTighteningResult = new LastTighteningResult();
		// To conform with torque data from EI device, change value format
		lastTighteningResult.setTorque(iResult.getTorque());
		lastTighteningResult.setAngle(iResult.getAngle());
		lastTighteningResult.setAngleStatus(1);
		lastTighteningResult.setTorqueStatus(1);
		lastTighteningResult.setTighteningStatus(iResult.getTighteningStatus());
		lastTighteningResult.setTighteningId(iResult.getTighteningId());
		lastTighteningResult.setProductId(iResult.getProductId());
		return lastTighteningResult;
	}
	
	private void adjustMultiSpindleResult(MultiSpindleResultUpload multiSpindleResult) {
		for(SpindleStatus status : multiSpindleResult.getSpindleStatusList()){
			status.setTorqueResult(status.getTorqueResult()/100.0);
			status.setAngleResult(status.getAngleResult());
		}
	}

	/**
	 * handles command errors received from the torque device
	 * 
	 * @param msg
	 */
	protected void handleCommandErrors(AbstractOPMessage msg) {
		// notify if error was encountered
		if (msg instanceof CommandError) {
			TorqueDeviceStatusInfo status = new TorqueDeviceStatusInfo();
			Integer errorCode = Integer.parseInt(((CommandError) msg).getErrorCode());
			OPCommandError opCommandError = OPCommandError.get(errorCode);
			status.setCommandError(opCommandError);
			status.setMessageSeverity(DeviceMessageSeverity.error);

			//Following errors are ignored since they are not "errors" and the users don't need to take any corrective actions
			//1. Atlas Copco always answers CommunicationStart with "client already connected" command error
			//2. If you subscribe to last tightening result more than once we get a "subscription already exists" command error
			if (errorCode == OPCommandError.clientAlreadyConnected.getErrorCode() ||
					errorCode == OPCommandError.lastTighteningResultSubAlreadyExists.getErrorCode() ||
					errorCode == OPCommandError.jobNotRunning.getErrorCode())
				return;
			
			// When PSET errors will occur then torque device will get disabled
			if (isPsetError(errorCode)) {
				device.disable();
			}
			status.setMessage("Device " + status.getMessageSeverity().toString() + ": " + opCommandError.getMessage());
			device.notifyListeners(status);
		}
	}

	public OPMessageParser getOpMessageParser() {
		if(opMessageParser == null)
			opMessageParser = new OPMessageParser(device.getLogger());
		
		return opMessageParser;
	}

	public OPMessageHelper getOpMessageHelper() {
		if(opMessageHelper == null)
			opMessageHelper = new OPMessageHelper(device.getLogger());
		
		return opMessageHelper;
	}

	/**
	 * handles command accepted received from the torque device
	 * 
	 * @param msg
	 */
	private void handleCommandAccepted(AbstractOPMessage msg) {
		// notify if command accepted
		if (msg instanceof CommandAccepted) {
			CommandAccepted commandAccepted = (CommandAccepted) msg;
			device.notifyListeners(commandAccepted);
		}
	}
	
	private boolean isPsetError(Integer errorCode) {
		// When following PSET errors will occur then return true.
		if (errorCode == OPCommandError.psetNotPresent.getErrorCode()
				|| errorCode == OPCommandError.psetCannotBeSet.getErrorCode()
				|| errorCode == OPCommandError.psetNotRunning.getErrorCode()) {
			return true;
		}
		return false;
	}
}
