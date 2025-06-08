package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;
/**
 * 
 * <h3>QiEntryStationDefaultStatus Class description</h3>
 * <p>
 * QiEntryStationDefaultStatus contains the status values for Entry Station
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
public enum QiEntryStationDefaultStatus implements IdEnum<QiEntryStationDefaultStatus> {
	
	NONE(0,"None"),
	REPAIRED(1,"Repaired"),
	NOT_REPAIRED(2,"Not Repaired"),
	NON_REPAIRABLE_SCRAP(3,"Non Repairable/Scrap");
	

	int id;
	String name;
	
	private QiEntryStationDefaultStatus(int id,String name) {
		this.name = name;
		this.id = id;
	}
	
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return StringUtils.trimToEmpty(name);
	}
	public static QiEntryStationDefaultStatus getType(int id) {
		return EnumUtil.getType(QiEntryStationDefaultStatus.class, id);
	}

}
