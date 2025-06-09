package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Severity 0-None 4-Maximum
 *
 */

public enum SEVERITY {

	NONE(0), ONE(1), TWO(2), THREE(3), FOUR(4);

	private int type;

	SEVERITY(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}

	public static SEVERITY findByType(int type) {
		SEVERITY[] items = SEVERITY.values();
		for (int i = 0; i < items.length; i++) {
			SEVERITY s = items[i];
			if (type == s.type()) {
				return s;
			}
		}
		return FOUR;
	}
}
