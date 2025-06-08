package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum VinBomApprovalStatus implements IdEnum<VinBomApprovalStatus> {
	PENDING(0),
	APPROVED(1),
	DENIED(2);

	private int id;
	
	private VinBomApprovalStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static VinBomApprovalStatus getType(int id) {
		return EnumUtil.getType(VinBomApprovalStatus.class, id);
	}
}
