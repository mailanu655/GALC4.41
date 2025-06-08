package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.IdEnum;
/**
 * 
 * <h3>QiExternalSystem Class description</h3>
 * <p>
 * QiExternalSystem contains the External System name for Headless Defect Entry Screen
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
public enum QiExternalSystem implements IdEnum<QiExternalSystem>  {
	COLOR_HARMONY(1,"Color Harmony"),
	HEADLESS_ENTRY(2,"Headless Entry"),
	LET(3,"LET"),
	TORQUE_STATION(4,"Torque Station"),
	LOT_CONTROL(5,"Lot Control")
    ;
	
	int id;
	String externalSystemName;
	private QiExternalSystem(int id,String externalSystemName) {
		this.externalSystemName = externalSystemName;
		this.id = id;
	}
	
	/**
	 * @return the externalSystemName
	 */
	public String getExternalSystemName() {
		return StringUtils.trimToEmpty(externalSystemName);
	}
	
	public int getId() {
		return id;
	}
	
}
