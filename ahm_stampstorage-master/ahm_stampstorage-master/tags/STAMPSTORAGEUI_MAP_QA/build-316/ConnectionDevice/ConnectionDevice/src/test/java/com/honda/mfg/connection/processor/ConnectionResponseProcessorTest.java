package com.honda.mfg.connection.processor;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.exceptions.ResponseTimeoutException;
import com.honda.mfg.connection.processor.ConnectionResponseProcessor;
import com.honda.mfg.connection.processor.ConnectionResponseReader;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;

import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 3/24/11
 */
public class ConnectionResponseProcessorTest {

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

    @Test
    public void successfullyStartResponseProcessorInAutoMode() {
        // Pre-conditions
        ConnectionResponseReader reader = mock(ConnectionResponseReader.class);

        ConnectionResponseProcessor processor = new ConnectionResponseProcessor(reader);
        when(reader.getResponse())
                .thenReturn("HELLO")
                .thenThrow(new CommunicationsException("I am broken..."));

        // Perform test


        // Post-condition checks/assertions
        // before starting, its assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(false, processor.isRunning());
        assertEquals(1, publishedEvents);
    }

    @Test
    public void successfullyStartResponseProcessorAndThenStopProcessing() {
        // Pre-conditions
        ConnectionResponseReader reader = mock(ConnectionResponseReader.class);

        when(reader.getResponse())
                .thenReturn("HELLO")
                .thenThrow(new CommunicationsException("I am broken..."));

        // Perform test
        ConnectionResponseProcessor processor = new ConnectionResponseProcessor(reader, true);

        // Post-condition checks/assertions
        // before starting, its assumed to be connected.
        assertEquals(true, processor.isRunning());
        assertEquals(0, publishedEvents);
        processor.run();
        assertEquals(true, processor.isRunning());
        assertEquals(1, publishedEvents);
        processor.run();
        assertEquals(false, processor.isRunning());
        assertEquals(1, publishedEvents);
    }

    @Test
    public void throwsExceptionAttemptingToGetResponse() {
        ConnectionResponseReader responseReader = mock(ConnectionResponseReader.class);

        NetworkCommunicationException networkCommunicationException = new NetworkCommunicationException();
        CommunicationsException communicationException = new CommunicationsException();
        ResponseTimeoutException responseTimeoutException = new ResponseTimeoutException();

        when(responseReader.getResponse())
                .thenThrow(responseTimeoutException)
                .thenReturn("{hello}")
                .thenThrow(networkCommunicationException)
                .thenThrow(communicationException);

        // Perform test
        ConnectionResponseProcessor processor =
                new ConnectionResponseProcessor(responseReader, false, true);
        // This run will cycle through the first three responseReader values.
        processor.run();
        // Need to reset to connected so we can run the last responseReader
        processor.setConnected(true);
        processor.run();

        // Post condition check
        int i = 0;
        ResponseTimeoutException actualResponseTimeoutException = (ResponseTimeoutException) processor.getSequence().get(i++);
        assertEquals(responseTimeoutException, actualResponseTimeoutException);

        GeneralMessage r = (GeneralMessage) processor.getSequence().get(i++);
        assertEquals((new GeneralMessage("{hello}")).getMessage(), r.getMessage());

        NetworkCommunicationException actualException = (NetworkCommunicationException) processor.getSequence().get(i++);
        assertEquals(networkCommunicationException, actualException);

        CommunicationsException actualCommunicationsException = (CommunicationsException) processor.getSequence().get(i++);
        assertEquals(communicationException, actualCommunicationsException);

    }

    @EventSubscriber(eventClass = GeneralMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void incrementCounter(ConnectionMessage message) {
        publishedEvents++;
    }

}
