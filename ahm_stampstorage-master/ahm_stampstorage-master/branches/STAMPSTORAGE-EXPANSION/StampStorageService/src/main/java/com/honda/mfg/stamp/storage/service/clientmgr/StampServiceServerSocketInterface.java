package com.honda.mfg.stamp.storage.service.clientmgr;

import java.io.IOException;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

public interface StampServiceServerSocketInterface extends SocketConnectionConstantsInterface {

	void setDone(boolean done);

	boolean isDone();

	void closeSocket();

	int getNumConnections();

	void start() throws IOException;

	void init() throws Exception;

	void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrap);

}
