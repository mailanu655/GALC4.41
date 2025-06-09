package com.honda.mfg.connection.processor;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.processor.messages.GeneralMessage;

/**
 * User: Jeffrey M Lutz Date: 7/7/11
 */
public class MockConnectionDevice implements ConnectionDevice {
	private static final Logger LOG = LoggerFactory.getLogger(MockConnectionDevice.class);

	public MockConnectionDevice() {
	}

	@Override
	public void sendMessage(ConnectionMessage message) throws CommunicationsException, NetworkCommunicationException {
		LOG.debug("Mock sending:  " + message.getMessage());
	}

	@Override
	public GeneralMessage sendMessage(ConnectionMessage request, int timeoutSec) {
		LOG.trace("Sending MES MESSAGE request()");
		return null;
	}

	@Override
	public boolean isHealthy() {
		return true;
	}

	public Calendar getLatestPing() {
		return Calendar.getInstance();
	}
}
