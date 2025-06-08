package com.honda.galc.client.device.lotcontrol;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.MessageHandler;
import com.honda.galc.net.SocketClient;
import com.honda.galc.net.TextSocketReceiver;

/**
 * 
 * <h3>TorqueSocketReceiver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TorqueSocketReceiver description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Jan 17, 2011
 *
 */

public class TorqueSocketReceiver extends TextSocketReceiver {

	private boolean receive = true;
	private TorqueSocketDevice device = null;
	
	public TorqueSocketReceiver(TorqueSocketDevice device, MessageHandler<String> messageHandler, char endOfMessage, Logger logger) {
		this(new SocketClient(device.getSocket()), messageHandler, endOfMessage, logger);
		this.device = device;
	}

	public TorqueSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler, char endOfMessage, Logger logger) {
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
				socket.close();		// ok to close the receiver
				device.finalizeSocket();
			}
		}
	}
	
	public void setReceive(boolean receive) {
		this.receive = receive;
	}
	
}
