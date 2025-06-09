package com.honda.mfg.connection;

import com.honda.mfg.connection.MessageRequestProcessor;
import com.honda.mfg.connection.MessageRequestProcessorImpl;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * User: Mihail Chirita
 * Date: Sep 28, 2010
 * Time: 11:26:56 AM
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
