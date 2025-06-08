package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum QiFlag implements IdEnum<QiFlag> {
	
	YES(1,"YES"),
	NO(0,"NO");
	
	int id;
	String name;
	
	private QiFlag(int id,String name) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiFlag getType(int id) {
		return EnumUtil.getType(QiFlag.class, id);
	}
	
	public static QiFlag getType(String name) {
		for(QiFlag type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiFlag status = getType(name);
		return status == null ? 0: status.getId();
	}
}
