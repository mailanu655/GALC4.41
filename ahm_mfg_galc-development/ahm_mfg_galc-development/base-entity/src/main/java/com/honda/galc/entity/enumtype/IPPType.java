package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * IPP Type 
 * @author Vivek Bettada
 * @date 2016-01-21
 */
public enum IPPType implements IdEnum<IPPType> {
    LINE_SCAN(0, "LINE SCAN"), LINE_MTOIC(1, "LINE MTOIC");

    private final int id;
    private final String name;

    private IPPType(int id, String newName) {
        this.id = id;
        this.name = newName;
    }

    public int getId() {
        return id;
    }

    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static IPPType getType(int id) {
        return EnumUtil.getType(IPPType.class, id);
    }
}
