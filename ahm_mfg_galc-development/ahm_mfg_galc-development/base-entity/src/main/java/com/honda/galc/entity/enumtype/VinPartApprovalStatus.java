package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum VinPartApprovalStatus implements IdEnum<VinPartApprovalStatus> {
	PENDING(0),
	APPROVED(1),
	DENIED(2);

	private int id;
	
	private VinPartApprovalStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static VinPartApprovalStatus getType(int id) {
		return EnumUtil.getType(VinPartApprovalStatus.class, id);
	}
}
