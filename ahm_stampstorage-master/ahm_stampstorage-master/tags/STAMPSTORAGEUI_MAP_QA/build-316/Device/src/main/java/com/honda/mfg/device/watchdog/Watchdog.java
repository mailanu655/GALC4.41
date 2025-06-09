package com.honda.mfg.device.watchdog;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class Watchdog {
    private WatchdogContext watchdogContext;

    private WatchdogState currentState;

    public Watchdog(WatchdogContext watchdogContext) {
        this.watchdogContext = watchdogContext;
        this.currentState = WatchdogState.INITIAL_STATE;
    }

    public void healthyEvent() {
        currentState = currentState.healthyEvent(watchdogContext);
    }

    public void unhealthyEvent() {
        currentState = currentState.unhealthyEvent(watchdogContext);
    }
}
