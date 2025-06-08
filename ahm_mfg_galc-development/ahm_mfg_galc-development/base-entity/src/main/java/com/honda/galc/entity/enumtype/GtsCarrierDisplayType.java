package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsCarrierDisplayType Class description</h3>
 * <p> GtsCarrierDisplayType description </p>
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
 * Jun 1, 2015
 *
 *
 */
public enum GtsCarrierDisplayType implements IdEnum<GtsCarrierDisplayType>{
	
	CARRIER(0,"Carrier"),
	PRODUCT_ID(1,"Product Id"),
	SHORT_PRODUCT_ID(2,"Short Product Id"),
	PROD_LOT(3,"Production Lot"),
	SHORT_PROD_LOT(4,"Short Prod Lot"),
	YMTO(5,"YMTO"),
	YMTO_COLOR(6,"YMTO Color");
	

	private final int id;
    private final String name;
   
    private GtsCarrierDisplayType(int intValue, String name) {
		this.id = intValue;
		this.name = name;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public static GtsCarrierDisplayType getType(int id) {
        return EnumUtil.getType(GtsCarrierDisplayType.class, id);
    }

}
