package com.honda.galc.client.dc.enumtype;

import org.apache.commons.lang.StringUtils;

public enum ShimInstallPartType {
	BASE_SHIM_ID,
	BASE_GAP,
	ACTUAL_SHIM_ID,
	FINAL_GAP;
	
	public static ShimInstallPartType get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid Shim Install Part Type name");
		}
		return Enum.valueOf(ShimInstallPartType.class, StringUtils.trimToEmpty(name));
	}
}
