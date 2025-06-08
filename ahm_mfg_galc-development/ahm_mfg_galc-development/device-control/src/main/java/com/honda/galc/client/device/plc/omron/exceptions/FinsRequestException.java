package com.honda.galc.client.device.plc.omron.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 22, 2010
 * Time: 10:34:03 AM
 */
public class FinsRequestException extends RuntimeException {

    public FinsRequestException() {
    }

    public FinsRequestException(String message) {
        super(message);
    }

    public FinsRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinsRequestException(Throwable cause) {
        super(cause);
    }
}
