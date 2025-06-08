package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum GtsNodeStatus implements IdEnum<GtsNodeStatus>{
	
	CLOSE(0),
	OPEN(1),
	LANE_CLOSED(2);
	
	private int id;
	
	private GtsNodeStatus(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static GtsNodeStatus getType(int id) {
        return EnumUtil.getType(GtsNodeStatus.class, id);
    }

}
