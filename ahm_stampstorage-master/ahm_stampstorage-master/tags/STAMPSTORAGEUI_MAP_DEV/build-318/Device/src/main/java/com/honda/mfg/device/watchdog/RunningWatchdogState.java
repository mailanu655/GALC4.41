package com.honda.mfg.device.watchdog;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class RunningWatchdogState extends WatchdogState {
    private WatchdogState stoppedState;

    @Override
    public WatchdogState healthyEvent(WatchdogContext context) {
        return this;
    }

    @Override
    public WatchdogState unhealthyEvent(WatchdogContext context) {
        context.close();
        return getStoppedState();
    }

    private WatchdogState getStoppedState() {
        if (stoppedState == null) {
            stoppedState = new StoppedWatchdogState();
        }
        return stoppedState;
    }
}
