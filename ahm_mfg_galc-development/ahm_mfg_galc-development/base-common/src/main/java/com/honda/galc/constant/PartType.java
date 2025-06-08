package com.honda.galc.constant;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date Aug 22, 2014
 */
public enum PartType {
	
	NONE,  
	REFERENCE,
	MFG;

	public static PartType get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid PartType name");
		}
		return Enum.valueOf(PartType.class, StringUtils.trimToEmpty(name));
	}
}