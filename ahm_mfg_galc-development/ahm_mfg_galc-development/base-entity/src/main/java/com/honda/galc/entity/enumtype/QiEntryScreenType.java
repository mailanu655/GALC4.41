package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum QiEntryScreenType implements IdEnum<QiEntryScreenType>{
	TEXT(0,"TEXT"),
	IMAGE(1,"IMAGE");
	
	int id;
	String name;
	
	private QiEntryScreenType(int id,String name) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiEntryScreenType getType(int id) {
		return EnumUtil.getType(QiEntryScreenType.class, id);
	}
	
	public static QiEntryScreenType getType(String name) {
		for(QiEntryScreenType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiEntryScreenType screenType = getType(name);
		return screenType == null ? 0: screenType.getId();
	}
	

	
	
}
