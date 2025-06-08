package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>ShippingQuorumStatus Class description</h3>
 * <p> ShippingQuorumStatus description </p>
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
public enum ShippingQuorumStatus implements IdEnum<ShippingQuorumStatus>{

	WAITING(0),
	ALLOCATING(1),
	LOADING(2),
	DELAYED(3),
	COMPLETE(4),
	INCOMPLETE(5),
	ALLOCATED(6);
	
	private int id;
    
	private ShippingQuorumStatus(int id) {
		this.id = id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ShippingQuorumStatus getType(int id) {
        return EnumUtil.getType(ShippingQuorumStatus.class, id);
    }
}
