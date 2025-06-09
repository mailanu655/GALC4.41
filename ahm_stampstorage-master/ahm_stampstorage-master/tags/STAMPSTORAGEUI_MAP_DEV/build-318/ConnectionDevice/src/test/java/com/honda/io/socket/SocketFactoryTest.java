package com.honda.io.socket;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static junit.framework.Assert.*;

/**
 * User: Jeffrey M Lutz
 * Date: 4/1/11
 */
public class SocketFactoryTest {

    private static final int NORMAL_CONNECT_TIMEOUT = 5;
    private final String GOOD_HOST = "localhost";
    private final String NULL_HOST = null;
    private final String EMPTY_HOST = "";

    private final int GOOD_PORT = SocketFactory.MAX_PORT_NUMBER;
    private final int TOO_SMALL_PORT = SocketFactory.MIN_PORT_NUMBER - 1;
    private final int TOO_BIG_PORT = SocketFactory.MAX_PORT_NUMBER + 1;

    private final int GOOD_CONNECT_TIMEOUT = SocketFactory.MIN_CONNECT_TIMEOUT;
    private final int TOO_SMALL_CONNECT_TIMEOUT = SocketFactory.MIN_CONNECT_TIMEOUT - 1;
    private TestableSocketServer server;

    @After
    public void after() {
        if (server != null) {
            server.disconnect();
        }
    }

    @Test
    public void successfullyConstructSocketFactory() {
        new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionConstructingSocketFactoryWithNullHostname() {
        new SocketFactory(NULL_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionConstructingSocketFactoryWithEmptyHostname() {
        new SocketFactory(EMPTY_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionConstructingSocketFactoryWithTooSmallPortNumber() {
        new SocketFactory(GOOD_HOST, TOO_SMALL_PORT, GOOD_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionConstructingSocketFactoryWithTooLargePortNumber() {
        new SocketFactory(GOOD_HOST, TOO_BIG_PORT, GOOD_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionConstructingSocketFactoryWithTooSmallConnectTimeout() {
        new SocketFactory(GOOD_HOST, GOOD_PORT, TOO_SMALL_CONNECT_TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionConstructingNewAddress() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.addressException = new IllegalArgumentException("Illegal argument exception...");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = SecurityException.class)
    public void throwsSecurityExceptionConstructingNewAddress() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.addressException = new SecurityException("Security manager permission issue.");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionConstructingNewSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.socketException = new IllegalArgumentException("If the proxy is of an invalid type or null.");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = SecurityException.class)
    public void throwsSecurityExceptionConstructingNewSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.socketException = new SecurityException("Security manager permission issue.");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = UnknownHostException.class)
    public void throwsUnknownHostExceptionBindingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.bindUnknownHostException = new UnknownHostException("What the ...?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IOException.class)
    public void throwsIOExceptionBindingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.bindIOException = new IOException("What the ...?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionBindingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.bindRuntimeException = new IllegalArgumentException("What the ...?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionConnectingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.connectSocketRuntimeException = new IllegalArgumentException("What the ...?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IllegalBlockingModeException.class)
    public void throwsIllegalBlockingModeExceptionConnectingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.connectSocketRuntimeException = new IllegalBlockingModeException();

        // Perform test
        sf.createSocket();
    }

    @Test(expected = SocketTimeoutException.class)
    public void throwsSocketTimeoutExceptionConnectingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.connectSocketTimeoutException = new SocketTimeoutException("What the ... ?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(expected = IOException.class)
    public void throwsIOExceptionConnectingSocket() throws IOException {
        // Pre-condition
        TestableSocketFactory sf = new TestableSocketFactory(GOOD_HOST, GOOD_PORT, NORMAL_CONNECT_TIMEOUT);
        sf.connectSocketIOException = new IOException("What the ... ?!?!");

        // Perform test
        sf.createSocket();
    }

    @Test(timeout = 10 * 1000L)
    public void successfullyConnectSocketToServer() throws IOException {
        // Pre-condition

        server = new TestableSocketServer(GOOD_PORT);

        // Perform test
        assertEquals("For some reason the server has accepted the client socket early.",
                false, server.hasAcceptedClientConnection());
        SocketFactory sf = new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
        sf.createSocket();

        assertEquals(true, server.hasAcceptedClientConnection());

    }

    @Test
    public void noExceptionWhenServerDropsSocketConnectionSinceSocketCannotDetermineHealthOfItself() throws IOException {
        server = new TestableSocketServer(GOOD_PORT);

        SocketFactory sf = new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
        Socket socket = sf.createSocket();

        assertTrue(server.hasAcceptedClientConnection());

        server.disconnect();

        assertFalse(server.hasAcceptedClientConnection());
        assertTrue(socket.isConnected());
        InputStream input = socket.getInputStream();

        input.read();
    }

    class TestableSocketServer implements Runnable {
        final Logger LOG = LoggerFactory.getLogger(TestableSocketServer.class);

        private int port;
        private long connectWaitTimeout = 500L; // Wait 0.5 seconds
        private ServerSocket serverSocket;
        private volatile boolean started;
        private boolean connected;
        Socket clientSocket;

        public TestableSocketServer(int port) throws IOException {
            this.started = false;
            this.connected = false;
            this.port = port;
            ScheduledExecutorService scheduler =
                    Executors.newScheduledThreadPool(1);
            scheduler.execute(this);
            while (!started) {
            }
        }

        public boolean hasAcceptedClientConnection() {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < connectWaitTimeout) {
            }
            return connected;
        }

        public void run() {
            try {
                LOG.info("Starting server on port:  " + port);
                serverSocket = new ServerSocket(port);
                LOG.info("Getting client connection.");
                started = true;
                clientSocket = serverSocket.accept();
                connected = true;
                LOG.info("Accepted client socket connection.");
                while (1 == 1) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public void disconnect() {
            try {
                clientSocket.close();
            } catch (Throwable e) {
            }
            try {
                serverSocket.close();
            } catch (Throwable e) {
            }
            connected = false;
        }
    }

    class TestableSocketFactory extends SocketFactory {

        private RuntimeException socketException;

        private RuntimeException addressException;

        private UnknownHostException bindUnknownHostException;
        private RuntimeException bindRuntimeException;
        private IOException bindIOException;

        private RuntimeException connectSocketRuntimeException;
        private SocketTimeoutException connectSocketTimeoutException;
        private IOException connectSocketIOException;

        public TestableSocketFactory(String hostName, int portNumber, int connectTimeout) {
            super(hostName, portNumber, connectTimeout);
        }

        @Override
        protected void connectSocket(Socket socket, InetSocketAddress address) throws IllegalArgumentException, IllegalBlockingModeException, SocketTimeoutException, IOException {
            if (connectSocketRuntimeException != null) {
                throw connectSocketRuntimeException;
            }
            if (connectSocketTimeoutException != null) {
                throw connectSocketTimeoutException;
            }
            super.connectSocket(socket, address);
        }

        @Override
        protected void bindSocket(Socket socket)
                throws UnknownHostException, IOException, IllegalArgumentException {
            if (bindUnknownHostException != null) {
                throw bindUnknownHostException;
            }
            if (bindIOException != null) {
                throw bindIOException;
            }
            if (bindRuntimeException != null) {
                throw bindRuntimeException;
            }
            super.bindSocket(socket);
        }

        @Override
        protected Socket constructNewSocket() {
            if (socketException != null) {
                throw socketException;
            }
            return super.constructNewSocket();
        }

        @Override
        protected InetSocketAddress constructNewAddress() {
            if (addressException != null) {
                throw addressException;
            }
            return super.constructNewAddress();
        }

    }
}
