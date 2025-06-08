package com.honda.galc.net;

import java.io.IOException;

import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;
/**
*
* This class is for the Ubisense telegrams which has no terminating character at the end 
* of its packet. Each telegram is fixed length specified in the header to be read and
* used to determine how much data to parse.
*
* @author Bernard Leong
* @date Jun 21, 2017
*/
public class UbisenseTextSocketReceiver extends TextSocketReceiver {
	
	public UbisenseTextSocketReceiver(SocketClient socketClient, MessageHandler<String> messageHandler, char endOfMessage, Logger logger) {
		super(socketClient, messageHandler, endOfMessage, logger);
	}
	
	@Override
	public String readSocket(){
		StringBuffer strBuffer = new StringBuffer();
		try {
			while (socket.isConnected()) {
				int aChar = getSocketReader().read();
				strBuffer.append((char) aChar);
				if (isEndOfMessage(strBuffer))
					return strBuffer.toString();
			}
		} catch (IOException e) {
			throw new ServiceInvocationException("Unable to read message from Ubisense socket due to " + e.getMessage() );
		}
		return null;
	}

	protected boolean isEndOfMessage(StringBuffer telegram) {
		if (telegram.length() < 12)
			return false;
		int length = Integer.parseInt(telegram.substring(4, 12));
		return telegram.length() == length;
	}
}
