package com.honda.mfg.connection.processor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Test;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.exceptions.ResponseTimeoutException;
import com.honda.mfg.connection.watchdog.DevicePing;

/**
 * User: vcc30690 Date: 4/11/11
 */
public class ConnectionPingTest {
	public void before() throws EventServiceExistsException {
		EventBusConfig.setSingleThreadMode();
	}

	@Test
	public void successfullyPerformsValidPing() {
		// Pre-condition setup
		ConnectionDevice mes = mock(ConnectionDevice.class);
		when(mes.isHealthy()).thenReturn(true);

		// Perform test
		DevicePing ping = new ConnectionPing(mes);
		boolean actual = ping.ping();

		// Assert post condition
		assertEquals("isHealthy device unsuccessfully", true, actual);
	}

	@Test
	public void failsPerformingPingDueToDeviceError() {
		// Pre-condition setup
		ConnectionDevice mes = mock(ConnectionDevice.class);
		doThrow(new CommunicationsException("Comm error")).when(mes).isHealthy();
		ConnectionDevice mes2 = mock(ConnectionDevice.class);
		doThrow(new NetworkCommunicationException("Comm error")).when(mes2).isHealthy();
		ConnectionDevice mes3 = mock(ConnectionDevice.class);
		when(mes3.isHealthy()).thenThrow(new ResponseTimeoutException());
		// Perform test
		DevicePing ping = new ConnectionPing(mes);
		boolean actual = ping.ping();

		DevicePing ping2 = new ConnectionPing(mes2);
		boolean actual2 = ping2.ping();

		DevicePing ping3 = new ConnectionPing(mes3);
		boolean actual3 = ping3.ping();

		// Assert post condition
		assertEquals("isHealthy device successfully", false, actual);
		assertEquals("ping2 device successfully", false, actual2);
		assertEquals("isHealthy device successfully", false, actual3);
	}

}
