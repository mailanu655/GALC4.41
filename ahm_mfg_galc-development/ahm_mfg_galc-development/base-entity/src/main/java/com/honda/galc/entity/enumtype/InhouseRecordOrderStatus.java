package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum InhouseRecordOrderStatus implements IdEnum<InhouseRecordOrderStatus>{
	
	/*
	 *1: Completed
	 *2: In-Progress
	 *3: Released
	 *4: Confirmed
	 *5: Planned
	 */
	UNDEFINED(0),
	COMPLETED(1),
	INPROGRESS(2),
	RELEASED(3),
	CONFIRMED(4),
	PLANNED(5);

	int id;
	
	private InhouseRecordOrderStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static InhouseRecordOrderStatus getType(int id) {
		return EnumUtil.getType(InhouseRecordOrderStatus.class, id);
	}
}
