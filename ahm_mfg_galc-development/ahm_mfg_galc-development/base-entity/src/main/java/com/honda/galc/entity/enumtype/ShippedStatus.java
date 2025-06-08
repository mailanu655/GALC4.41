package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>ShippedStatus</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ShippedStatus description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 28, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 28, 2017
 */
public enum ShippedStatus implements IdEnum<ShippedStatus>{
	SHIPPED(0), UNSHIPPED(1);
	private int id;

	private ShippedStatus(int id) {
		this.id = id;
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}


	public static ShippedStatus getStatus(int id) {
        return EnumUtil.getType(ShippedStatus.class, id);
    }

}
