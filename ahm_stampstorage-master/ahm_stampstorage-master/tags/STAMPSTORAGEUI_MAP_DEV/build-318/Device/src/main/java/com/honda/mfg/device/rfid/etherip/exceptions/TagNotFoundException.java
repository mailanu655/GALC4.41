package com.honda.mfg.device.rfid.etherip.exceptions;

/**
 * User: Jeffrey M Lutz
 * Date: 4/14/11
 */
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
        super();
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(Throwable cause) {
        super(cause);
    }
}
