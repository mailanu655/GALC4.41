package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

public enum PartCheck {
	
	DEFAULT(0),
	NONE(1);
	
	private int id;
	private PartCheck(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static PartCheck getType(int id) {
		for (PartCheck p : values()) {
			if (id == p.getId()) {
				return p;
			}
		}
		return null;
	}
	
	public static PartCheck get(String name) {
		PartCheck defaultPartCheck = PartCheck.DEFAULT;
		try {
			if(StringUtils.isNotEmpty(name)) {
				return Enum.valueOf(PartCheck.class, StringUtils.trimToEmpty(name));
			}
		} catch (IllegalArgumentException iae) {
			return defaultPartCheck;
		}
		return defaultPartCheck;
	}
}
