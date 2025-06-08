package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * 
 * <h3>GtsDefectStatus Class description</h3>
 * <p> GtsDefectStatus description </p>
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
public enum GtsDefectStatus implements IdEnum<GtsDefectStatus>{
	
	OUTSTANDING(0,"Outstanding","R"),
	REPAIRED(1,"Repaired",""),
	DIRECT_PASS(2,"Direct Pass",""),
	SCRAP(3,"Scrap","S");
	
	private final int id;
    private final String name;
    private final String shortName;
   
    private GtsDefectStatus(int intValue, String name,String shortName) {
		this.id = intValue;
		this.name = name;
		this.shortName = shortName;
    }
    
    public int getId() {
		return id;
	}
    
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static GtsDefectStatus getType(int id) {
        return EnumUtil.getType(GtsDefectStatus.class, id);
    }

}
