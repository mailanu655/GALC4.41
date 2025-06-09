package com.honda.mfg.threads;

import com.honda.mfg.schedule.Scheduler;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class SynchronizedExampleTest {
    private final Logger LOG = LoggerFactory.getLogger(SynchronizedExampleTest.class);
    private final int LOOP_START = 1;
    private final int LOOP_SIZE = 10;
    private final int LOOP_SWITCH_COUNT = LOOP_SIZE / 2;
    private final long WAIT = 100L;

    @Test
    public void testSynchronized() throws InterruptedException {
        String expectedMsg = "Hello World!";
        SynchronizedExample me = new SynchronizedExample();
        AnnotationProcessor.process(me);

        assertEquals(false, me.inLoop);
        Scheduler s = new Scheduler(me, "my thread");

        for (int i = LOOP_START; i < LOOP_SIZE; i++) {
            if (i % LOOP_SWITCH_COUNT == 0) {
                EventBus.publish(expectedMsg);
                me.inLoop = false;
            }
            LOG.info("Value:  " + me.someValue + "   -->  inLoop? " + me.inLoop);
        }
        // Pause for max eventbus publish event.
        pause(200L);
        assertEquals(false, me.inLoop);
        String actualMsg = me.someValue;
        assertEquals(expectedMsg, actualMsg);
        AnnotationProcessor.unprocess(me);
    }

    private void pause(long timeout) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeout) {
        }
    }
}
