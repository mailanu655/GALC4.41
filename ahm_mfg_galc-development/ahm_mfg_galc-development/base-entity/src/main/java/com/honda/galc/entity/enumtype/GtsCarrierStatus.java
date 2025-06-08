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
public enum GtsCarrierStatus implements IdEnum<GtsCarrierStatus>{
	
	NORMAL(0,"Normal",""),
	EMPTY(2,"Empty","E"),
	DAMAGED(3,"Damaged","D"),
	PALLET(4,"Paint Pallet","P");
	

	private final int id;
    private final String name;
    private final String displayName;
   
    private GtsCarrierStatus(int intValue, String name,String displayName) {
		this.id = intValue;
		this.name = name;
		this.displayName = displayName;
    }
    
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}


	public static GtsCarrierStatus getType(int id) {
        return EnumUtil.getType(GtsCarrierStatus.class, id);
    }

}
