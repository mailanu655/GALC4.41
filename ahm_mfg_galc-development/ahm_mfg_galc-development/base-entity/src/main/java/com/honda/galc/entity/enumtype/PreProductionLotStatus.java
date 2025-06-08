package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum PreProductionLotStatus implements IdEnum<PreProductionLotStatus> {
	
	HOLD(0),
	RELEASE(1);
	
	int id;
	
	private PreProductionLotStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static PreProductionLotStatus getType(int id) {
		return EnumUtil.getType(PreProductionLotStatus.class, id);
	}
}
