package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum PartLotStatus implements IdEnum<PartLotStatus>{
	OPEN((short)0), INPROGRESS((short)1), CLOSED((short)2), SAFTYSTOCK((short)9);

	private short id;
	
	private PartLotStatus(short id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static PartLotStatus getType(short id) {
		return EnumUtil.getType(PartLotStatus.class, id);
	}

}
