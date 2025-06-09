package com.honda.mfg.connection.exceptions;

import org.junit.Test;

import com.honda.mfg.connection.exceptions.CommunicationsException;

/**
 * User: Jeffrey M Lutz
 * Date: 4/13/11
 */
public class CommunicationsExceptionTest {

    @Test
    public void testAll() {
        CommunicationsException e = new CommunicationsException();
        new CommunicationsException("Hello");
        new CommunicationsException("Hello", e);
        new CommunicationsException(e);
    }
}
