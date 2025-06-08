package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/*
 * 
 * @author Gangadhararao Gadde 
 * @ Feb 06, 2014
 */
public enum LotHoldStatus implements IdEnum<LotHoldStatus> {
	LOT_NOT_ON_HOLD(0,"Lot not on Hold"), LOT_ON_HOLD(1,"Lot on Hold"), ;

    private final int id;
    private String typeString;

    private LotHoldStatus(int id,String typeString) {
        this.id = id;
        this.typeString= typeString;
    }

    public int getId() {
        return id;
    }
    
    public String getTypeString() {
    	return typeString;
    }

    public static LotHoldStatus getType(int id) {
        return EnumUtil.getType(LotHoldStatus.class, id);
    }
}
