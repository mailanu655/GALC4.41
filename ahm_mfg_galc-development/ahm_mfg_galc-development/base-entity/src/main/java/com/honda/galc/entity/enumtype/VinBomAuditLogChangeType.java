package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum VinBomAuditLogChangeType implements IdEnum<VinBomAuditLogChangeType> {
	DELETE(0),
	INSERT(1),
	UPDATE(2);

	private int id;
	
	private VinBomAuditLogChangeType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static VinBomAuditLogChangeType getType(String name) {
		return EnumUtil.getType(VinBomAuditLogChangeType.class, name);
	}
}
