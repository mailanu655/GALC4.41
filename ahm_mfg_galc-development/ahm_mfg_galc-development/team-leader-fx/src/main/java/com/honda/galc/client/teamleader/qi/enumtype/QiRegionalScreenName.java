package com.honda.galc.client.teamleader.qi.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.IdEnum;
/**
 * 
 * <h3>ScreenName Class description</h3>
 * <p>
 * ScreenName contains the screen Names for QICS.
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
public enum QiRegionalScreenName implements IdEnum<QiRegionalScreenName>  {
	
	INSPECTION_LOCATION(1,"Location"),
	INSPECTION_PART(2,"Part"),
	DEFECT_MAINTENANCE(3,"Defect"),
	PART_LOCATION_COMBINATION(4,"Part Location Comb"),
	PART_DEFECT_COMBINATION(5,"Part Defect Comb"),
	REPAIR_METHOD(6,"Repair Method"),
	IMAGE_SECTION_MAINTENANCE(7,"Image Section"),
	IMAGE_MAINTENANCE(8,"Image"),
	RESPONSIBILITY_PLANT(9,"Regional Responsibility-Plant"),
	RESPONSIBILITY_SITE(10,"Regional Responsibility-Site"),
	PROCESS_POINT_GROUP(11,"Process Point Group");
	
	int id;
	String screenName;
	private QiRegionalScreenName(int id, String screenName) {
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
	
	public static QiRegionalScreenName getType(String name) { 
        for(QiRegionalScreenName type : values()) { 
           if(type.getScreenName().equalsIgnoreCase(name)) return type; 
       } 
        return null; 
   } 	
}
