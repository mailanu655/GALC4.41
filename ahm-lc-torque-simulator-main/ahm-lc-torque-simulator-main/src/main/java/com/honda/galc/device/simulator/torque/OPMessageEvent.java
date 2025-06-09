package com.honda.galc.device.simulator.torque;

import com.honda.galc.client.ui.IEvent;

/**
 * @author Subu Kathiresan
 * @date Jan 7, 2015
 */
public class OPMessageEvent implements IEvent {

	private String deviceId = null;
	private OPMessageType msgType = null;
	private String message = "";
	
	public OPMessageEvent(String deviceId, OPMessageType msgType, String message) {
		this.deviceId = deviceId;
		this.msgType = msgType;
		this.message = message;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public OPMessageType getMsgType() {
		return msgType;
	}
	
	public void setMsgType(OPMessageType msgType) {
		this.msgType = msgType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
