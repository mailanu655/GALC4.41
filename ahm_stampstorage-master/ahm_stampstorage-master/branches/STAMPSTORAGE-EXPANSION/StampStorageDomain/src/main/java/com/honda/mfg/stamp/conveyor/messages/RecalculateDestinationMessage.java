package com.honda.mfg.stamp.conveyor.messages;

/**
 * User: VCC30690 Date: 9/16/11
 */
public class RecalculateDestinationMessage {

	private boolean recalc;

	public RecalculateDestinationMessage(boolean recalc) {
		this.recalc = recalc;
	}

	public boolean isRecalc() {
		return this.recalc;
	}
}
