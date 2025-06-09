package com.honda.mfg.stamp.storage.service.utils;

public enum RoleChangeReason {

	FORCE_START("Force Start"), DESIGNATED_PRI("Designated Primary"), FORCE_STOP("Force Stop"),
	MISSED_PING("Missed Ping"), COMMUNICATION_ERRORS("Communication Error");

	private String message;

	private RoleChangeReason(String reason) {
		message = reason;
	}

	public String getMessage() {
		return message;
	}
}
