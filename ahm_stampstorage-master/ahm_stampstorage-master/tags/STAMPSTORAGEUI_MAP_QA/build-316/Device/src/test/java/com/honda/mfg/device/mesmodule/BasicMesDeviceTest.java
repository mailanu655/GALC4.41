package com.honda.mfg.device.mesmodule;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.MockMessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import com.honda.mfg.device.mesmodule.messages.MesRequest;
import com.honda.mfg.device.mesmodule.messages.PingMessage;
import com.honda.mfg.device.messages.MessageRequest;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class BasicMesDeviceTest {

    private static final int RESPONSE_TIMEOUT = 1;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    private MesInitializeState getState(boolean initialized) {
        MesInitializeState state = mock(MesInitializeState.class);
        when(state.isInitialized()).thenReturn(initialized);
        return state;
    }

    @Test
    public void successfullyReadFromMesDevice() {
        // Pre-conditions
        GeneralMessage expectedMsg = new GeneralMessage("{Some Data}");
        String expectedData = expectedMsg.getMessage();
        String input = "{Some Data}";

        GeneralMessage expectedResponse = new GeneralMessage(input);
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(true), RESPONSE_TIMEOUT);
        GeneralMessage actualMessage = mes.sendMessage(expectedResponse, RESPONSE_TIMEOUT);

        String actualData = actualMessage.getMessage();
        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        MesRequest actualRequest = (MesRequest) requestProcessor.getRequestsSent().get(0);
        assertEquals(MesRequest.class, actualRequest.getClass());
        assertEquals(expectedData, actualData);
    }

    @Test
    public void successfullyWriteToMesDevice() {
        // Pre-conditions
        String expectedData = "SomeData";

        MesMessage expectedResponse = new GeneralMessage("h");
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);

        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(true), RESPONSE_TIMEOUT);
        mes.sendMessage(new GeneralMessage(expectedData));

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        MessageRequest actualRequest = requestProcessor.getRequestsSent().get(0);
        assertEquals(MesRequest.class, actualRequest.getClass());
    }

    @Test
    public void successfullyPingMesDevice() {
        // Pre-conditions
        boolean expectedData = true;

        MesMessage expectedResponse = new PingMessage("", true);
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        requestProcessor.addSendRequestBehavior(expectedResponse);
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(true), RESPONSE_TIMEOUT);
        boolean actualData = mes.isHealthy();

        // Post-conditions
        assertEquals(1, requestProcessor.getRequestsSent().size());
        MesRequest actualRequest = (MesRequest) requestProcessor.getRequestsSent().get(0);
        assertEquals(MesRequest.class, actualRequest.getClass());
        assertEquals(expectedData, actualData);
    }

    @Test(expected = ResponseTimeoutException.class)
    public void throwsResponseTimeoutExceptionAttemptingReadFromMesDevice() {
        // Pre-conditions

        GeneralMessage expectedResponse = new GeneralMessage("hello");
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(true), RESPONSE_TIMEOUT);
        mes.sendMessage(expectedResponse, RESPONSE_TIMEOUT);

    }

    @Test
    public void throwsResponseTimeoutExceptionAttemptingToPingMesDevice() {
        // Pre-conditions
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(true), RESPONSE_TIMEOUT);

        // Post-conditions
        assertEquals(false, mes.isHealthy());
    }

    @Test(expected = CommunicationsException.class)
    public void throwsCommunicationsExceptionAttemptingToPingUninitializedMesDevice() {
        // Pre-conditions
        MesProcessorPair mockPair = mock(MesProcessorPair.class);
        MockMessageRequestProcessor requestProcessor = new MockMessageRequestProcessor();
        when(mockPair.getRequestProcessor()).thenReturn(requestProcessor);
        // Perform test
        BasicMesDevice mes = new BasicMesDevice(mockPair, getState(false), RESPONSE_TIMEOUT);
        mes.isHealthy();
    }
}
