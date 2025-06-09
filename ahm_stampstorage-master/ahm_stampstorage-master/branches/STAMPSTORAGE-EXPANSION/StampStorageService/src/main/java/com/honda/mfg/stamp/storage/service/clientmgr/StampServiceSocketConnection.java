// StampServiceSocketConnection.java

package com.honda.mfg.stamp.storage.service.clientmgr;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.PingMessage;
import com.honda.mfg.stamp.storage.service.utils.ExceptionHandler;
import com.honda.mfg.stamp.storage.service.utils.MessageSinkInterface;
import com.honda.mfg.stamp.storage.service.utils.MessageTransport;

/**
 * This class manages a client socket connection in a separate thread.
 * Reading and writing the socket streams is done in
 * the {@link com.honda.mfg.stamp.storage.service.utils.MessageTransport} class
 * @author VCC44349
 * @see StampServiceServerSocket
 *
 */
/**
 * @author VCC44349
 *
 */
public class StampServiceSocketConnection implements StampServiceSocketConnectionInterface, Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(StampServiceSocketConnection.class);

	private Socket clientSocket = null;
	private Properties props = null;
	private int socketTimeOut = 60000;
	private int maxNumSocketRetries = 2;// client socket times out on a read.
	private MessageTransport ot = null;
	private ExceptionHandler exceptionHandler = null;
	private MessageSinkInterface messageSink = null;
	private boolean pingReceivedFlag;
	private ExecutorService socketConnectionThread_ = null;
	/**
	 * boolean to control termination of run() loop call {@link #setDone(boolean)}
	 * to end processing
	 */
	private volatile boolean done = false;

	/**
	 * Manages socket connection on a separate thread; calls
	 * {@link MessageTransport} for reading and writing from/to data streams
	 * 
	 * @param clientSocket
	 * @param props
	 */
	public StampServiceSocketConnection(Socket clientSocket, Properties props) {
		try {
			this.props = new Properties(props);
			this.clientSocket = clientSocket;
			socketTimeOut = Integer
					.valueOf(this.props.getProperty(SOCKET_TIMEOUT_PROPERTY_KEY, String.valueOf(socketTimeOut)))
					.intValue();
			LOG.info("StampServiceSocketConnection.Init(): clientSocketTime out is: " + socketTimeOut);
			maxNumSocketRetries = Integer.valueOf(this.props.getProperty(MAX_NUM_CLIENT_SOCKET_TIMEOUT_ON_A_READ,
					String.valueOf(maxNumSocketRetries))).intValue();

			this.clientSocket.setTcpNoDelay(true);
			this.clientSocket.setSoLinger(true, 10);
			this.clientSocket.setSoTimeout(socketTimeOut);
			ot = new MessageTransport(this.clientSocket.getInputStream(), this.clientSocket.getOutputStream(), this);

			socketConnectionThread_ = Executors.newSingleThreadExecutor();
			setInitialized(true);
			// socketConnectionThread_.setDaemon(true);

		} catch (Exception e) {
			LOG.error("StampServiceSocketConnection.StampServiceSocketConnection():" + e, 30);
			e.printStackTrace();
		}
	}

	public void start() {
		socketConnectionThread_.execute(this);
	}

	public void run() {
		// Service the client inside a thread

		try {
			// run loop moved to serviceClient for error handling
			serviceClient();
		} catch (IOException ioEx) {
			LOG.error("StampServiceSocketConnection.run():" + ioEx, 30);
			ioEx.printStackTrace();
		} catch (Exception e) {
			LOG.error("StampServiceSocketConnection.run():" + e, 30);
			e.printStackTrace();
		}
	}

	public void processOutput(ConnectionMessage msg) throws Exception {
		try {
			MessageRequest req = new ConnectionRequest(msg);
			ot.writeObject(req);
		} catch (Exception e) {
			clientSocket.close();
			LOG.error("StampServiceSocketConnection.processOutput():" + e, 30);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * this method called from {@link #run()} and initiates processing of data on
	 * the input stream. Tries nto reconnect a specified number of times before
	 * terminating thread
	 * {@see com.honda.mfg.stamp.storage.service.utils.MessageTransport}
	 * 
	 * @throws Exception
	 */
	public void serviceClient() throws Exception {
		int numRetries = 0;
		// boolean ok = true;

		/**
		 * added a conditional while loop so that upon recieving an exception we set a
		 * flag and send a ping and wait for a response from client. If client does not
		 * respond within the so-timeout then close the socket.
		 */
		while (!done) {
			LOG.info("(1):StampServiceSocketConnection.Service Client(): Entered loop. ");
			try {
				ot.process();
				setDone(true);
			} catch (InterruptedIOException ioEx) // SocketTimeoutException
			{
				/**
				 * added logic to implement ping
				 */
				numRetries++;
				LOG.error("StampServiceSocketConnection.ServiceClient():retry count" + numRetries);
				if (pingReceivedFlag) {
					// reset flags since this is the first time out
					numRetries = 1;
					LOG.error("StampServiceSocketConnection.ServiceClient():reset timeout to " + numRetries);
					// reset flags since this is the first time out
					pingReceivedFlag = false;
				}

				LOG.error("StampServiceSocketConnection.ServiceClient():MaxNumClientSocketTimesOut: "
						+ maxNumSocketRetries);// first time read times out
				if ((numRetries < maxNumSocketRetries))// first time read times out
				{
					LOG.error("StampServiceSocketConnection.serviceClient():InterruptedIOException:" + numRetries
							+ " Timeout" + ioEx.getMessage());
					done = false;
					LOG.error("StampServiceSocketConnection.serviceClient():InterruptedIOException: Send heart beat."
							+ ioEx.getMessage());
					processOutput(new PingMessage());
				} else if (numRetries == maxNumSocketRetries)// second time read times out
				{
					LOG.error("StampServiceSocketConnection.ServiceClient():InterruptedIOException:" + numRetries
							+ " timeout:" + ioEx.getMessage());
					LOG.error(
							"StampServiceSocketConnection.ServiceClient():InterruptedIOException: Close Client Socket:"
									+ ioEx.getMessage());
					done = true;
				}
			} catch (Exception e) {
				LOG.error("StampServiceSocketConnection.serviceClient():" + e);
				e.printStackTrace();
				done = true;
			}
			LOG.info("StampServiceSocketConnection.: end of loop");

		} // end of while

		LOG.info("(1):StampServiceSocketConnection.Service Client(): out of run loop. ");
		LOG.info("Closing client socket connection...");
		closeSocket();
	}// end of ServiceClient

	public void closeSocket() {
		try {
			if (exceptionHandler != null) {
				exceptionHandler.handleException(null);
			}
			if (ot != null) {
				ot.setDone(true);
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (Exception e) {
			LOG.error("Exception closing socket. " + e);
		}
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * sets message sink in
	 * {@link com.honda.mfg.stamp.storage.service.utils.MessageTransport}
	 * 
	 * @param messageSink
	 */
	public void setObjectSink(MessageSinkInterface messageSink) {
		this.messageSink = messageSink;
		ot.setMessageSink(this.messageSink);
	}

	public void setPingReceivedFlag() {
		pingReceivedFlag = true;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void setDone(boolean done) {
		this.done = done;
	}

	private boolean bInitialized = false;

	@Override
	public boolean isInitialized() {
		return bInitialized;
	}

	protected void setInitialized(boolean bInitialized) {
		this.bInitialized = bInitialized;
	}

}// end of class StampServiceSocketConnection
