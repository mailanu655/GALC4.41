package com.honda.mfg.device.watchdog;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class StoppedWatchdogState extends WatchdogState {
    private WatchdogState runningState;

    @Override
    public WatchdogState healthyEvent(WatchdogContext context) {
        return getRunningState();
    }

    @Override
    public WatchdogState unhealthyEvent(WatchdogContext context) {
        context.close();
        return this;
    }

    private WatchdogState getRunningState() {
        if (runningState == null) {
            runningState = new RunningWatchdogState();
        }
        return runningState;
    }
}
