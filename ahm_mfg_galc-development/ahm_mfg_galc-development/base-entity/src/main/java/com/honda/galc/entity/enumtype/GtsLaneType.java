package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsLaneType Class description</h3>
 * <p> GtsLaneType description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 26, 2015
 *
 *
 */
public enum GtsLaneType implements IdEnum<GtsLaneType>{
	
	NORMAL(0,"Normal"),
	ENTRY(1,"Entry"),
	EXIT(2,"Exit"),
	MOVE_PROGRESS(3,"MoveProgress");
	

	private final int id;
    private final String name;
   
    private GtsLaneType(int intValue, String name) {
		this.id = intValue;
		this.name = name;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public static GtsLaneType getType(int id) {
        return EnumUtil.getType(GtsLaneType.class, id);
    }

}
