package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum PreProductionLotSendStatus implements IdEnum<PreProductionLotSendStatus>{
	WAITING(0),INPROGRESS(1), DONE(2), SENT(3);

	int id;
	
	private PreProductionLotSendStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static PreProductionLotSendStatus getType(int id) {
		return EnumUtil.getType(PreProductionLotSendStatus.class, id);
	}
}
