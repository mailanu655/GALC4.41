package com.honda.galc.net;

import java.io.BufferedReader;
import java.io.IOException;

import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>TextSocketReceiver Class description</h3>
 * <p> TextSocketReceiver description </p>
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
 * @author Jeffray Huang<br>
 * Apr 12, 2010
 *
 */
public class TextSocketReceiver extends SocketReceiver<String>{
	
	public static final char NULL = 0;
	public static final char END_OF_LINE = '\n';
	private char endOfMessage = Character.MAX_VALUE;
	
	public TextSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler) {
		super(socketClient, messageHandler);
	}
	
	public TextSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler, char endOfMessage) {
		super(socketClient, messageHandler);
		this.endOfMessage = endOfMessage;
	}
	
	public TextSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler, char endOfMessage,
			Logger logger) {
		super(socketClient, messageHandler, logger);
		this.endOfMessage = endOfMessage;
	}
		
	@Override
	public String readSocket(){
		StringBuffer strBuffer = new StringBuffer();
		try {
			while(socket.isConnected()) {
				int aChar = getSocketReader().read();
				if (!isEndOfMessage(aChar)) 
					strBuffer.append((char) aChar);
				else 
					return strBuffer.toString();
			}
		} catch (IOException e) {
			throw new ServiceInvocationException("Unable to read message from socket due to " + e.getMessage() );
		}
		return null;
		
	}

	protected BufferedReader getSocketReader() {
		return socket.getBufferedReader();
	}
	
	protected boolean isEndOfMessage(int intchar) {
		//By default check both NULL and END OF LINE, otherwise check end of message
		if(endOfMessage == Character.MAX_VALUE)
			return intchar == NULL || intchar == END_OF_LINE;
		else
			return (endOfMessage == intchar);
	}

}
