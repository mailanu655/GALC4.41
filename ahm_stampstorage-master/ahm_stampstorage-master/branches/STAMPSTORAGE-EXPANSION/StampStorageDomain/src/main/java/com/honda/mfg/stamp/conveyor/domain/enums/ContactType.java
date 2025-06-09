package com.honda.mfg.stamp.conveyor.domain.enums;

public enum ContactType {

	EMAIL(0), PAGER(1), BOTH(2);

	private int type;

	ContactType(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}

	public static ContactType findByType(int type) {
		ContactType[] items = ContactType.values();
		for (int i = 0; i < items.length; i++) {
			ContactType s = items[i];
			if (type == s.type()) {
				return s;
			}
		}
		return EMAIL;
	}
}
