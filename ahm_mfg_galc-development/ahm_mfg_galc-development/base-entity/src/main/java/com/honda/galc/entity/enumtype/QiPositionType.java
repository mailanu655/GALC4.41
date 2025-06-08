package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum QiPositionType implements IdEnum<QiPositionType> {
	
	PRIMARY(1,"PRIMARY"),
	SECONDARY(0,"SECONDARY");
	
	int id;
	String name;
	
	private QiPositionType(int id,String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiPositionType getType(int id) {
		return EnumUtil.getType(QiPositionType.class, id);
	}
	
	public static QiPositionType getType(String name) {
		for(QiPositionType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiPositionType type = getType(name);
		return type == null ? 0: type.getId();
	}
}
