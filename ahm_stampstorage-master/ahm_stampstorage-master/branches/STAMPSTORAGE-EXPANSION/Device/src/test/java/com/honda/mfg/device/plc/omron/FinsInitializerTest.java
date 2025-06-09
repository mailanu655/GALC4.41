package com.honda.mfg.device.plc.omron;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.MockMessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.plc.omron.messages.FinsInitializeResponse;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: Jeffrey M Lutz
 * Date: 4/8/11
 */
public class FinsInitializerTest {
    private int expectedSrcNode = 111;
    private int expectedDestNode = 222;
    long expectedFirst = 1L;
    long expectedSecond = 2L;
    long expectedThird = 3L;
    long expectedFourth = 4L;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @After
    public void after() {
    }

    @Test(timeout = 5000L)
    public void successfullyInitializeDeviceInNewThread() {
        // Pre-conditions
        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));
        request.addSendRequestBehavior(new NetworkCommunicationException("network breakdown...."));
        request.addSendRequestBehavior(new FinsInitializeResponse(expectedDestNode, expectedSrcNode));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(expectedSecond)
                .thenReturn(expectedSecond)
                .thenReturn(expectedThird)
                .thenReturn(expectedFourth);
        FinsInitializer initializer = new FinsInitializer(pair, 1);
        while (!initializer.isInitialized()) {
        }
    }

    @Test
    public void throwsExceptionAttemptingToStartResponseProcessorInsideProcessorPair() {
        // Pre-conditions
        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(0L)
                .thenReturn(-1L)
                .thenReturn(1L);
        FinsInitializer initializer = new FinsInitializer(pair);
        int expectedInvalidNode;

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        expectedInvalidNode = FinsInitializer.INVALID_NODE_NUMBER;
        assertEquals(expectedInvalidNode, initializer.getSourceNode());
        assertEquals(expectedInvalidNode, initializer.getDestinationNode());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        expectedInvalidNode = FinsInitializer.INVALID_NODE_NUMBER;
        assertEquals(expectedInvalidNode, initializer.getSourceNode());
        assertEquals(expectedInvalidNode, initializer.getDestinationNode());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        expectedInvalidNode = FinsInitializer.INVALID_NODE_NUMBER;
        assertEquals(expectedInvalidNode, initializer.getSourceNode());
        assertEquals(expectedInvalidNode, initializer.getDestinationNode());
        verify(pair, times(3)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();
    }

    @Test
    public void successfullyInitializeDevice() {
        // Pre-conditions
        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));
        request.addSendRequestBehavior(new FinsInitializeResponse(expectedDestNode, expectedSrcNode));
        request.addSendRequestBehavior(new FinsInitializeResponse(expectedDestNode, expectedSrcNode));
        request.addSendRequestBehavior(new NetworkCommunicationException("network breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(expectedSecond)
                .thenReturn(expectedSecond)
                .thenReturn(expectedThird)
                .thenReturn(expectedFourth);
        FinsInitializer initializer = new FinsInitializer(pair);

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        int expectedInvalidNode = FinsInitializer.INVALID_NODE_NUMBER;
        assertEquals(expectedInvalidNode, initializer.getSourceNode());
        assertEquals(expectedInvalidNode, initializer.getDestinationNode());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();

        // Perform test - Attempt to re-initialize with success
        initializer.run();
        // assertions
        assertEquals(true, initializer.isInitialized());
        assertEquals(expectedSrcNode, initializer.getSourceNode());
        assertEquals(expectedDestNode, initializer.getDestinationNode());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(2)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions
        assertEquals(true, initializer.isInitialized());
        assertEquals(expectedSrcNode, initializer.getSourceNode());
        assertEquals(expectedDestNode, initializer.getDestinationNode());
        verify(pair, times(3)).getInitializeRequiredTime();
        verify(pair, times(2)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions
        assertEquals(true, initializer.isInitialized());
        assertEquals(expectedSrcNode, initializer.getSourceNode());
        assertEquals(expectedDestNode, initializer.getDestinationNode());
        verify(pair, times(4)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions
        assertEquals(false, initializer.isInitialized());
        assertEquals(expectedInvalidNode, initializer.getSourceNode());
        assertEquals(expectedInvalidNode, initializer.getDestinationNode());
        verify(pair, times(5)).getInitializeRequiredTime();
        verify(pair, times(4)).getRequestProcessor();
    }
}