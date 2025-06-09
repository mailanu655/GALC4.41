package com.honda.mfg.mesproxy;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: Adam S. Kendell
 * Date: 6/21/11
 */
public class ServerConnectionTest extends TestCase {


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
    ServerConnectionManager cm = mock(ServerConnectionManager.class);
    private static final int GOOD_RETRY_RATE = 500;
    private static final int GOOD_QUEUE_SIZE = 5;
    private static final String GOOD_MESSAGE_TERMINATOR = "{END_OF_MESSAGE}";


    public void testSuccessfullyCreateServerConnection() {
        new ServerConnection(socket, cm);
    }

    public void testThrowsExceptionConstructingServerConnectionWithNullSocket() {
        try {
            new ServerConnection(null, cm);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
        }
    }

//    public void testThrowsExceptionConstructingServerConnectionWithNullConnectionManager() {
//        try {
//            new ServerConnection(socket, null);
//            Thread.sleep(10000);
//            fail("Should throw a NullPointerException");
//        } catch (InterruptedException e) {
//            fail("Should throw a NullPointerException");
//        } catch (NullPointerException e) {
//        }
//    }


    public void testOfferToInputQueue() {
        when(cm.offerToInputQueue(anyString())).thenReturn(true);

        ServerConnection c = new ServerConnection(socket, cm);
        assertTrue(c.offerToInputQueue("Test"));
        //verify(cm.offerToInputQueue("Test"));
        assertFalse(c.offerToInputQueue(null));

    }

    public void testWriteToOutputStream() throws IOException {
        String expectedOut = "Test";
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        when(socket.getOutputStream()).thenReturn(out);

        ServerConnection c = new ServerConnection(socket, cm);
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

        ServerConnection c = new ServerConnection(socket, cm);
        c.writeToOutputStream("T");

//        verify(out).write(eq(b), anyInt(), anyInt());
//        verify(in).close();
//        verify(out).close();
//        verify(socket).close();
//        verify(cm).removeConnection(c);

    }

    public void testGetMessageTerminator() {
        when(cm.getMessageTerminator()).thenReturn(GOOD_MESSAGE_TERMINATOR);
        ServerConnection c = new ServerConnection(socket, cm);
        assertEquals(GOOD_MESSAGE_TERMINATOR, c.getMessageTerminator());

    }

//    public void testProperties() throws IOException {
//        Properties p = new Properties();
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("MesProxy.properties");
//        p.load(is);
//        ArrayList<String> hosts = new ArrayList<String>();
//
//        hosts.add(0, "localhost");
//        p.setProperty("allowedHosts", hosts.toString());
//        FileOutputStream out = new FileOutputStream("MesProxy.properties1");
//        p.store(out,"Test");
//        out.close();
//
////        FileInputStream in = new FileInputStream("MesProxy.properties1");
////        p.load(in);
////        ArrayList<String> hosts2 = new ArrayList<String>();
////        hosts2 = p.getProperty("allowedHosts");
//
//    }

}
