package com.honda.galc.constant;

import org.apache.commons.lang.StringUtils;


/**
 * @author Subu Kathiresan
 * @date Mar 3, 2015
 */
public enum OperationType {
	
	INSTRUCTION,
	GALC_INSTRUCTION,
	GALC_SCAN_WITH_MEAS,
	GALC_SCAN,
	GALC_MEAS,
	GALC_SCAN_WITH_MEAS_MANUAL,
	GALC_MEAS_MANUAL,
	GALC_AUTO_COMPLETE,
	GALC_MADE_FROM;
	
	public static OperationType get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid OperationType name");
		}
		return Enum.valueOf(OperationType.class, StringUtils.trimToEmpty(name));
	}
}
 