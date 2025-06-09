package com.honda.mfg.connection.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 22, 2010
 * Time: 10:34:03 AM
 */
public class ResponseTimeoutException extends RuntimeException {

    public ResponseTimeoutException() {
    }

    public ResponseTimeoutException(String message) {
        super(message);
    }

    public ResponseTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseTimeoutException(Throwable cause) {
        super(cause);
    }
}
