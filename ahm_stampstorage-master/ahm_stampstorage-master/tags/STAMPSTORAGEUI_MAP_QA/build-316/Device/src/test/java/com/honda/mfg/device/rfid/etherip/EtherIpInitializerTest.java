package com.honda.mfg.device.rfid.etherip;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.MockMessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.rfid.etherip.messages.RfidClearPendingResponsesResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidGetControllerConfigurationResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidGetControllerInfoResponse;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: Jeffrey M Lutz
 * Date: 4/11/11
 */
public class EtherIpInitializerTest {
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

    @Test
    public void successfullyInitializesInNewThread() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

//        request.addSendRequestBehavior();

        EtherIpInitializer initializer =
                new EtherIpInitializer(pair, 3);
        // Perform test

    }

    @Test
    public void successfullyInitializeDeviceAndRemainsInitialized() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new RfidClearPendingResponsesResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerConfigurationResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerInfoResponse(1, 2, new Date()));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();
    }

    @Test
    public void throwsExceptionAttemptingToStartResponseProcessorInsideProcessorPair() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(-1L)
                .thenReturn(0L);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();
    }

    @Test
    public void throwsExceptionAttemptingToSendFirstRequestWhileAttemptingToInitializeDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();
    }

    @Test
    public void throwsNetworkExceptionAttemptingToSendFirstRequestWhileAttemptingToInitializeDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new NetworkCommunicationException("network breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();
    }

    @Test
    public void throwsExceptionAttemptingToSendSecondRequestWhileAttemptingToInitializeDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new RfidClearPendingResponsesResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(2)).getRequestProcessor();
    }

    @Test
    public void throwsExceptionAttemptingToSendThirdRequestWhileAttemptingToInitializeDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new RfidClearPendingResponsesResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerConfigurationResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());
        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();
    }

    @Test
    public void successfullyInitializeThenFailsDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new RfidClearPendingResponsesResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerConfigurationResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerInfoResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(-1L)
                .thenReturn(expectedSecond);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Pre-condition check
        assertEquals(false, initializer.isInitialized());

        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();
    }

    @Test
    public void successfullyInitializeAfterInitialFailureDevice() {
        // Pre-conditions
        RfidProcessorPair pair = mock(RfidProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));
        request.addSendRequestBehavior(new RfidClearPendingResponsesResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerConfigurationResponse(1, 2, new Date()));
        request.addSendRequestBehavior(new RfidGetControllerInfoResponse(1, 2, new Date()));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(expectedSecond)
                .thenReturn(expectedSecond);
        EtherIpInitializer initializer = new EtherIpInitializer(pair);

        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions - not initialized
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(4)).getRequestProcessor();
    }
}
