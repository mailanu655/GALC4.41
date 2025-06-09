package com.honda.mfg.stamp.storage.service.messagebuilders;

/**
 * User: vcc30690 Date: 3/23/11
 */
public class UnknownStorageMessageException extends RuntimeException {

	public UnknownStorageMessageException(String message) {
		super(message);
	}

	public UnknownStorageMessageException(String message, Throwable cause) {
		super(message, cause);
	}
}
