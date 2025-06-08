package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>HoldStatus</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HoldStatus description </p>
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
 * <TD>Jun 27, 2012</TD>
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
 * @since Jun 27, 2012
 */
public enum HoldStatus implements IdEnum<HoldStatus>{
	ON_HOLD(1, "OnHold"),
	NOT_ON_HOLD(0,"NoOnHold");
	private final int id;
	private String name;

	private HoldStatus(int intValue, String name) {
		this.id = intValue;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getNmae(){
		return name;
	}

}
