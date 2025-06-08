package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.IdEnum;
/**
 * 
 * <h3>PddaStatus Class description</h3>
 * <p>
 * PddaStatus contains status values for PDDA status.
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 */
public enum PddaStatus implements IdEnum<PddaStatus>  {
	
	PROD(1,"PROD"),
	ADD_PROD(2,"ADD PROD"),
	MOVE_PURG(3,"MOVEPURG"),
	MOVE_PROD(4,"MOVEPROD"),
	PROD_PURG(5,"PRODPURG"),
	DELT_PURG(6,"DELTPURG");

	
	int id;
	String screenName;
	private PddaStatus(int id, String screenName) {
		this.screenName = screenName;
		this.id = id;
	}
	
	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return StringUtils.trimToEmpty(screenName);
	}
	
	public int getId() {
		return id;
	}
	
	public static PddaStatus getType(String name) { 
        for(PddaStatus type : values()) { 
           if(type.getScreenName().equalsIgnoreCase(name)) return type; 
       } 
        return null; 
   } 	
}
