package com.honda.mfg.stamp.conveyor.domain.enums;

public enum AlarmTypes {

	UNDEFINED(0), MES_CONNECTION_UNHEALTHY(701), DB2_CONNECTION_UNHEALTHY(702), INSPECTION_REQUIRED_ALARM(703),
	INVALID_DIE_ALARM(704), INVALID_STOP_ALARM(705), BACK_ORDER_ALARM(706), UNABLE_TO_UNBLOCK(707),
	/** GSA 20140516 - Unable to Unblock alarm **/
	WRONG_DIE_IN_QUEUE(708);

	/** GSA 20140602 - Wrong Die in Q Stop **/

	/**
	 * @param type
	 */
	private AlarmTypes(int type) {
		this.type = type;
	}

	private final int type;

	public int type() {
		return type;
	}

	public static AlarmTypes findByType(int type) {
		AlarmTypes[] items = AlarmTypes.values();
		for (int i = 0; i < items.length; i++) {
			AlarmTypes s = items[i];
			if (type == s.type()) {
				return s;
			}
		}
		return UNDEFINED;
	}
}
