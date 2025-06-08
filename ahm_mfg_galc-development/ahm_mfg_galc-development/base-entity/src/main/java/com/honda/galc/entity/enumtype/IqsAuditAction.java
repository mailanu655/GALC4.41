package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum IqsAuditAction implements IdEnum<IqsAuditAction> {

	NONE(0, "None"),
	REPAIRED(1, "Repaired"),
	RETURN_TO_PLANT(2, "Return to Plant"),
	SHIP_AS_IS(3, "Ship As Is");

	private final int id;
	private String auditAction;

	private IqsAuditAction(int id, String auditAction) {
		this.id = id;
		this.auditAction = auditAction;
	}

	public int getId() {
		return id;
	}

	public String getAuditAction() {
		return auditAction;
	}

	public static IqsAuditAction getType(int id) {
		return EnumUtil.getType(IqsAuditAction.class, id);
	}

	@Override
	public String toString() {
		return auditAction;
	}
}
