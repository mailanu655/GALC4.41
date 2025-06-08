package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsProductStatus Class description</h3>
 * <p> GtsProductStatus description </p>
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
 * Jun 17, 2015
 *
 *
 */
public enum GtsProductStatus implements IdEnum<GtsProductStatus>{
	
	RELEASE(0,"Release",""),
	HOLD(2,"Hold","H"),
	SHIPPING_HOLD(3,"Shipping_Hold","H");
	

	private final int id;
    private final String name;
    private final String shortName;
   
    private GtsProductStatus(int intValue, String name, String shortName) {
		this.id = intValue;
		this.name = name;
		this.shortName = shortName;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static GtsProductStatus getType(int id) {
        return EnumUtil.getType(GtsProductStatus.class, id);
    }

}
