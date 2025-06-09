package com.honda.mfg.connection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;

import com.honda.mfg.connection.processor.messages.ConnectionRequest;

/**
 * User: Mihail Chirita Date: Sep 28, 2010 Time: 11:26:56 AM
 */
public class MessageRequestProcessorTest {

	@Test
	public void flushesTheWriterWhenSendingTheRequest() throws IOException {
		BufferedWriter bufferedWriter = mock(BufferedWriter.class);
		ConnectionRequest readRequest = mock(ConnectionRequest.class);
		MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(bufferedWriter);
		requestProcessor.sendRequest(readRequest);
		verify(bufferedWriter).flush();
	}
}
