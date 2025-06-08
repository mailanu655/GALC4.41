package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * added new repair line type for QICS repair in panel 
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public enum LineType implements IdEnum<LineType> {
    LINE(0), AREA(1), REPAIR(2);

    private final int id;

    private LineType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LineType getType(int id) {
        return EnumUtil.getType(LineType.class, id);
    }
}
