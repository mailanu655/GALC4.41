package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>SubProductShippingDetailStatus Class description</h3>
 * <p> SubProductShippingDetailStatus description </p>
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
 * Aug 26, 2014
 *
 *
 */
public enum SubProductShippingDetailStatus implements IdEnum<SubProductShippingDetailStatus> {

	WAITING(0),
	LOADED(1),
	MTOC_NOT_MATCH(2),
	NEW_PROD_ID(3),
	DONE_WAITING(8),
	DONE(9),
	DONE_MTOC_NOT_MATCH(10),
	DONE_NEW_PROD_ID(11);

	int _id;

	private SubProductShippingDetailStatus(int id) {
		_id = id;
	}

	public int getId() {
		return _id;
	}
	
	public boolean isShipped(){
		return _id >=8;
	}

	public static SubProductShippingDetailStatus getType(int id) {
		return EnumUtil.getType(SubProductShippingDetailStatus.class, id);
	}
}
