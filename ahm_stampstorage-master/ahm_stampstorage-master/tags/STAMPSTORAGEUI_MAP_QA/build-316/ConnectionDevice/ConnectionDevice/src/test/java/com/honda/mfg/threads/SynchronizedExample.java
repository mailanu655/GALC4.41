package com.honda.mfg.threads;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronization is important when reading a class level field in a while loop with no code inside the loop
 * <p/>
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 * Time: 10:31 AM
 */
public class SynchronizedExample implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(SynchronizedExample.class);

    volatile String someValue;
    volatile boolean inLoop;

    @EventSubscriber(eventClass = String.class)
    public void receiveEvent(String eventMsg) {
        LOG.info("Received msg:  " + eventMsg);
        someValue = eventMsg;
        LOG.info("Set someValue:  " + someValue);
    }

    @Override
    public void run() {
        inLoop = true;
        while (someValue == null) {
        }
        inLoop = false;
    }
}
