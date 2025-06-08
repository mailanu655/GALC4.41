package com.honda.galc.enumtype;

import org.apache.commons.lang.StringUtils;

public enum OperationEfficiencyStatus {
	VIEW,
	UNIT_INCOMPLETE,
	UNIT_COMPLETE,
	PRODUCT_INCOMPLETE,
	PRODUCT_COMPLETE;

	public static OperationEfficiencyStatus get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid Operation Efficiency Status");
		}
		return Enum.valueOf(OperationEfficiencyStatus.class, StringUtils.trimToEmpty(name));
	}
}
