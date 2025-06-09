package com.honda.mfg.stamp.conveyor.messages;

/**
 * User: Jeffrey M Lutz Date: 6/19/11
 */
public class StaleDataMessage {
	private boolean stale;

	public StaleDataMessage(boolean stale) {
		this.stale = stale;
	}

	public boolean isStale() {
		return this.stale;
	}
}
