package com.honda.mfg.connection.watchdog;

public interface WatchdogAdapterInterface {

	public abstract void roleChange(boolean isPassive);

	public abstract void setPassivePing(boolean passivePing);

	public abstract void setOwnerId(String ownerId);

	public abstract void setPassiveWaitCount(int passiveWaitCount);

	public abstract int getPassiveWaitCount();

	public abstract void setCurrentMaxWaitCount(int currentMaxWaitCount);

	public abstract int getCurrentMaxWaitCount();

	public abstract void incrementWaitCount(int delta);

	public abstract void resetMaxWaitCount();

	public abstract boolean isAdaptiveWaitCount();

	public abstract void stopRunning();

}
