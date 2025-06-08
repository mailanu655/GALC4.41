package com.honda.galc.client.device.plc.omron.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 22, 2010
 * Time: 10:34:03 AM
 */
public class FinsResponseException extends RuntimeException {

    public FinsResponseException() {
    }

    public FinsResponseException(String message) {
        super(message);
    }

    public FinsResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinsResponseException(Throwable cause) {
        super(cause);
    }
}
