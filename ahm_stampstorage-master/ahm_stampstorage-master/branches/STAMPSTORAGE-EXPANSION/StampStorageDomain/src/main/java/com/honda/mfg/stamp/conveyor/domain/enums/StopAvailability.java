package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Is the Stop Available or Blocked? User: vcc30690 Date: 1/19/12 Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public enum StopAvailability {

	AVAILABLE(0), BLOCKED(1);

	private int type;

	StopAvailability(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}

	public static StopAvailability findByType(int type) {
		StopAvailability[] items = StopAvailability.values();
		for (int i = 0; i < items.length; i++) {
			StopAvailability s = items[i];
			if (type == s.type()) {
				return s;
			}
		}
		return AVAILABLE;
	}

}
