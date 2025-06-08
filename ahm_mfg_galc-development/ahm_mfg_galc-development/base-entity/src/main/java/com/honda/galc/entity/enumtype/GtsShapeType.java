package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsShapeType Class description</h3>
 * <p> GtsShapeType description </p>
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
 * Jun 8, 2015
 *
 *
 */
public enum GtsShapeType implements IdEnum<GtsShapeType>{
	
	RECTANGLE(0),
	ROUND_RECTANGLE(1),
	TRIANGLE(2),
	ELLIPSE(3),
	LINE(4);
	

	private final int id;
   
    private GtsShapeType(int intValue) {
		this.id = intValue;
    }
    
    public int getId() {
		return id;
	}
    
	
	public static GtsShapeType getType(int id) {
        return EnumUtil.getType(GtsShapeType.class, id);
    }

}
