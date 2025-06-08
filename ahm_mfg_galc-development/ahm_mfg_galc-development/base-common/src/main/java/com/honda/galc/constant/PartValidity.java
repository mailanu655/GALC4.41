package com.honda.galc.constant;

import org.apache.commons.lang.StringUtils;

/**
 * @author Abhishek Garg
 * @date Mar 22, 2016
 */
public enum PartValidity {
	
	EXPIRED,  
	VALID,
	FUTUREDATED,
	INVALID,
	UNDEFINED;

	public static PartValidity get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid PartValidity name");
		}
		return Enum.valueOf(PartValidity.class, StringUtils.trimToEmpty(name));
	}
}