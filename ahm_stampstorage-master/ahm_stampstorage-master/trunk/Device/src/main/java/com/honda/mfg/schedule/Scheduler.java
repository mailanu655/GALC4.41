package com.honda.mfg.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 5, 2010
 */
public class Scheduler {
    private ScheduledExecutorService s;
    private long delayIntervalSec;

    public Scheduler(Runnable runnable, long samplingIntervalSec, String threadName) {
        this(runnable, samplingIntervalSec, threadName, false);
    }

    public Scheduler(Runnable runnable, long delayIntervalSec, long samplingIntervalSec, String threadName){
        this.delayIntervalSec = delayIntervalSec;
        ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, false);
        this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
        Runnable runMe = new MyRunnable(runnable, samplingIntervalSec);
        new Scheduler(runMe, "private party");
    }

    public Scheduler(Runnable runnable, long samplingIntervalSec, String threadName, boolean daemon) {
        ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
        this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
        this.s.scheduleAtFixedRate(runnable, 0, samplingIntervalSec, SECONDS);
    }

    public Scheduler(Runnable runnable, String threadName) {
        boolean daemon = false;
        ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
        this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
        this.s.execute(runnable);
    }

    public Scheduler() {
        String threadName = "Unknown";
        Boolean daemon = false;
        ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
        this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    class MyRunnable implements Runnable {
        Runnable runnable;
        long samplingIntervalSec;

        public MyRunnable(Runnable runnable, long samplingIntervalSec) {
            this.runnable = runnable;
            this.samplingIntervalSec = samplingIntervalSec;
        }

        @Override
        public void run() {
            long delta = System.currentTimeMillis();
            while((System.currentTimeMillis()-delta)< (delayIntervalSec * 1000L)){
            }
            s.scheduleAtFixedRate(runnable, 0, samplingIntervalSec, SECONDS);
        }
    }

    public void runOnce(Runnable runnable) {
        this.s.execute(runnable);
    }

    public void shutdown() {
        this.s.shutdown();
    }

    boolean isShutdown() {
        return s.isShutdown();
    }
}
