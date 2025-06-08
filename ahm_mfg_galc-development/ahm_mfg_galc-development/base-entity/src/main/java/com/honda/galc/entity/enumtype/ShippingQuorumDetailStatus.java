package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>ShippingQuorumDetailStatus Class description</h3>
 * <p> ShippingQuorumDetailStatus description </p>
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
 * Sep 18, 2014
 *
 *
 */
public enum ShippingQuorumDetailStatus implements IdEnum<ShippingQuorumDetailStatus>{

	AUTO_LOAD(0),
	MANUAL_LOAD(1),
	MISS_LOAD(2);
	
	private int id;
    
	private ShippingQuorumDetailStatus(int id) {
		this.id = id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static ShippingQuorumDetailStatus getType(int id) {
        return EnumUtil.getType(ShippingQuorumDetailStatus.class, id);
    }
	
}
