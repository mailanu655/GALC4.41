package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum SkippedProductStatus implements IdEnum<PartLotStatus>{
	DISABLED((short)0), SKIPPED((short)1), COMPLETED((short)2);

	private short id;
	
	private SkippedProductStatus(short id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static SkippedProductStatus getType(short id) {
		return EnumUtil.getType(SkippedProductStatus.class, id);
	}
}
