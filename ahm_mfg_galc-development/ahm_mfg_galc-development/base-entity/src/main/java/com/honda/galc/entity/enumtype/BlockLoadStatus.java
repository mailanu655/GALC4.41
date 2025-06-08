package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>BlockLoadStatus</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BlockLoadStatus description </p>
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
 * <TD>May 26, 2011</TD>
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
 * @since May 26, 2011
 */

public enum BlockLoadStatus implements IdEnum<BlockLoadStatus>{
	LOADED(0),
	REMOVE(1),
	RELOAD(2),
	REMAKE(3),
	PRE_STAMP(8),
	STAMPED(9);

	private int id;

	private BlockLoadStatus(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public static BlockLoadStatus getType(int id) {
		return EnumUtil.getType(BlockLoadStatus.class, id);
	}


}
