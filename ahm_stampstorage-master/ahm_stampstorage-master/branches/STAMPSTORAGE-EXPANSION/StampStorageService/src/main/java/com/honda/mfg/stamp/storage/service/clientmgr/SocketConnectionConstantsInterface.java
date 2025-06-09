package com.honda.mfg.stamp.storage.service.clientmgr;

public interface SocketConnectionConstantsInterface {

	int DEFAULT_MAX_CONNECTIONS = 1000;
	int DEFAULT_MAX_NUM_TIMEOUTS = 2;

	/**
	 * These are values from server socket configuration/properties
	 */
	String MAX_NUM_CONNECTIONS = "MaxNumConnections";
	String MAX_NUM_TIMEOUTS = "MaxNumTimeouts";
	String SERVER_PORT_PROPERTY_KEY = "ServerPort";
	String SERVER_SOCKET_TIMEOUT_PROPERTY_KEY = "ServerSocketTimeout";
	String SOCKET_TIMEOUT_PROPERTY_KEY = "SocketTimeout";
	String PRE_WRITE_DELAY = "PreWriteDelay";
	String PRE_READ_DELAY = "PreReadDelay";
	String MAX_NUM_CLIENT_SOCKET_TIMEOUT_ON_A_READ = "maxNumClientSocketTimesOutOnARead";
	int DEFAULT_PORT = 44449;

}
