package com.honda.eventbus;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 4/4/11
 */
public class EventBusConfigTest {
    private Thread listenerThread;

    @Before
    public void before() throws EventServiceExistsException {
        listenerThread = null;
    }

    @Test
    public void testSingleThreadMode() throws EventServiceExistsException {

        EventBusConfig.isSingleThreadMode();

        EventBusConfig.setSingleThreadMode();

        EventBusConfig.setSingleThreadMode();

        AnnotationProcessor.process(this);

        assertEquals(true, EventBusConfig.isSingleThreadMode());
        assertEquals(false, EventBusConfig.isSwingMode());

        EventBus.publish("Hello World.");
        while (listenerThread == null)
            ;
        Thread myThread = Thread.currentThread();
        assertEquals(myThread, listenerThread);

        AnnotationProcessor.unprocess(this);

        try {
            EventBusConfig.setSwingMode();
            fail("I should not be able to switch EventBus modes!!!");
        } catch (EventServiceExistsException e) {
            // success!!!!  This exception is expected and part of the test
        }
    }

    @EventSubscriber(eventClass = String.class)
    public void threadListener(String something) {
        listenerThread = Thread.currentThread();
    }
}
