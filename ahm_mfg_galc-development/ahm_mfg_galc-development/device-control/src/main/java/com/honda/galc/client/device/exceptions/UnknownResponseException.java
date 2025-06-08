package com.honda.galc.client.device.exceptions;

/**
 * User: Jeffrey M Lutz
 * Date: Jan 27, 2011
 */
public class UnknownResponseException extends RuntimeException {
    public UnknownResponseException() {
    }

    public UnknownResponseException(String message) {
        super(message);
    }

    public UnknownResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownResponseException(Throwable cause) {
        super(cause);
    }
}
