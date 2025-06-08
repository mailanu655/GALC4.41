package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Sept 11, 2016
 */
public enum PrintAttributeFormatRequiredType implements IdEnum<PrintAttributeFormatRequiredType> {
    NOT_REQUIRED(0), 
    REQUIRED(1);


    private final int id;

    private PrintAttributeFormatRequiredType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PrintAttributeFormatRequiredType getType(int id) {
        return EnumUtil.getType(PrintAttributeFormatRequiredType.class, id);
    }

}
