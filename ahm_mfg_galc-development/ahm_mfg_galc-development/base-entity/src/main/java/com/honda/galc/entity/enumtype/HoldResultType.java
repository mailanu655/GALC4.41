package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/*
 * 
 * @author Gangadhararao Gadde 
 * @ Feb 06, 2014
 */
public enum HoldResultType implements IdEnum<HoldResultType> {
	GENERIC_HOLD(-1,"Hold of any type"), HOLD_NOW(0,"Dept. Hold"), HOLD_AT_SHIPPING(1,"Hold at Shipping"), ;

    private final int id;
    private String typeString;

    private HoldResultType(int id,String typeString) {
        this.id = id;
        this.typeString= typeString;
    }

    public int getId() {
        return id;
    }
    
    public String getTypeString() {
    	return typeString;
    }

    public static HoldResultType getType(int id) {
        return EnumUtil.getType(HoldResultType.class, id);
    }
}
