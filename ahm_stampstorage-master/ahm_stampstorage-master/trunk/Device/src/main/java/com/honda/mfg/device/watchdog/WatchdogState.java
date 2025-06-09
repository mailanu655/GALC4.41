package com.honda.mfg.device.watchdog;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public abstract class WatchdogState {

    public static final WatchdogState INITIAL_STATE =
            new StoppedWatchdogState();

    public abstract WatchdogState healthyEvent(WatchdogContext context);

    public abstract WatchdogState unhealthyEvent(WatchdogContext context);
}
