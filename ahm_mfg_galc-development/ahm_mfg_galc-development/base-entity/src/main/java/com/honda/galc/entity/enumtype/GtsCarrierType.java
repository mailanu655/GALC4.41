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
public enum GtsCarrierType implements IdEnum<GtsCarrierType>{
	
	NORMAL(0,"Normal"),
	BAD(1,"Bad"),
	EMPTY(2,"Empty"),
	SCRAP(3,"Scrap"),
	SCRAP1(4,"Scrap1"),
	SCRAP2(5,"Scrap2");
	

	private final int id;
    private final String name;
   
    private GtsCarrierType(int intValue, String name) {
		this.id = intValue;
		this.name = name;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public static GtsCarrierType getType(int id) {
        return EnumUtil.getType(GtsCarrierType.class, id);
    }

}
