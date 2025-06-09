package com.honda.mfg.schedule;

import java.util.concurrent.ThreadFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 5, 2010
 */
public class ThreadFactoryImpl implements ThreadFactory {
    private String threadName;
    private Boolean daemon;

    public ThreadFactoryImpl(String threadName) {
        this(threadName, false);
    }

    public ThreadFactoryImpl(String threadName, Boolean daemon) {
        this.threadName = threadName;
        this.daemon = daemon;
    }

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(daemon);
        return thread;
    }
}
