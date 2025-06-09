package com.honda.mfg.device.rfid.etherip;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.MockMessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.rfid.RfidMemory;
import com.honda.mfg.device.rfid.etherip.exceptions.TagNotFoundException;
import com.honda.mfg.device.rfid.etherip.messages.RfidGetControllerInfoResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidReadTagIdResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidReadTagResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidWriteTagResponse;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Jeffrey M Lutz
 * Date: Jan 24, 2011
 */
public class BasicEtherIpRfidDeviceTest {
    private static final int RESPONSE_TIMEOUT = 1;
    private static final int INSTANCE_COUNTER = 2;
    private static final int NODE_ID = 3;
    private static final int RFID_RETRY_COUNT = 3;

    private Date now;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
        now = new Date();
    }

    @Test
    public void successfullyWriteTag() {
        // Pre-conditions
        String expectedData = "Hello World!";
        RfidWriteTagResponse expectedResponse = new RfidWriteTagResponse(INSTANCE_COUNTER, NODE_ID, now);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        rfid.writeTag(new RfidMemory(1, 22), expectedData);

        // assertions
    }

    @Test
    public void successfullyReadTag() {
        // Pre-conditions
        String expectedData = "Hello World!";
        RfidReadTagResponse expectedResponse = new RfidReadTagResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readTag(new RfidMemory(1, 22));

        // assertions
        assertEquals(expectedData, actualData);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionAttemptingToReadTagFromUninitializedDevice() {
        // Pre-conditions
        String expectedData = "Hello World!";
        RfidReadTagResponse expectedResponse = new RfidReadTagResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(false), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readTag(new RfidMemory(1, 22));

        // assertions
        assertEquals(expectedData, actualData);
    }

    @Test(expected = TagNotFoundException.class)
    public void throwsTagNotFoundExceptionAttemptingToReadTagFromUninitializedDevice() {
        // Pre-conditions
        String expectedData = null;
        RfidReadTagResponse expectedResponse = new RfidReadTagResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readTag(new RfidMemory(1, 22));

        // assertions
        assertEquals(expectedData, actualData);
    }

    @Test
    public void successfullyGetControllerInfo() {
        // Pre-conditions
        RfidGetControllerInfoResponse expectedResponse = new RfidGetControllerInfoResponse(INSTANCE_COUNTER, NODE_ID, now);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readControllerInfo();

        // assertions
        assertNotNull(actualData);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionAttemptingToGetControllerInfoToUninitializedDevice() {
        // Pre-conditions
        RfidGetControllerInfoResponse expectedResponse = new RfidGetControllerInfoResponse(INSTANCE_COUNTER, NODE_ID, now);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(false), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readControllerInfo();

        // assertions
        assertNotNull(actualData);
    }

    @Test
    public void successfullyReadTagId() {
        // Pre-conditions
        String expectedData = "SomeData";
        RfidReadTagIdResponse expectedResponse = new RfidReadTagIdResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        String actualData = rfid.readTagId();

        // assertions
        assertEquals(expectedData, actualData);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionAttemptingToReadTagIdDueToUninitializedDevice() {
        // Pre-conditions
        String expectedData = "SomeData";
        RfidReadTagIdResponse expectedResponse = new RfidReadTagIdResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(false), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        rfid.readTagId();

        // assertions
    }

    @Test(expected = TagNotFoundException.class)
    public void throwsTagNotFoundExceptionAttemptingToReadTagId() {
        // Pre-conditions
        String expectedData = null;
        RfidReadTagIdResponse expectedResponse = new RfidReadTagIdResponse(INSTANCE_COUNTER, NODE_ID, now, expectedData);

        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(pair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicEtherIpRfidDevice rfid = new BasicEtherIpRfidDevice(pair, getState(true), RESPONSE_TIMEOUT, RFID_RETRY_COUNT);
        rfid.readTagId();

        // assertions
    }


    @Test(timeout = 5000L)
    public void validateReadTagIdIsSuccessfulWithMaxValue() {
//        String expectedTagId = "FFFFFFFFFFFFFFFF";
//
//        RfidProcessorPair processorPair = mock(RfidProcessorPair.class);
//
//        RfidReadTagIdResponse response = new RfidReadTagIdResponse(0, 1, new Date(), expectedTagId);
//
//        MessageRequestProcessorImpl requestProcessor = new MockRequestProcessor(RfidReadTagIdRequest.class, response);
//
//        RfidResponseProcessor p = new MockResponseProcessor(null, null);
//
//        when(processorPair.getRequestProcessor()).thenReturn(requestProcessor);
//        when(processorPair.getResponseProcessor()).thenReturn(p);
//
//        RfidDevice device = new BasicEtherIpRfidDevice(processorPair);
//
//        String actualTagId = device.readTagId();
//        Assert.assertEquals(expectedTagId, actualTagId);
    }

    @Test(timeout = 5000L)
    public void validateReadTagIdIsSuccessfulWithMinValue() {
//        String expectedTagId = "0000000000000000";
//
//        RfidProcessorPair processorPair = mock(RfidProcessorPair.class);
//
//        RfidReadTagIdResponse response = new RfidReadTagIdResponse(0, 1, new Date(), expectedTagId);
//
//        MessageRequestProcessorImpl requestProcessor = new MockRequestProcessor(RfidReadTagIdRequest.class, response);
//
//        RfidResponseProcessor p = new MockResponseProcessor(null, null);
//
//        when(processorPair.getRequestProcessor()).thenReturn(requestProcessor);
//        when(processorPair.getResponseProcessor()).thenReturn(p);
//
//        RfidDevice device = new BasicEtherIpRfidDevice(processorPair);
//
//        String actualTagId = device.readTagId();
//        Assert.assertEquals(expectedTagId, actualTagId);
    }

    @Test(timeout = 5000L)
    public void validateReadTagIsSuccessful() {
//        String expectedTag = "MyFirstTag";
//
//        RfidProcessorPair processorPair = mock(RfidProcessorPair.class);
//
//        RfidReadTagResponse response = new RfidReadTagResponse(0, 1, new Date(), expectedTag);
//
//        MessageRequestProcessorImpl requestProcessor = new MockRequestProcessor(RfidReadTagRequest.class, response);
//
//        RfidResponseProcessor responseProcessor = new MockResponseProcessor(null, null);
//
//
//        when(processorPair.getRequestProcessor()).thenReturn(requestProcessor);
//        when(processorPair.getResponseProcessor()).thenReturn(responseProcessor);
//
//        RfidDevice device = new BasicEtherIpRfidDevice(processorPair);
//        String actualTag = device.readTag(new RfidMemory(0, 0));
//        Assert.assertEquals(expectedTag, actualTag);
    }


    @Test(timeout = 5000L)
    public void validateWriteTagIsSuccessful() {
    }

    private EtherIpInitializeState getState(boolean initialized) {
        EtherIpInitializeState state = mock(EtherIpInitializeState.class);
        when(state.isInitialized()).thenReturn(initialized);

        return state;
    }
}

