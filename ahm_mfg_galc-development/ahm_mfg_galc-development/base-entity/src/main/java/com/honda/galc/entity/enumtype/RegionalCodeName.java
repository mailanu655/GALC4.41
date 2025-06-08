package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum RegionalCodeName implements IdEnum<RegionalCodeName> {
	
	CATEGORY_CODE(1,"CATEGORY_CODE"),
	DEFECT_STATUS(2,"DEFECT_STATUS"),
	PROCESS_AREA_GROUP_NAME(3, "PROCESS_AREA_GROUP_NAME"),
	REGIONAL_METRIC(4, "REGIONAL_METRIC");
	
	int id;
	String name;
	
	private RegionalCodeName(int id,String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static RegionalCodeName getType(int id) {
		return EnumUtil.getType(RegionalCodeName.class, id);
	}
	
	public static RegionalCodeName getType(String name) {
		for(RegionalCodeName type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		RegionalCodeName status = getType(name);
		return status == null ? 0: status.getId();
	}
}
