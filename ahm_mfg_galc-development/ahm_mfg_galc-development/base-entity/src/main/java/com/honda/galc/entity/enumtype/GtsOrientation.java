package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsCarrierStatus Class description</h3>
 * <p> GtsCarrierStatus description </p>
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
 * May 11, 2015
 *
 *
 */
public enum GtsOrientation implements IdEnum<GtsOrientation>{
	
	NORTH(0,"North"),
	NORTH_EAST(1,"North_East"),
	EAST(2,"East"),
	SOUTH_EAST(3,"South_East"),
	SOUTH(4,"South"),
	SOUTH_WEST(5,"South_East"),
	WEST(6,"West"),
	NORTH_WEST(7,"North_West");

	private final int id;
    private final String name;
   
    private GtsOrientation(int intValue, String name) {
		this.id = intValue;
		this.name = name;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public static GtsOrientation getType(int id) {
        return EnumUtil.getType(GtsOrientation.class, id);
    }

}
