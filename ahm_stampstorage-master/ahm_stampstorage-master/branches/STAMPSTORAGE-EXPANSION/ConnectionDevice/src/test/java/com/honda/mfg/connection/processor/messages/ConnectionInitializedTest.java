package com.honda.mfg.connection.processor.messages;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;

/**
 * User: vcc30690 Date: 4/25/11
 */
public class ConnectionInitializedTest {

	ConnectionInitialized deviceInitialized;
	int count;

	@Test
	public void successfullyCreateMesDeviceInitializedMessage() {

		ConnectionInitialized deviceInitialized = new ConnectionInitialized();
		assertNotNull(deviceInitialized.getInitializedTime());
	}
}
