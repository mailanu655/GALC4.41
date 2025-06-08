package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsMoveStatus Class description</h3>
 * <p> GtsMoveStatus description </p>
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
 * May 21, 2015
 *
 *
 */
public enum GtsMoveStatus implements IdEnum<GtsMoveStatus>{
	FINISHED(0),
	CREATED(1),
	STARTED(2),
	EXPIRED(3);
	
	private int id;
	
	private GtsMoveStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static GtsMoveStatus getType(int id) {
        return EnumUtil.getType(GtsMoveStatus.class, id);
    }
	
}
