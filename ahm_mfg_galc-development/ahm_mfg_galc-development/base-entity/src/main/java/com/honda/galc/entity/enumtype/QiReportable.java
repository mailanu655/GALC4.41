package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>QiReportable Enum description</h3>
 * <p> QiReportable contains reportable and non reportable to be saved into database. </p>
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
 * @author vcc72927<br>
 * July 20, 2017
 *
 *
 */
/** * * 
* @version 0.1 
* @author vcc72927 
* @since July 20, 2017
*/
public enum QiReportable implements IdEnum<QiReportable> {
	/*
	0=Reportable (based on PDC attribute)
	1=Non-Reportable by Regional Maintenance
	2=Non-Reportable by Local Maintenance
	3=Non-Reportable by Station Configuration
	4=Non-Reportable by Repair - No Problem Found
	5=Non-Reportable by Data Correction
	*/
	
	REPORTABLE(0,"REPORTABLE"),
	NON_REPORTABLE_BY_REGIONAL_MAINTENANCE(1, "NON REPORTABLE BY REGIONAL MAINTENANCE"),
	NON_REPORTABLE_BY_LOCAL_MAINTENANCE(2, "NON REPORTABLE BY LOCAL MAINTENANCE"),
	NON_REPORTABLE_BY_STATION_CONFIG(3, "NON REPORTABLE BY STATION CONFIG"),
	NON_REPORTABLE_BY_NO_PROBLEM_FOUND(4, "NON REPORTABLE BY NO PROBLEM FOUND"),
	NON_REPORTABLE(5,"NON REPORTABLE"),
	NON_REPORTABLE_AFTER_SHIP(6,"NON REPORTABLE AFTER SHIP");
	
	int id;
	String name;
	
	private QiReportable(int id,String name) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static QiReportable getType(int id) {
		return EnumUtil.getType(QiReportable.class, id);
	}
	
	public static QiReportable getType(String name) {
		for(QiReportable type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		QiReportable status = getType(name);
		return status == null ? 0: status.getId();
	}
}
