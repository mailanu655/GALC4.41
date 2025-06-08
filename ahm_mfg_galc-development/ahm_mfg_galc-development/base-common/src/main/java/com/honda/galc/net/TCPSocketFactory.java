package com.honda.galc.net;

import java.net.*;
import java.io.*;

/**
 * This class offers a timeout feature on socket connections.
 * A maximum length of time allowed for a connection can be 
 * specified, along with a host and port.
 *
 * @author Subu Kathiresan
 * @date March 26, 2009
 */
public class TCPSocketFactory {

	public static int DEFAULT_CONNECTION_TIMEOUT = 4000;

	public static Socket getSocket(String hostName, int port) throws InterruptedIOException, IOException {
		return getSocket(hostName, port, DEFAULT_CONNECTION_TIMEOUT);
	}

	/**
	 * Attempts to connect to a service at the specified host
	 * and port, for a specified maximum amount of time.
	 */
	public static Socket getSocket(String hostName, int port, int connectionTimeOut) throws InterruptedIOException, IOException {
		if (hostName != null)
			hostName = hostName.trim();

		SocketThread st = new SocketThread(hostName, port );
		st.start();

		int timer = 0;
		Socket sock = null;

		while(true) {
			if (st.isConnected()) {
				sock = st.getSocket();
				break;
			}
			else {
				if (st.isError())
					throw (st.getException());
				try	{
					Thread.sleep (POLL_INTERVAL );
				}
				catch (InterruptedException ie) {}

				// Increment timer
				timer += POLL_INTERVAL;

				if (timer > connectionTimeOut)
					throw new InterruptedIOException("Could not connect to server " + hostName + " : Connection time out is " + connectionTimeOut + " milliseconds");
			}
		}
		return sock;
	}

	/**
	 * Creates a Socket connected to the given host and port.
	 * <p>
	 * @param address The address of the host to connect to.
	 * @param port The port to connect to.
	 * @return A Socket connected to the given host and port.
	 * @exception IOException If an I/O error occurs while creating the Socket.
	 */
	public static Socket getSocket(InetAddress address, int port) throws IOException {
		return new Socket(address, port);
	}

	/**
	 * Creates a Socket connected to the given host and port.
	 * <p>
	 * @param address The address of the host to connect to.
	 * @param port The port to connect to.
	 * @param connectionTimeout time (in milliseconds) to wait before time out
	 * @param readTimeout socket read timeout value (in milliseconds)
	 * @return A Socket connected to the given host and port.
	 * @exception IOException If an I/O error occurs while creating the Socket.
	 */
	public static Socket getSocket(String host, int port, int connectionTimeout, int readTimeout) throws UnknownHostException, IOException  {
		Socket socket = getSocket(host, port, connectionTimeout);
		socket.setSoTimeout(readTimeout);
		return socket;
	}

	/**
	 * Creates a ServerSocket bound to a specified port.  A port
	 * of 0 will create the ServerSocket on a system-determined free port.
	 * <p>
	 * @param port  The port on which to listen, or 0 to use any free port.
	 * @return A ServerSocket that will listen on a specified port.
	 * @exception IOException If an I/O error occurs while creating
	 *                        the ServerSocket.
	 */
	public static ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port);
	}

	/**
	 * Creates a ServerSocket bound to a specified port with a given
	 * maximum queue length for incoming connections.  A port of 0 will
	 * create the ServerSocket on a system-determined free port.
	 * <p>
	 * @param port  The port on which to listen, or 0 to use any free port.
	 * @param backlog  The maximum length of the queue for incoming connections.
	 * @return A ServerSocket that will listen on a specified port.
	 * @exception IOException If an I/O error occurs while creating
	 *                        the ServerSocket.
	 */
	public static ServerSocket createServerSocket(int port, int backlog) throws IOException {
		return new ServerSocket(port, backlog);
	}

	/**
	 * Creates a ServerSocket bound to a specified port on a given local
	 * address with a given maximum queue length for incoming connections.
	 * A port of 0 will
	 * create the ServerSocket on a system-determined free port.
	 * <p>
	 * @param port  The port on which to listen, or 0 to use any free port.
	 * @param backlog  The maximum length of the queue for incoming connections.
	 * @param bindAddr  The local address to which the ServerSocket should bind.
	 * @return A ServerSocket that will listen on a specified port.
	 * @exception IOException If an I/O error occurs while creating
	 *                        the ServerSocket.
	 */
	public static ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
		return new ServerSocket(port, backlog, bindAddr);
	}

	/**
	 * Inner class for establishing a socket thread
	 * within another thread, to prevent blocking. 
	 */
	static class SocketThread extends Thread {
		volatile private Socket _connection = null;
		private String _host = null;
		private int _port = 0;
		private IOException _exception = null;

		public SocketThread(String host, int port) {
			_host = host;
			_port = port;
		}

		public void run() {
			Socket sock = null;

			try {
				sock = new Socket(_host, _port);
			}
			catch (IOException ioe) {
				_exception = ioe;
				return;
			}
			_connection = sock;
		}

		public boolean isConnected() {
			if (_connection == null)
				return false;
			else
				return true;
		}

		public boolean isError() {
			if (_exception == null)
				return false;
			else
				return true;
		}

		public Socket getSocket() {
			return _connection;
		}

		public IOException getException() {
			return _exception;
		}
	}

	// Polling delay for socket checks (in milliseconds)
	private static final int POLL_INTERVAL = 100;
}

