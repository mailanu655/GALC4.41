package com.honda.mfg.connection.processor;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.connection.MockMessageRequestProcessor;
import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.processor.ConnectionInitializer;
import com.honda.mfg.connection.processor.ConnectionProcessorPair;
import com.honda.mfg.connection.processor.messages.ConnectionInitialized;
import com.honda.mfg.connection.processor.messages.ConnectionUninitialized;
import com.honda.mfg.connection.processor.messages.PingMessage;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class ConnectionInitializerTest {

    long expectedFirst = 1L;
    long expectedSecond = 2L;
    long expectedThird = 3L;
    long expectedFourth = 4L;
    int count;
    int countUninitialized;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @After
    public void after() {
    }

    @EventSubscriber(eventClass = ConnectionInitialized.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchEvent(ConnectionInitialized deviceInitialized) {
        this.count++;
    }

    @EventSubscriber(eventClass = ConnectionUninitialized.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchEvent(ConnectionUninitialized deviceUninitialized) {
        this.countUninitialized++;
    }

    @Test(timeout = 5000L)
    public void successfullyInitializeDeviceInNewThread() {
        // Pre-conditions
        ConnectionProcessorPair pair = mock(ConnectionProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));
        request.addSendRequestBehavior(new NetworkCommunicationException("network breakdown...."));
        request.addSendRequestBehavior(new PingMessage());

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(expectedSecond)
                .thenReturn(expectedSecond)
                .thenReturn(expectedThird)
                .thenReturn(expectedFourth);
        ConnectionInitializer initializer = new ConnectionInitializer(pair, 1);
        while (!initializer.isInitialized()) {
        }
        initializer.shutdown();
    }

    //    @Test
    // TODO Ambica please fix this.  It does not run from mvn command prompt.
    public void successfullyReceiveAsyncInitializedMessage() {
        // Pre-conditions
        ConnectionProcessorPair pair = mock(ConnectionProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();
        request.addSendRequestBehavior(new PingMessage());

        ConnectionInitializer initializer = new ConnectionInitializer(pair);

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(1L);

        //Perform test
        int beforeCount = count;
        assertEquals(0, beforeCount);
        AnnotationProcessor.process(this);
        initializer.run();
        AnnotationProcessor.unprocess(this);
        int afterCount = count;
        int diffCount = afterCount - beforeCount;

        //Post-condition
        assertTrue(1 < diffCount);
    }

    @Test
    public void throwsExceptionAttemptingToStartResponseProcessorInsideProcessorPair() {
        // Pre-conditions
        ConnectionProcessorPair pair = mock(ConnectionProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(-1L)
                .thenReturn(0L)
                .thenReturn(1L);
        ConnectionInitializer initializer = new ConnectionInitializer(pair);

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(0)).getRequestProcessor();

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(3)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();
    }

    @Test
    public void successfullyInitializeDevice() {
        // Pre-conditions
        ConnectionProcessorPair pair = mock(ConnectionProcessorPair.class);
        MockMessageRequestProcessor request = new MockMessageRequestProcessor();

        request.addSendRequestBehavior(new CommunicationsException("communications breakdown...."));
        request.addSendRequestBehavior(new PingMessage());
        request.addSendRequestBehavior(new NetworkCommunicationException("network breakdown...."));

        when(pair.getRequestProcessor()).thenReturn(request);
        when(pair.getInitializeRequiredTime())
                .thenReturn(expectedFirst)
                .thenReturn(expectedSecond)
                .thenReturn(expectedSecond)
                .thenReturn(expectedThird)
                .thenReturn(expectedFourth);
        ConnectionInitializer initializer = new ConnectionInitializer(pair);

        // Perform test - Attempt to re-initialize but produces exception in sendRequest()
        initializer.run();
        // assertions - not initialized
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(1)).getInitializeRequiredTime();
        verify(pair, times(1)).getRequestProcessor();

        // Perform test - Attempt to re-initialize with success
        initializer.run();
        // assertions
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(2)).getInitializeRequiredTime();
        verify(pair, times(2)).getRequestProcessor();

        // Perform test
        initializer.run();
        // assertions
        assertEquals(true, initializer.isInitialized());
        verify(pair, times(3)).getInitializeRequiredTime();
        verify(pair, times(2)).getRequestProcessor();

        // Perform test
        initializer.run();

        // assertions
        assertEquals(false, initializer.isInitialized());
        verify(pair, times(4)).getInitializeRequiredTime();
        verify(pair, times(3)).getRequestProcessor();
    }
}
