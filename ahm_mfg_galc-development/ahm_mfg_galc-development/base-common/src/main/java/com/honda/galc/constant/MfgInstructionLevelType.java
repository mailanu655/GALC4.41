package com.honda.galc.constant;

import org.apache.commons.lang.StringUtils;


/**
 * @author Brandon Kroeger
 * @date Aug 19, 2015
 */
public enum MfgInstructionLevelType {
	
	ALL,
	GALC;
	
	public static MfgInstructionLevelType get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid MfgInstructionLevelType name");
		}
		return Enum.valueOf(MfgInstructionLevelType.class, StringUtils.trimToEmpty(name));
	}
}
 