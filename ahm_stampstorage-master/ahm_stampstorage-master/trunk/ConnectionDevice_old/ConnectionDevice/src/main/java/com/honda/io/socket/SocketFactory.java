package com.honda.io.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;

/**
 * User: Jeffrey M Lutz
 * Date: 4/1/11
 */
public class SocketFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SocketFactory.class);
    static final int MIN_CONNECT_TIMEOUT = 1;
    static final int MIN_PORT_NUMBER = 1;
    static final int MAX_PORT_NUMBER = 65535;
    static final int MILLI_SEC_PER_SECOND = 1000;

	public SocketFactory(String hostName, int portNumber, int connectTimeoutSecs) {

        if (hostName == null || hostName.length() < MIN_PORT_NUMBER) {
            throw new IllegalArgumentException(" host name cannot be empty or null");
        }
        if (portNumber < MIN_PORT_NUMBER || portNumber > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Port number out side legal range of 0 - 65535.  PORT:  " + portNumber);
        }
        if (connectTimeoutSecs < MIN_CONNECT_TIMEOUT) {
            throw new IllegalArgumentException("Illegal connect timeout ( 1- ).  CONNECT TIME: " + connectTimeoutSecs);
        }
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.connectTimeoutMilli = connectTimeoutSecs * MILLI_SEC_PER_SECOND;
    }

    public Socket createSocket()
            throws  SecurityException, IOException,
            IllegalArgumentException, IllegalBlockingModeException {
        Socket socket;
        InetSocketAddress inetSocketAddress = getAddress();

        socket = getNewSocket();

        bindSocket(socket);

        LOG.info("Attempting to open socket to:  " + getConfigMsg());
        connectSocket(socket, inetSocketAddress);
        LOG.info("Opened socket to:  " + hostName + ":" + portNumber);

        return socket;
    }

    protected void connectSocket(Socket socket, InetSocketAddress address)
            throws IllegalArgumentException, IllegalBlockingModeException, IOException {
        socket.connect(address, connectTimeoutMilli);
    }

    protected void bindSocket(Socket socket)
            throws IOException, IllegalArgumentException {
        socket.bind(null);
    }

    private Socket getNewSocket()
            throws IllegalArgumentException, SecurityException {
        return constructNewSocket();
    }

    private InetSocketAddress getAddress()
            throws IllegalArgumentException, SecurityException {
        return constructNewAddress();
    }

    protected Socket constructNewSocket() {
        return new Socket();
    }

    protected InetSocketAddress constructNewAddress() {
        return new InetSocketAddress(hostName, portNumber);
    }

    public String getConfigMsg() {
        return "HOSTNAME:PORT " + hostName + ":" + portNumber + " -->  Connect Timeout: " + connectTimeoutMilli + " (ms.)";
    }
    
    private String hostName;
    public String getHostName() {
		return hostName;
	}

    public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	private int portNumber;
	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	private int connectTimeoutMilli;

    public int getConnectTimeoutMilli() {
		return connectTimeoutMilli;
	}

	public void setConnectTimeoutMilli(int connectTimeoutMilli) {
		this.connectTimeoutMilli = connectTimeoutMilli;
	}


}
