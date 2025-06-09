package com.honda.mfg.connection.exceptions;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 22, 2010
 * Time: 3:24:37 PM
 */
public class CommunicationsException extends RuntimeException {
    public CommunicationsException() {
    }

    public CommunicationsException(String message) {
        super(message);
    }

    public CommunicationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommunicationsException(Throwable cause) {
        super(cause);
    }
}
