package com.honda.mfg.mesproxy;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * User: Adam S. Kendell
 * Date: 6/20/11
 */
public class ClientConnectionTest extends TestCase {

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
    ClientConnectionManager cm = mock(ClientConnectionManager.class);
    private static final int GOOD_RETRY_RATE = 500;
    private static final int GOOD_QUEUE_SIZE = 5;
    private static final String GOOD_MESSAGE_TERMINATOR = "{END_OF_MESSAGE}";

    public void testSuccessfullyCreateClientConnection() {
        new ClientConnection(socket, cm);
    }

    public void testThrowsExceptionConstructingClientConnectionWithNullSocket() {
        try {
            new ClientConnection(null, cm);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
        }
    }

//    public void testThrowsExceptionConstructingClientConnectionWithNullConnectionManager() {
//        try {
//            new ClientConnection(socket, null);
//            Thread.sleep(10000);
//            fail("Should throw a NullPointerException");
//        } catch (InterruptedException e) {
//            fail("Should throw a NullPointerException");
//        } catch (NullPointerException e) {
//        }
//    }


    public void testOfferToInputQueue() {
        when(cm.offerToInputQueue(anyString())).thenReturn(true);

        ClientConnection c = new ClientConnection(socket, cm);
        assertTrue(c.offerToInputQueue("Test"));
        assertFalse(c.offerToInputQueue(null));
    }

    public void testWriteToOutputStream() throws IOException {
        String expectedOut = "Test";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(out);

        ClientConnection c = new ClientConnection(socket, cm);
        c.writeToOutputStream(expectedOut);
        String actualOut = out.toString();
        assertEquals(expectedOut, actualOut);
    }

    public void testAgentDeathNotification() throws IOException {
        socket = mock(Socket.class);
        OutputStream out = mock(OutputStream.class);
        InputStream in = mock(InputStream.class);

        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);

        byte[] b = "T".getBytes();

        doThrow(new IOException()).when(out).write(anyByte());
        doThrow(new IOException()).when(out).write(anyInt());
        doThrow(new IOException()).when(out).write(eq(b), anyInt(), anyInt());

        ClientConnection c = new ClientConnection(socket, cm);
        c.writeToOutputStream("T");

//        verify(out).write(eq(b), anyInt(), anyInt());
//        verify(in).close();
//        verify(out).close();
//        verify(socket).close();
//        verify(cm).removeConnection(c);

    }
}
