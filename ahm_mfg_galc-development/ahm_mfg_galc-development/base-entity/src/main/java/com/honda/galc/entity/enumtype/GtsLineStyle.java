package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsLineStyle Class description</h3>
 * <p> GtsLineStyle description </p>
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
 * May 27, 2015
 *
 *
 */
public enum GtsLineStyle implements IdEnum<GtsLineStyle>{
	
	SOLID(0),
	DASHED(1);
	

	private final int id;
   
    private GtsLineStyle(int intValue) {
		this.id = intValue;
    }
    
    public int getId() {
		return id;
	}
    
	
	public static GtsLineStyle getType(int id) {
        return EnumUtil.getType(GtsLineStyle.class, id);
    }

}
