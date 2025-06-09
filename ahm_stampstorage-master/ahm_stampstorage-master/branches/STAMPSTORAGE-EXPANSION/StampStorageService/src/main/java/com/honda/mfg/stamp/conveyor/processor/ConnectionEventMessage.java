package com.honda.mfg.stamp.conveyor.processor;

/**
 * @author VCC44349
 *
 */
public class ConnectionEventMessage {
	private boolean connected;

	public ConnectionEventMessage(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return this.connected;
	}
}
