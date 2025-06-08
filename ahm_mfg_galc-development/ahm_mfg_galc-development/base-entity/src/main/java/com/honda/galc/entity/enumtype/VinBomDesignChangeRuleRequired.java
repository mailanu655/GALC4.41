package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum VinBomDesignChangeRuleRequired implements IdEnum<VinBomDesignChangeRuleRequired> {
	NOT_APPLIED(0),
	REQUIRED(1),
	OPTIONAL(2);

	private int id;
	
	private VinBomDesignChangeRuleRequired(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static VinBomDesignChangeRuleRequired getType(int id) {
		return EnumUtil.getType(VinBomDesignChangeRuleRequired.class, id);
	}
}
