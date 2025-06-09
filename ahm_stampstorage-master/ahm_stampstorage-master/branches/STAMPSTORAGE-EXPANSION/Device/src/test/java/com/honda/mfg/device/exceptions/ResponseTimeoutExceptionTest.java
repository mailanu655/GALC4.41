package com.honda.mfg.device.exceptions;

import org.junit.Test;

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
