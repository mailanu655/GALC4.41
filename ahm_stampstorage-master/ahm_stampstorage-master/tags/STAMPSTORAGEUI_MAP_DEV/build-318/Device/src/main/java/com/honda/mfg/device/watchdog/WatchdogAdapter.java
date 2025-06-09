package com.honda.mfg.device.watchdog;

import com.honda.mfg.schedule.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class WatchdogAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(WatchdogAdapter.class);

    private Watchdog watchdog;
    private DevicePing devicePing;
    private Scheduler scheduler;
    private volatile boolean firstPass;

    WatchdogAdapter(Watchdog watchdog, DevicePing devicePing) {
        this.watchdog = watchdog;
        this.devicePing = devicePing;
    }

    public WatchdogAdapter(Watchdog watchdog, DevicePing devicePing, int pollIntervalSecs) {
        this(watchdog, devicePing);
        this.firstPass = true;
        scheduler = new Scheduler(new Runner(), pollIntervalSecs, "WatchdogAdapter");
    }

    void stopRunning() {
        scheduler.shutdown();
    }

    void run() {
        new Runner().run();
    }

    private class Runner implements Runnable {
        public void run() {
            if (firstPass) {
                firstPass = false;
                return;
            }
            long delta = System.currentTimeMillis();
            boolean healthy = devicePing.ping();
            delta = System.currentTimeMillis() - delta;
            if (healthy) {
                LOG.info("Ping:  healthy    --> " + devicePing.getClass().getSimpleName()
                        + " --> Response time: " + delta + " (ms)");
                watchdog.healthyEvent();
            } else {
                LOG.info("Ping:  unhealthy  --> " + devicePing.getClass().getSimpleName()
                        + " --> Response time: " + delta + " (ms)");
                watchdog.unhealthyEvent();
            }
        }
    }
}
