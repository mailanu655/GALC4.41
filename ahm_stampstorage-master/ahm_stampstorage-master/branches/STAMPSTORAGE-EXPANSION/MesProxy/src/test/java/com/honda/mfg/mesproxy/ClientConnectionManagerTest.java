package com.honda.mfg.mesproxy;

import junit.framework.TestCase;

import java.net.Socket;

import static org.mockito.Mockito.mock;

/**
 * User: Adam S. Kendell
 * Date: 6/20/11
 */
public class ClientConnectionManagerTest extends TestCase {
    private static final int NORMAL_CONNECT_TIMEOUT = 5;
    private final String GOOD_HOST = "localhost";
    private final String NULL_HOST = null;
    private final String EMPTY_HOST = "";

    static final int MIN_PORT_NUMBER = 1;
    static final int MAX_PORT_NUMBER = 65535;

    private final int GOOD_PORT = 1000;
    private final int TOO_SMALL_PORT = MIN_PORT_NUMBER - 1;
    private final int TOO_BIG_PORT = MAX_PORT_NUMBER + 1;

    private final int GOOD_CONNECT_TIMEOUT = 500;
    private final int TOO_SMALL_CONNECT_TIMEOUT = 1;
    //    private TestableSocketServer server;
    Socket socket = mock(Socket.class);

    private static final int GOOD_RETRY_RATE = 500;
    private static final int GOOD_QUEUE_SIZE = 5;
    private static final String GOOD_MESSAGE_TERMINATOR = "{END_OF_MESSAGE}";


    public void testSuccessfullyCreateClientConnectionManager() {
        new ClientConnectionManager(GOOD_PORT, GOOD_HOST, GOOD_CONNECT_TIMEOUT, GOOD_RETRY_RATE, false, GOOD_QUEUE_SIZE);
    }

    public void testThrowsExceptionConstructingClientConnectionManagerWithBadPort() {
        try {
            new ClientConnectionManager(TOO_SMALL_PORT, GOOD_HOST, GOOD_CONNECT_TIMEOUT, GOOD_RETRY_RATE, false, GOOD_QUEUE_SIZE);
            fail("Should throw a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testConnectionRetry() {
    }

}
