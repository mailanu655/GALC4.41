package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsCarrierType Class description</h3>
 * <p> GtsCarrierType description </p>
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
public enum GtsBorderType implements IdEnum<GtsBorderType>{
	
	RECTANGLE(0),
	ROUND_RECTANGLE(1),
	ELLIPSE(2);
	

	private final int id;
   
    private GtsBorderType(int intValue) {
		this.id = intValue;
    }
    
    public int getId() {
		return id;
	}
    
	
	public static GtsBorderType getType(int id) {
        return EnumUtil.getType(GtsBorderType.class, id);
    }

}
