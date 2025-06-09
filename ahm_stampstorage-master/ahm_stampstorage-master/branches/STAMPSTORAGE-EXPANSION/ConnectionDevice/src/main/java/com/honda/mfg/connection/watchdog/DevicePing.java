package com.honda.mfg.connection.watchdog;

/**
 * User: Jeffrey M Lutz Date: 4/2/11
 */
public interface DevicePing {

	public boolean ping();

	public abstract boolean passivePing(int passiveWaitCount, int pollIntervalSec);

	public abstract float getWaitCount(int pollIntervalSec);
}
