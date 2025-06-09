package com.honda.mfg.device.rfid.etherip;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.rfid.etherip.messages.RfidClearPendingResponsesResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidResponse;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Jeffrey M Lutz
 * Date: 4/12/11
 */
public class RfidResponseProcessorTest {

    private int publishedEvents;

    @Before
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
        publishedEvents = 0;
        AnnotationProcessor.process(this);
    }

    @After
    public void after() {
        AnnotationProcessor.unprocess(this);
    }

    @Test(timeout = 2000L)
    public void successfullyStartResponseProcessorInAutoMode() {
        // Pre-conditions
        RfidResponseReader reader = mock(RfidResponseReader.class);
        RfidResponseBuilder builder = mock(RfidResponseBuilder.class);

        when(reader.getResponse()).thenReturn("HELLO");
        when(builder.buildRfidResponse(anyString()))
                .thenReturn(new RfidClearPendingResponsesResponse(1, 2, new Date()))
                .thenThrow(new UnknownResponseException())
                .thenThrow(new CommunicationsException("I am broken.."));

        // Perform test
        RfidResponseProcessor processor = new RfidResponseProcessor(reader, builder);

        // Assertions
        // before starting, it is assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
    }

    @Test
    public void successfullyStartResponseProcessorAndThenStopProcessing() {
        // Pre-conditions
        RfidResponseReader reader = mock(RfidResponseReader.class);
        RfidResponseBuilder builder = mock(RfidResponseBuilder.class);

        when(reader.getResponse()).thenReturn("HELLO");
        when(builder.buildRfidResponse(anyString()))
                .thenReturn(new RfidClearPendingResponsesResponse(1, 2, new Date()))
                .thenThrow(new UnknownResponseException())
                .thenThrow(new CommunicationsException("I am broken.."));

        // Perform test
        RfidResponseProcessor processor = new RfidResponseProcessor(reader, builder, true);

        // Assertions
        // before starting, it is assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(false, processor.isRunning());
        assertEquals(1, publishedEvents);
    }

    @Test
    public void throwsNetworkCommunicationExceptionAttemptingToProcess() {
        // Pre-conditions
        RfidResponseReader reader = mock(RfidResponseReader.class);
        RfidResponseBuilder builder = mock(RfidResponseBuilder.class);

        when(reader.getResponse()).thenReturn("HELLO");
        when(builder.buildRfidResponse(anyString()))
                .thenReturn(new RfidClearPendingResponsesResponse(1, 2, new Date()))
                .thenThrow(new UnknownResponseException())
                .thenThrow(new NetworkCommunicationException("I am broken.."));

        // Perform test
        RfidResponseProcessor processor = new RfidResponseProcessor(reader, builder, true);

        // Assertions
        // before starting, it is assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(false, processor.isRunning());
        assertEquals(1, publishedEvents);
    }


    @Test
    public void throwsResponseTimeoutExceptionAttemptingToProcess() {
        // Pre-conditions
        RfidResponseReader reader = mock(RfidResponseReader.class);
        RfidResponseBuilder builder = mock(RfidResponseBuilder.class);

        when(reader.getResponse())
                .thenThrow(new ResponseTimeoutException())
                .thenReturn("Hello");
        when(builder.buildRfidResponse(anyString()))
                .thenReturn(new RfidClearPendingResponsesResponse(1, 2, new Date()))
                .thenThrow(new NetworkCommunicationException("I am broken.."));

        // Perform test
        RfidResponseProcessor processor = new RfidResponseProcessor(reader, builder, true);

        // Assertions
        // before starting, it is assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(false, processor.isRunning());
        assertEquals(1, publishedEvents);
    }

    @EventSubscriber(eventClass = RfidResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void incrementCounter(RfidResponse rfidResponse) {
        publishedEvents++;
    }
}
