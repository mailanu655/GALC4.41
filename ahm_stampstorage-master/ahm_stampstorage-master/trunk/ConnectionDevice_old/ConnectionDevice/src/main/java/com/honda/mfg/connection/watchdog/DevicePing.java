package com.honda.mfg.connection.watchdog;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public interface DevicePing {

    boolean ping();

    boolean passivePing(int passiveWaitCount, int pollIntervalSec);

    float getWaitCount(int pollIntervalSec);
}
