package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum QiEntryModelVersioningStatus implements IdEnum<QiEntryModelVersioningStatus> {

	NO(0,"Pending"),
	YES(1,"Current");
	
	int id;
	String name;
	
	private QiEntryModelVersioningStatus(int id,String name) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiEntryModelVersioningStatus getType(int id) {
		return EnumUtil.getType(QiEntryModelVersioningStatus.class, id);
	}
	
	public static QiEntryModelVersioningStatus getType(String name) {
		for(QiEntryModelVersioningStatus type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiEntryModelVersioningStatus status = getType(name);
		return status == null ? 0: status.getId();
	}

}
