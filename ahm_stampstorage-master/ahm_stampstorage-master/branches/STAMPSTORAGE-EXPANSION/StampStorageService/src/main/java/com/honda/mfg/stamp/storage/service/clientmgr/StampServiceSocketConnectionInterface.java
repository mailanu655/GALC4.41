// StampServiceSocketConnectionInterface.java

package com.honda.mfg.stamp.storage.service.clientmgr;

import com.honda.mfg.connection.processor.messages.ConnectionMessage;

public interface StampServiceSocketConnectionInterface extends SocketConnectionConstantsInterface {
	void processOutput(ConnectionMessage o) throws Exception;

	void closeSocket();

	void setDone(boolean done);

	boolean isDone();

	boolean isInitialized();

}
