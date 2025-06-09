package com.honda.mfg.device.rfid.etherip;

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
 * User: Jeffrey M Lutz
 * Date: 4/12/11
 */
public class StreamRfidProcessorPairTest {
    private static final boolean RUN_ONCE = true;

    @Test(timeout = 2000L)
    public void successfullyStartResponseProcessorOnNewThread() {
        String inputMsg = "hello";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamRfidProcessorPair pair =
                new StreamRfidProcessorPair(streamPair);
        // the new thread running response processor should initialize within 5 secs.
        while (pair.getInitializeRequiredTime() == 0L) {
        }
        assertTrue(pair.getInitializeRequiredTime() > 0);
    }

    @Test
    public void successfullyRunResponseProcessor() {
        String inputMsg = "hello";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamRfidProcessorPair pair =
                new StreamRfidProcessorPair(streamPair, RUN_ONCE);
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

        StreamRfidProcessorPair pair =
                new StreamRfidProcessorPair(streamPair, RUN_ONCE);

        assertTrue(pair.getInitializeRequiredTime() == 0L);
        // First run fails to initialize
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() < 0L);
        // Second run successfully initializes
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() > 0L);
    }

    @Test
    public void throwsExceptionAttemptingToGetResponseProcessor() {
        String inputMsg = "hello";
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamPair streamPair = mock(StreamPair.class);
        when(streamPair.in()).thenThrow(new RuntimeException("severely broken"))
                .thenReturn(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputMsg.getBytes()))));
        when(streamPair.out()).thenReturn(new BufferedWriter(new OutputStreamWriter(outputStream)));

        StreamRfidProcessorPair pair =
                new StreamRfidProcessorPair(streamPair, RUN_ONCE);

        assertTrue(pair.getInitializeRequiredTime() == 0L);
        // First run fails to initialize
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() < 0L);
        // Second run successfully initializes
        pair.run();
        assertTrue(pair.getInitializeRequiredTime() > 0L);
    }
}
