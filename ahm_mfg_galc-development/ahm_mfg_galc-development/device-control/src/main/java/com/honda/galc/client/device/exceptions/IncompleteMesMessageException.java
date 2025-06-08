package com.honda.galc.client.device.exceptions;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 22, 2010
 * Time: 3:24:37 PM
 */
public class IncompleteMesMessageException extends RuntimeException {
    public IncompleteMesMessageException() {
    }

    public IncompleteMesMessageException(String message) {
        super(message);
    }

    public IncompleteMesMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteMesMessageException(Throwable cause) {
        super(cause);
    }
}
