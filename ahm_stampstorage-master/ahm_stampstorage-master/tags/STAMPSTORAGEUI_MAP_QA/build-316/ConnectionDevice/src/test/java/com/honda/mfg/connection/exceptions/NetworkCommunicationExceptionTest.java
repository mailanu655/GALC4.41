package com.honda.mfg.connection.exceptions;

import org.junit.Test;

import com.honda.mfg.connection.exceptions.NetworkCommunicationException;

/**
 * User: Jeffrey M Lutz
 * Date: 4/13/11
 */
public class NetworkCommunicationExceptionTest {

    @Test
    public void testAll() {
        NetworkCommunicationException e = new NetworkCommunicationException();
        new NetworkCommunicationException("Hello");
        new NetworkCommunicationException("Hello", e);
        new NetworkCommunicationException(e);
    }
}
