package com.honda.galc.enumtype;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date April 6, 2016
 */
public enum SubscriptionType {

	INCLUDE,
	EXCLUDE;

	private SubscriptionType() {}

	public static SubscriptionType get(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Not a valid SubscriptionType");
		}
		return Enum.valueOf(SubscriptionType.class, StringUtils.trimToEmpty(name));
	}
}
