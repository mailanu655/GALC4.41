/**
 * 
 */
package com.honda.galc.client.schedule;

import com.honda.galc.common.message.MessageType;

/**
 * @author Subu Kathiresan
 * @date Jan 24, 2013
 */
public class DisplayMessageEvent {
	
	private String _message;
	private MessageType _messageType = MessageType.INFO;

	public DisplayMessageEvent(String message) {
		super();
		_message = message;
	}
	
	public DisplayMessageEvent(String message, MessageType msgType) {
		this(message);
		_messageType = msgType;
	}

	public String getmessage() {
		return _message;
	}

	public void setmessage(String message) {
		_message = message;
	}
	
	public MessageType getMessageType() {
		return _messageType;
	}

	public void setMessageType(MessageType msgType) {
		_messageType = msgType;
	}
}

