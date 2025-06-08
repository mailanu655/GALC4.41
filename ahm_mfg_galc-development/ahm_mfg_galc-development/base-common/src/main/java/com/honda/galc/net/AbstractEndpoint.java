package com.honda.galc.net;

public abstract class AbstractEndpoint implements Endpoint{

	private static long DEFAULT_HEARTBEAT_INTERVAL = 30000; 
	private long heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
	private boolean isConnected = false;
	public boolean equals(Endpoint endpoint) {
		return this.name().equals(endpoint.name());
	}

	public long heartbeatInterval() {
		return heartbeatInterval;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean flag) {
		 this.isConnected = flag;
	}

}
