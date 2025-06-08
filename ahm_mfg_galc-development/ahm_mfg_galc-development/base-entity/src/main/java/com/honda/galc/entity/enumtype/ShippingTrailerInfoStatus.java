package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>ShippingTrailerInfoStatus Class description</h3>
 * <p> ShippingTrailerInfoStatus description </p>
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
public enum ShippingTrailerInfoStatus implements IdEnum<ShippingTrailerInfoStatus>{

	WAITING(0),
	LOADING(1),
	HOLD(2),
	PCMP(3),
	COMPLETE(4),
	DEVANNED(5);
	
	private int id;
    
	private ShippingTrailerInfoStatus(int id) {
		this.id = id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ShippingTrailerInfoStatus getType(int id) {
        return EnumUtil.getType(ShippingTrailerInfoStatus.class, id);
    }
}
