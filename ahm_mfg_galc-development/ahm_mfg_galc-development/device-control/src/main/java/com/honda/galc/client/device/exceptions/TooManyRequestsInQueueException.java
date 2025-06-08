package com.honda.galc.client.device.exceptions;

/**
 * @author Subu Kathiresan
 * @date Dec 20, 2012
 */
public class TooManyRequestsInQueueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TooManyRequestsInQueueException() {
	}

	public TooManyRequestsInQueueException(String message) {
		super(message);
	}

	public TooManyRequestsInQueueException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooManyRequestsInQueueException(Throwable cause) {
		super(cause);
	}
}
