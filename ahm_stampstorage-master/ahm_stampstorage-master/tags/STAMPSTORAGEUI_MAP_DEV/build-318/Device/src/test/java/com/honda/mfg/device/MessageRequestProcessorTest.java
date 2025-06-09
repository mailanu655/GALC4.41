package com.honda.mfg.device;

import com.honda.mfg.device.plc.omron.messages.FinsMemoryReadRequest;
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
        FinsMemoryReadRequest readRequest = mock(FinsMemoryReadRequest.class);
        MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(bufferedWriter);
        requestProcessor.sendRequest(readRequest);
        verify(bufferedWriter).flush();
    }
}
