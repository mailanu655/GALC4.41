package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum LotControlRuleFlag implements IdEnum<LotControlRuleFlag> {
    OFF(0), ON(1);

    private int id;

    private LotControlRuleFlag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LotControlRuleFlag getType(int id) {
        return EnumUtil.getType(LotControlRuleFlag.class, id);
    }
}
