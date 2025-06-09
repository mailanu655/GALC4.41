package com.honda.mfg.device.exceptions;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 22, 2010
 * Time: 3:24:37 PM
 */
public class NetworkCommunicationException extends RuntimeException {
    public NetworkCommunicationException() {
    }

    public NetworkCommunicationException(String message) {
        super(message);
    }

    public NetworkCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkCommunicationException(Throwable cause) {
        super(cause);
    }
}
