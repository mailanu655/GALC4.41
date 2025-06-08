package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum VinBomActiveStatus implements IdEnum<VinBomActiveStatus> {
	INACTIVE(0),
	ACTIVE(1),
	DEPRECATED(2);

	private int id;
	
	private VinBomActiveStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static VinBomActiveStatus getType(int id) {
		return EnumUtil.getType(VinBomActiveStatus.class, id);
	}
}
