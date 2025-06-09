package com.honda.mfg.connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import com.honda.io.MockStreamPair;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.GeneralMessage;

/**
 * User: Jeffrey M Lutz Date: 4/7/11
 */
public class MessageRequestProcessorImplTest {

	@Test
	public void successfullySendRequest() throws IOException {
		// Pre-condition
		ConnectionRequest msgRequest = new ConnectionRequest("any message");
		MockStreamPair mockPair;
		mockPair = new MockStreamPair(new GeneralMessage("any message"));
		MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(mockPair.out());

		String expectedMsg = msgRequest.getMessageRequest().toString();

		// Perform test
		requestProcessor.sendRequest(msgRequest);
		String actualMsg = mockPair.getBytesSentThruOutputStream();

		// Post condition checks
		assertEquals("Error attempting to sendRequest() via messageRequestProcessor.", expectedMsg, actualMsg);
	}

	// @Test(expected = CommunicationsException.class)
	public void throwsExceptionWhenAttemptingToSendRequest() throws IOException {
		// Pre-condition
		ConnectionRequest msgRequest = mock(ConnectionRequest.class);
		BufferedWriter writer = mock(BufferedWriter.class);
		doThrow(new IOException("unable to send")).when(writer).write(anyString());

		// Perform test
		MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(writer);
		requestProcessor.sendRequest(msgRequest);
	}
}
