package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsInspectionStatus Class description</h3>
 * <p> GtsInspectionStatus description </p>
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
public enum GtsInspectionStatus implements IdEnum<GtsInspectionStatus>{
	
	PASS(0,""),
	UNKNOWN(1,"X"),
	FAIL(2,"F"),
    FAIL_S(3,"S"),
    FAIL_M(4,"M"),
    FAIL_L(5,"L"),
    WELD(6,"W"),
    DELAYED(7,"D"),
    STOP(8,"T"),
	PAUSE(9,"P");
	
	private final int id;
    private final String name;
    
    private GtsInspectionStatus(int intValue, String name) {
		this.id = intValue;
		this.name = name;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public static GtsInspectionStatus getType(int id) {
        return EnumUtil.getType(GtsInspectionStatus.class, id);
    }
	
	public static GtsInspectionStatus getType(String name) {
		for (GtsInspectionStatus status : GtsInspectionStatus.values()) {
			if(status.name().equalsIgnoreCase(name)) return status;
		}
		return null;
	}

}
