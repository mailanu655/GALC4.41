package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsFontStyle Class description</h3>
 * <p> GtsFontStyle description </p>
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
 * May 26, 2015
 *
 *
 */
public enum GtsFontStyle implements IdEnum<GtsFontStyle>{
	
	PLAIN(0),
	BOLD(1),
	ITALIC(2),
	BOLD_ITALIC(3);

	private final int id;
    
    private GtsFontStyle(int intValue) {
		this.id = intValue;
    }
    
    public int getId() {
		return id;
	}
    
	public static GtsFontStyle getType(int id) {
        return EnumUtil.getType(GtsFontStyle.class, id);
    }

}
