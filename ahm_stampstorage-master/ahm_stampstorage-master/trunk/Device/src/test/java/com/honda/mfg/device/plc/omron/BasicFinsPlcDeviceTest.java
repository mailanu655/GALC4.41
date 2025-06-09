package com.honda.mfg.device.plc.omron;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.MockMessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.messages.MessageBase;
import com.honda.mfg.device.messages.MessageRequest;
import com.honda.mfg.device.plc.omron.messages.*;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class BasicFinsPlcDeviceTest extends MessageBase {

    private static final int RESPONSE_TIMEOUT = 1;

    private int expectedSrcNode = 111;
    private int expectedDestNode = 222;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @Test
    public void successfullyRecoverAfterReadMemoryThrowsException() {
        // Pre-conditions
        String expectedData = "SomeData";
        FinsMemoryReadResponse expectedResponse = new FinsMemoryReadResponse(expectedDestNode, expectedSrcNode, 1, expectedData);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(new NetworkCommunicationException("something"));
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = null;
        try {
            actualData = plc.readMemory(getMemory());
            fail();
        } catch (NetworkCommunicationException e) {
            // expected to be here
        }

        // Post-conditions
        assertNull(actualData);

        actualData = plc.readMemory(getMemory());
        assertEquals(expectedData, actualData);
    }

    @Test
    public void successfullyReadMemoryFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = "SomeData";
        FinsMemoryReadResponse expectedResponse = new FinsMemoryReadResponse(expectedDestNode, expectedSrcNode, 1, expectedData);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = plc.readMemory(getMemory());

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        FinsMemoryReadRequest actualRequest = (FinsMemoryReadRequest) requestProcessor.getRequestsSent().get(0);
        assertEquals(expectedData, actualData);
    }

    @Test
    public void successfullyWriteMemoryFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = "SomeData";
        FinsMemoryWriteResponse expectedResponse = new FinsMemoryWriteResponse(expectedDestNode, expectedSrcNode, 1);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        plc.writeMemory(getMemory(), expectedData);

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        FinsMemoryWriteRequest actualRequest = (FinsMemoryWriteRequest) requestProcessor.getRequestsSent().get(0);
        String actualRequestString = actualRequest.getMessageRequest();
        assertTrue(actualRequestString.contains(expectedData));
    }

    @Test
    public void successfullyReadClockFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = getClockValueAsString();
        FinsClockReadResponse expectedResponse = new FinsClockReadResponse(expectedDestNode, expectedSrcNode, 1, getClockValueAsBinary());

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = plc.readClock();

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        FinsClockReadRequest actualRequest = (FinsClockReadRequest) requestProcessor.getRequestsSent().get(0);
        assertEquals(expectedData, actualData);
    }

    @Test(expected = ResponseTimeoutException.class)
    public void throwsResponseTimeoutExceptionAttemptingReadMemoryFromFinsPlcDevice() {
        String expectedData = null;
        FinsMemoryReadResponse expectedResponse = new FinsMemoryReadResponse(expectedDestNode, expectedSrcNode, 1, expectedData);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = plc.readMemory(getMemory());
    }

    @Test(expected = ResponseTimeoutException.class)
    public void throwsResponseTimeoutExceptionAttemptingWriteMemoryToFinsPlcDevice() {
        // Pre-conditions
        String expectedData = "";

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(new ResponseTimeoutException("Did not receive anything..."));
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        plc.writeMemory(getMemory(), expectedData);

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        FinsMemoryWriteRequest actualRequest = (FinsMemoryWriteRequest) requestProcessor.getRequestsSent().get(0);
        String actualRequestString = actualRequest.getMessageRequest();
        assertTrue(actualRequestString.contains(expectedData));
    }

    @Test(expected = ResponseTimeoutException.class)
    public void throwsResponseTimeoutExceptionAttemptingReadClockFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = getClockValueAsString();

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(new ResponseTimeoutException("Did not receive anything..."));
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = plc.readClock();
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionAttemptingToReadMemoryFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = "SomeData";
        FinsMemoryReadResponse expectedResponse = new FinsMemoryReadResponse(expectedDestNode, expectedSrcNode, 1, expectedData);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(false), RESPONSE_TIMEOUT);
        String actualData = plc.readMemory(getMemory());
    }

    @Test(expected = ResponseTimeoutException.class)
    public void throwsResponseTimeoutExceptionAttemptingToReadClockFromFinsPlcDevice() {
        // Pre-conditions
        String expectedData = null;
        FinsClockReadResponse expectedResponse = new FinsClockReadResponse(expectedDestNode, expectedSrcNode, 1, expectedData);

        FinsProcessorPair pair = mock(FinsProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicFinsPlcDevice plc = new BasicFinsPlcDevice(pair, getState(true), RESPONSE_TIMEOUT);
        String actualData = plc.readClock();
    }

    private String getClockValueAsString() {
        return "11-01-02 01:23:45_12";
    }

    private String getClockValueAsBinary() {
        StringBuilder sb = new StringBuilder();
        sb.append(asChars(17, 1));
        sb.append(asChars(1, 1));
        sb.append(asChars(2, 1));
        sb.append(asChars(1, 1));
        sb.append(asChars(35, 1));
        sb.append(asChars(69, 1));
        sb.append(asChars(18, 1));
        return sb.toString();
    }

    private FinsInitializeState getState(boolean initialized) {
        FinsInitializeState state = mock(FinsInitializeState.class);
        when(state.isInitialized()).thenReturn(initialized);
        when(state.getSourceNode()).thenReturn(expectedSrcNode);
        when(state.getDestinationNode()).thenReturn(expectedDestNode);

        return state;
    }

    private FinsMemory getMemory() {
        return new FinsMemory(FinsMemory.BANK.DM, 1, 200);
    }
}
