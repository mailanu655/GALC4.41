package com.honda.galc.entity.enumtype;

import java.util.regex.Pattern;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Sep 11, 2016
 */
public enum TemplateType implements IdEnum<DeviceTagType> {
    JASPER(0, ""),
    COMPILED_JASPER(1, ""),
    ZEBRA_SCRIPT(2, "@"),
    BAUBLY_SCRIPT(3, "@"),
    TOSHIBA_SCRIPT(4, "@"),
    IMAGE(5, ""),
    MQ_CONFIG(6, "");
    
    private final int id;
    private final String variableDelimiter;

    private TemplateType(int id, String variableDelimiter) {
        this.id = id;
        this.variableDelimiter = variableDelimiter;
    }

    public int getId() {
        return id;
    }
    
    public String getVariableDelimiter() {
    	return variableDelimiter;
    }

    public static TemplateType getType(int id) {
        return EnumUtil.getType(TemplateType.class, id);
    }
    
    public Pattern getVariablePattern() {
		return Pattern.compile(variableDelimiter + ".*" + variableDelimiter);
    }
}
