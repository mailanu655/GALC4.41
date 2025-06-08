package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum QiActiveStatus implements IdEnum<QiActiveStatus> {
	
	INACTIVE(0,"INACTIVE"),
	ACTIVE(1,"ACTIVE");
	
	int id;
	String name;
	
	private QiActiveStatus(int id,String name) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiActiveStatus getType(int id) {
		return EnumUtil.getType(QiActiveStatus.class, id);
	}
	
	public static QiActiveStatus getType(String name) {
		for(QiActiveStatus type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiActiveStatus status = getType(name);
		return status == null ? 0: status.getId();
	}
}
