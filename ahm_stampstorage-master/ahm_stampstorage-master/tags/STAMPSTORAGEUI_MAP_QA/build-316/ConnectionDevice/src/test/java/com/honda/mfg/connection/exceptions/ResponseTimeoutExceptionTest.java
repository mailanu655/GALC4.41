package com.honda.mfg.connection.exceptions;

import org.junit.Test;

import com.honda.mfg.connection.exceptions.ResponseTimeoutException;

/**
 * User: Jeffrey M Lutz
 * Date: 4/13/11
 */
public class ResponseTimeoutExceptionTest {

    @Test
    public void testAll() {
        ResponseTimeoutException e = new ResponseTimeoutException();
        new ResponseTimeoutException("Hello");
        new ResponseTimeoutException("Hello", e);
        new ResponseTimeoutException(e);
    }
}
