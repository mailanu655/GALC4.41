package com.honda.galc.client.device.etcher;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.etcher.LecLaserSocketDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.MessageHandler;
import com.honda.galc.net.SocketClient;
import com.honda.galc.net.TextSocketReceiver;

/**
 * @author Subu Kathiresan
 * @date May 24, 2017
 */
public class LecLaserSocketReceiver extends TextSocketReceiver {

	private boolean receive = true;
	LecLaserSocketDevice device = null;
	
	public LecLaserSocketReceiver(LecLaserSocketDevice device, MessageHandler<String> messageHandler, char endOfMessage, Logger logger) {
		this(new SocketClient(device.getSocket()), messageHandler, endOfMessage, logger);
		this.device = device;
	}

	public LecLaserSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler, char endOfMessage, Logger logger) {
		super(socketClient, messageHandler, endOfMessage, logger);
	}

	@Override
	public void run() {
		if(!messageHandler.isAlive()) messageHandler.start();
		while(receive && socket.isValid()) {
			String message = null;
			try {
				message = readSocket();
				if (message != null) {
					messageHandler.enqueue(message);
					log(message);
				}
			} catch (Throwable ex) {
				logger.error(ex, "Error receiving message on socket: " + StringUtils.trimToEmpty(ex.getMessage()));
				device.setConnected(false);
				device.finalizeSocket();
			}
		}
	}
	
	public void setReceive(boolean receive) {
		this.receive = receive;
	}
	
}
