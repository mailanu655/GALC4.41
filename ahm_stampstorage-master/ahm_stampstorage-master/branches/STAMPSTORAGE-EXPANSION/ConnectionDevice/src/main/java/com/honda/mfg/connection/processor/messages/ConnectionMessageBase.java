package com.honda.mfg.connection.processor.messages;

import com.honda.mfg.connection.processor.MessageValidator;

/**
 * User: vcc30690 Date: 4/15/11
 */
public class ConnectionMessageBase implements ConnectionMessage {
	private String message;

	public ConnectionMessageBase(String message) {
		MessageValidator msgValidator = new MessageValidator();
		if (msgValidator.isAlreadyWrapped(message)) {
			this.message = message;
		} else {
			this.message = "{\"GeneralMessage\":" + message + "}";
		}
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String toString() {
		return message;
	}
}
