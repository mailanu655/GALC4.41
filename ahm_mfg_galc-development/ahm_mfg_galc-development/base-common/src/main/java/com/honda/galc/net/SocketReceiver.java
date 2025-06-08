package com.honda.galc.net;

import java.io.IOException;

import com.honda.galc.common.logging.Logger;

public abstract class SocketReceiver<T> implements Runnable{
	
	protected SocketClient socket;
	protected MessageHandler<T> messageHandler;
	protected Logger logger;
	
	public SocketReceiver(SocketClient socketClient, MessageHandler<T> messageHandler) {
		this.socket = socketClient;
		this.messageHandler = messageHandler;
	}
	
	public SocketReceiver(SocketClient socketClient, MessageHandler<T> messageHandler, Logger logger) {
		this.socket = socketClient;
		this.messageHandler = messageHandler;
		this.logger = logger;
	}
	
	public void run() {
		if(!messageHandler.isAlive()) messageHandler.start();
		while(socket.isValid()) {
			T message = null;
			try {
				message = readSocket();
				log(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			messageHandler.enqueue(message);
		}
		
	}

	protected void log(T message) {
		if(logger != null)
			logger.info("<<" + message + "<<");
		else
			Logger.getLogger().info("<<" + message + "<<");
	}
	
	public abstract T readSocket() throws IOException;

}
