package com.honda.galc.client.device.plc.omron.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 22, 2010
 * Time: 10:34:03 AM
 */
public class FinsHeaderException extends RuntimeException {

    public FinsHeaderException() {
    }

    public FinsHeaderException(String message) {
        super(message);
    }

    public FinsHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinsHeaderException(Throwable cause) {
        super(cause);
    }
}
