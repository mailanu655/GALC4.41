package com.honda.mfg.connection.processor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Before;
import org.junit.Test;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.connection.MockMessageRequestProcessor;
import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.ResponseTimeoutException;
import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.PingMessage;

/**
 * User: vcc30690 Date: 4/12/11
 */
public class BasicConnectionTest {

	private static final int RESPONSE_TIMEOUT = 1;

	@Before
	public void before() throws EventServiceExistsException {
		EventBusConfig.setSingleThreadMode();
	}

	private ConnectionInitializeState getState(boolean initialized) {
		ConnectionInitializeState state = mock(ConnectionInitializeState.class);
		when(state.isInitialized()).thenReturn(initialized);
		return state;
	}

	@Test
	public void successfullyReadFromMesDevice() {
		// Pre-conditions
		GeneralMessage expectedMsg = new GeneralMessage("{Some Data}");
		String expectedData = expectedMsg.getMessage();
		String input = "{Some Data}";

		GeneralMessage expectedResponse = new GeneralMessage(input);
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		requestProcessor.addSendRequestBehavior(expectedResponse);
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(true), RESPONSE_TIMEOUT);
		ConnectionMessage actualMessage = mes.sendMessage(expectedResponse, RESPONSE_TIMEOUT);

		String actualData = actualMessage.getMessage();
		// Post-conditions
		assertEquals(1, requestProcessor.getRequestsSent().size());
		ConnectionRequest actualRequest = (ConnectionRequest) requestProcessor.getRequestsSent().get(0);
		assertEquals(ConnectionRequest.class, actualRequest.getClass());
		assertEquals(expectedData, actualData);
	}

	@Test
	public void successfullyWriteToMesDevice() {
		// Pre-conditions
		String expectedData = "SomeData";

		ConnectionMessage expectedResponse = new GeneralMessage("h");
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		requestProcessor.addSendRequestBehavior(expectedResponse);
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);

		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(true), RESPONSE_TIMEOUT);
		mes.sendMessage(new GeneralMessage(expectedData));

		// Post-conditions
		assertEquals(1, requestProcessor.getRequestsSent().size());
		MessageRequest actualRequest = requestProcessor.getRequestsSent().get(0);
		assertEquals(ConnectionRequest.class, actualRequest.getClass());
	}

	@Test
	public void successfullyPingMesDevice() {
		// Pre-conditions
		boolean expectedData = true;

		ConnectionMessage expectedResponse = new PingMessage("", true);
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		requestProcessor.addSendRequestBehavior(expectedResponse);
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(true), RESPONSE_TIMEOUT);
		boolean actualData = mes.isHealthy();

		// Post-conditions
		assertEquals(1, requestProcessor.getRequestsSent().size());
		ConnectionRequest actualRequest = (ConnectionRequest) requestProcessor.getRequestsSent().get(0);
		assertEquals(ConnectionRequest.class, actualRequest.getClass());
		assertEquals(expectedData, actualData);
	}

	@Test(expected = ResponseTimeoutException.class)
	public void throwsResponseTimeoutExceptionAttemptingReadFromMesDevice() {
		// Pre-conditions

		GeneralMessage expectedResponse = new GeneralMessage("hello");
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(true), RESPONSE_TIMEOUT);
		mes.sendMessage(expectedResponse, RESPONSE_TIMEOUT);

	}

	@Test
	public void throwsResponseTimeoutExceptionAttemptingToPingMesDevice() {
		// Pre-conditions
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(true), RESPONSE_TIMEOUT);

		// Post-conditions
		assertEquals(false, mes.isHealthy());
	}

	@Test(expected = CommunicationsException.class)
	public void throwsCommunicationsExceptionAttemptingToPingUninitializedMesDevice() {
		// Pre-conditions
		ConnectionProcessorPair mockPair = mock(ConnectionProcessorPair.class);
		MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
		when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
		// Perform test
		ConnectionDevice mes = new BasicConnection(mockPair, getState(false), RESPONSE_TIMEOUT);
		mes.isHealthy();
	}
}
