package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>QiDefectEntryScanType Class description</h3>
 * <p>
 * QiDefectEntryScanType contains the Defect values for an Entry Department
 * </p>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * @author LnTInfotech<br>
 */

public enum QiDefectEntryScanType implements IdEnum<QiDefectEntryScanType> {
	
	REPAIRED(0, "Repaired"),
	NOT_REPAIRED(1, "Not Repaired"),
	NON_REPAIRABLE(2, "Non Repairable"),
	DIRECT_PASS(3, "Direct Pass"),
	DONE(4, "Done"),
	PART_DEFECT_COMBINATION(5, "Part Defect Combination"),
	VOID_LAST(6, "Void Last");
	
	int id;
	String name;
	
	private QiDefectEntryScanType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return StringUtils.trimToEmpty(name);
	}
	
	public static QiDefectEntryScanType getType(int id) {
		return EnumUtil.getType(QiDefectEntryScanType.class, id);
	}
	
	public static QiDefectEntryScanType getType(String name) {
		for(QiDefectEntryScanType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return QiDefectEntryScanType.PART_DEFECT_COMBINATION;
	}
	
}