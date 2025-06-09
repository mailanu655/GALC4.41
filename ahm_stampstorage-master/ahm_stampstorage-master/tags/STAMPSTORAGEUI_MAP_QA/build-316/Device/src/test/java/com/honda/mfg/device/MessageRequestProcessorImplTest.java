package com.honda.mfg.device;

import com.honda.io.MockStreamPair;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.plc.omron.FinsMemory;
import com.honda.mfg.device.plc.omron.messages.FinsInitializeResponse;
import com.honda.mfg.device.plc.omron.messages.FinsMemoryWriteRequest;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * User: Jeffrey M Lutz
 * Date: 4/7/11
 */
public class MessageRequestProcessorImplTest {

    @Test
    public void successfullySendRequest() throws IOException {
        // Pre-condition
        FinsMemoryWriteRequest msgRequest = new FinsMemoryWriteRequest(new FinsMemory(FinsMemory.BANK.DM, 0, 10), 1, 0, 1, "hello");
        MockStreamPair mockPair;
        mockPair = new MockStreamPair(new FinsInitializeResponse(1, 2));
        MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(mockPair.out());

        String expectedMsg = msgRequest.getMessageRequest().toString();

        // Perform test
        requestProcessor.sendRequest(msgRequest);
        String actualMsg = mockPair.getBytesSentThruOutputStream();

        // Post condition checks
        assertEquals("Error attempting to sendRequest() via messageRequestProcessor.",
                expectedMsg, actualMsg);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionWhenAttemptingToSendRequest() throws IOException {
        // Pre-condition
        FinsMemoryWriteRequest msgRequest = mock(FinsMemoryWriteRequest.class);
        BufferedWriter writer = mock(BufferedWriter.class);
        doThrow(new IOException("unable to send")).when(writer).write(anyString());

        // Perform test
        MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(writer);
        requestProcessor.sendRequest(msgRequest);
    }
}
