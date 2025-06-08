package com.honda.galc.common.exception;

import com.honda.galc.common.message.MessageType;

public class TrackException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4997449694921900660L;
	private MessageType messageType;
	
	public TrackException(String aMessage) {
		super(aMessage);
	}
	
	public TrackException(String message, MessageType messageType) {
		super(message);
		this.messageType = messageType;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

}
