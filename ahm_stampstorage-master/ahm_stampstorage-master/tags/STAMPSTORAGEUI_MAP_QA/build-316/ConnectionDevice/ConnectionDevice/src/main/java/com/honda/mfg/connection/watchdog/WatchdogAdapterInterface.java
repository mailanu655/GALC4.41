package com.honda.mfg.connection.watchdog;

public interface WatchdogAdapterInterface {

    void roleChange(boolean isPassive);

    void setPassivePing(boolean passivePing);

    void setOwnerId(String ownerId);

    void setPassiveWaitCount(int passiveWaitCount);

    int getPassiveWaitCount();

    void setCurrentMaxWaitCount(int currentMaxWaitCount);

    int getCurrentMaxWaitCount();

    void incrementWaitCount(int delta);

    void resetMaxWaitCount();

    boolean isAdaptiveWaitCount();

    void stopRunning();

}
