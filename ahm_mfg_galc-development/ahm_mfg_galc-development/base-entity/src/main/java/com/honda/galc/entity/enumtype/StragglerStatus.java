package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum StragglerStatus implements IdEnum<StragglerStatus> {
	
	NO_ACTION(0),
	CREATED(1),
	RELEASE(2),
	CREATED_AND_RELEASE(3);
	
	int id;
	
	private StragglerStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static StragglerStatus getType(int id) {
		return EnumUtil.getType(StragglerStatus.class, id);
	}
}
