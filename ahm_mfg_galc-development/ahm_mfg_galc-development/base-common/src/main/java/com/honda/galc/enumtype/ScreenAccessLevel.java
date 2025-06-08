package com.honda.galc.enumtype;

public enum ScreenAccessLevel implements IdEnum<ScreenAccessLevel> {
	NO_ACCESS(0), 
	FULL_ACCESS(1), 
	READ_ONLY(2);

	private final int id;

	private ScreenAccessLevel(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ScreenAccessLevel getType(int id) {
		return EnumUtil.getType(ScreenAccessLevel.class, id);
	}
}
