package com.honda.mfg.connection.processor;

import java.util.Calendar;

import com.honda.mfg.connection.processor.messages.ConnectionMessage;

/**
 * User: vcc30690 Date: 4/11/11
 */
public interface ConnectionDevice {

	public void sendMessage(ConnectionMessage request);

	public ConnectionMessage sendMessage(ConnectionMessage request, int timeoutSec);

	public boolean isHealthy();

	public abstract Calendar getLatestPing();
}
