package com.honda.galc.jca;

import static java.util.logging.Level.*;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.resource.spi.work.Work;

public class SocketProcessor implements Work {
	private final Logger logger = Logger.getLogger(SocketProcessor.class.getSimpleName());

	private SocketMessage socketMessage;
	private final SocketMessageEndpoint messageEndpoint;

	public SocketProcessor(SocketMessage socketMessage, SocketMessageEndpoint messageEndpoint) {
		this.socketMessage = socketMessage;
		this.messageEndpoint = messageEndpoint;
	}

	public SocketMessageEndpoint getMessageEndpoint() {
		return messageEndpoint;
	}

	public void release() {
		closeSocket();
	}

	public void run() {
		try {
			logger.fine("Executing the onMessage(socketMessage) method");
			messageEndpoint.onMessage(socketMessage);
		} catch (Exception e) {
			logger.log( WARNING,  "Exception on execution of MDB, processing has probably failed. Socket will be closed.", e);
		} finally {
			closeSocket();
		}
	}

	private void closeSocket() {
		try {
			Socket socket = socketMessage.getRawSocket();
			if ( !socket.isClosed()){
				logger.info("Socket message close socket is called ");
				socket.close();
			}
		} catch (final IOException e) {
			logger.log( SEVERE, "Exception on close of socket, processing may have failed", e);
		}
	}

}
