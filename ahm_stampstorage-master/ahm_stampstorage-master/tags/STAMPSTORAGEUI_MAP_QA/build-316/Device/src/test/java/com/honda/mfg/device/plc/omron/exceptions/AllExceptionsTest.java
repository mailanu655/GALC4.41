package com.honda.mfg.device.plc.omron.exceptions;

import org.junit.Test;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class AllExceptionsTest {

    @Test
    public void testAllConstructors() {
        Exception e1 = new FinsHeaderException();
        Exception e2 = new FinsHeaderException("Hello");
        new FinsHeaderException("Hello", e1);
        new FinsHeaderException(e2);

        new FinsRequestException();
        new FinsRequestException("Hello");
        new FinsRequestException("Hello", e1);
        new FinsRequestException(e2);

        new FinsResponseException();
        new FinsResponseException("Hello");
        new FinsResponseException("Hello", e1);
        new FinsResponseException(e2);
    }
}
