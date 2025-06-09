package com.honda.mfg.device.plc.omron;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.plc.omron.messages.FinsClockReadResponse;
import com.honda.mfg.device.plc.omron.messages.FinsMemoryReadResponse;
import com.honda.mfg.device.plc.omron.messages.FinsResponse;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * User: Jeffrey M Lutz
 * Date: Sep 27, 2010
 */
public class FinsResponseProcessorTest {

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

    @Test(timeout = 5000)
    public void successfullyStartResponseProcessorInAutoMode() {
        // Pre-conditions
        FinsResponseReader reader = mock(FinsResponseReader.class);
        FinsResponseBuilder builder = mock(FinsResponseBuilder.class);

        when(reader.getResponse()).thenReturn("HELLO");
        when(builder.buildFinsResponse(anyString()))
                .thenReturn(new FinsClockReadResponse(1, 2, 3, "CLOCK"));

        // Perform test
        FinsResponseProcessor processor = new FinsResponseProcessor(reader, builder);

        // Post-condition checks/assertions
        // before starting, its assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
    }

    @Test(timeout = 5000)
    public void successfullyStartResponseProcessorAndThenStopProcessing() {
        // Pre-conditions
        FinsResponseReader reader = mock(FinsResponseReader.class);
        FinsResponseBuilder builder = mock(FinsResponseBuilder.class);

        when(reader.getResponse()).thenReturn("HELLO");
        when(builder.buildFinsResponse(anyString()))
                .thenReturn(new FinsClockReadResponse(1, 2, 3, "CLOCK"))
                .thenThrow(new UnknownResponseException())
                .thenThrow(new CommunicationsException("I am broken..."));

        // Perform test
        FinsResponseProcessor processor = new FinsResponseProcessor(reader, builder, true);

        // Post-condition checks/assertions
        // before starting, its assumed to be connected.
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

    @Test(timeout = 5000)
    public void throwsExceptionAttemptingToGetResponse() {
        FinsResponseReader responseReader = mock(FinsResponseReader.class);
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());

        FinsMemoryReadResponse finsMemoryReadResponse = new FinsMemoryReadResponse(100, 101, 102, "test");
        NetworkCommunicationException networkCommunicationException = new NetworkCommunicationException();
        CommunicationsException communicationException = new CommunicationsException();
        ResponseTimeoutException responseTimeoutException = new ResponseTimeoutException();

        when(responseReader.getResponse())
                .thenThrow(responseTimeoutException)
                .thenReturn(finsMemoryReadResponse.getFinsResponse())
                .thenThrow(networkCommunicationException)
                .thenThrow(communicationException);

        // Perform test
        FinsResponseProcessor processor =
                new FinsResponseProcessor(responseReader, responseBuilder, false, true);
        // This run will cycle through the first three responseReader values.
        processor.run();
        // Need to reset to connected so we can run the last responseReader
        processor.setConnected(true);
        processor.run();

        // Post condition check
        int i = 0;
        ResponseTimeoutException actualResponseTimeoutException = (ResponseTimeoutException) processor.getSequence().get(i++);
        assertEquals(responseTimeoutException, actualResponseTimeoutException);

        FinsMemoryReadResponse r = (FinsMemoryReadResponse) processor.getSequence().get(i++);
        assertEquals(finsMemoryReadResponse.getFinsResponse(), r.getFinsResponse());

        NetworkCommunicationException actualException = (NetworkCommunicationException) processor.getSequence().get(i++);
        assertEquals(networkCommunicationException, actualException);

        CommunicationsException actualCommunicationsException = (CommunicationsException) processor.getSequence().get(i++);
        assertEquals(communicationException, actualCommunicationsException);

    }

    @EventSubscriber(eventClass = FinsResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void incrementCounter(FinsResponse finsResponse) {
        publishedEvents++;
    }
}
