package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 04, 2016
 */
public enum PartNameVisibleType implements IdEnum<PartNameVisibleType> {
    EDITABLE(0), 
    HIDDEN(1),
    READ_ONLY(2),
    TL_CONFIRM(3); //Refer to: RGALCDEV-6982; Need TL log in to enter/update build result; 


    private final int id;

    private PartNameVisibleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PartNameVisibleType getType(int id) {
        return EnumUtil.getType(PartNameVisibleType.class, id);
    }

}
