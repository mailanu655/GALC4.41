package com.honda.mfg.device.mesmodule;

import com.honda.io.StreamPair;
import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class StreamMesProcessorPairTest {

    private static final boolean RUN_ONCE = true;

    @Test(timeout = 5000L)
    public void successfullyStartResponseProcessorOnNewThread() {
        String inputMsg = "hello";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamMesProcessorPair pair =
                new StreamMesProcessorPair(streamPair);
        // The new thread running response processor should initialize within 5 secs.
        while (pair.getInitializeRequiredTime() == 0L) {
        }
        assertTrue(pair.getInitializeRequiredTime() > 0);
    }

    @Test
    public void successfullyRunResponseProcessor() {
        String inputMsg = "{hello}";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamMesProcessorPair pair =
                new StreamMesProcessorPair(streamPair, RUN_ONCE);

        assertTrue(pair.getInitializeRequiredTime() == 0L);
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() > 0L);
        MessageRequestProcessor requestProcessor = pair.getRequestProcessor();
        assertNotNull(requestProcessor);
    }

    @Test
    public void throwsExceptionAttemptingToStartResponseProcessor() {
        String inputMsg = "hello";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenThrow(new NetworkCommunicationException("broken again..."))
                .thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamMesProcessorPair pair =
                new StreamMesProcessorPair(streamPair, RUN_ONCE);

        MessageRequestProcessor requestProcessor = pair.getRequestProcessor();

        assertTrue(pair.getInitializeRequiredTime() == 0L);
        // First run fails to initialize
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() < 0L);
        // Second run successfully initializes
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() > 0L);
    }
}
