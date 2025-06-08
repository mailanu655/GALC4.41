package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>DataCorrectionUpdateAttributes Enum description</h3>
 * <p> DataCorrectionUpdateAttributes contains various fields to be updated in QiDefectResult table </p>
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
public enum DataCorrectionUpdateAttributes implements IdEnum<DataCorrectionUpdateAttributes> {
	
	RESPONSIBLE_PLANT(0,"responsiblePlant"),
	RESPONSIBLE_DEPT(1,"responsibleDept"), 					
	RESPONSIBLE_LEVEL_1(2,"responsibleLevel1"), 					
	RESPONSIBLE_LEVEL_2(3,"responsibleLevel2"), 					
	RESPONSIBLE_LEVEL_3(4,"responsibleLevel3"), 					
	REPORTABLE(5,"reportable"), 	
	LOCALTHEME(6,"localTheme"), 
	ORIGINAL_DEFECT_STATUS(7,"originalDefectStatus"), 
	CURRENT_DEFECT_STATUS(8,"currentDefectStatus"),
	RESPONSIBLE_SITE(9,"responsibleSite"), 
	GDP_DEFECT(10,"gdpDefect"),
	INSPECTION_PART_NAME(11,"inspectionPartName"),
	INSPECTION_PART_LOCATION_NAME(12,"inspectionPartLocationName"),
	INSPECTION_PART_LOCATION2_NAME(13,"inspectionPartLocation2Name"),
	INSPECTIONPART2_NAME(14,"inspectionPart2Name"),
	INSPECTIONPART2_LOCATION_NAME(15,"inspectionPart2LocationName"),
	INSPECTIONPART2_LOCATION2_NAME(16,"inspectionPart2Location2Name"),
	INSPECTIONPART3_NAME(17,"inspectionPart3Name"),
	DEFECT_TYPE_NAME(18,"defectTypeName"),
	DEFECT_TYPE_NAME2(19,"defectTypeName2"),
	PROCESS_NO(20,"processNo"),
	PROCESS_NAME(21,"processName"),
	UNIT_NO(22,"unitNo"),
	UNIT_DESC(23,"unitDesc"),
	ENTRY_SITE_NAME(24,"entrySiteName"),
	ENTRY_PLANT_NAME(25,"entryPlantName"),
	REPAIR_AREA(26,"repairArea"),
	REPAIR_METHOD_NAME_PLAN(27,"repairMethodNamePlan"),
	REPAIR_TIME_PLAN(28,"repairTimePlan"),
	ENGINE_FIRING_FLAG(29,"engineFiringFlag"),
	IQS_VERSION(30,"iqsVersion"),
	IQS_CATEGORY_NAME(31,"iqsCategoryName"),
	IQS_QUESTION_NO(32,"iqsQuestionNo"),
	IQS_QUESTION(33,"iqsQuestion"),
	THEME_NAME(34,"themeName"),
	DEFECT_CATEGORY_NAME(35,"defectCategoryName"),
	ENTRY_SCREEN(36,"entryScreen"),
	IQS_SCORE(37,"iqsScore");
    
	int id;
	String name;
	
	private DataCorrectionUpdateAttributes(int id,String name) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static DataCorrectionUpdateAttributes getType(int id) {
		return EnumUtil.getType(DataCorrectionUpdateAttributes.class, id);
	}
	
	public static DataCorrectionUpdateAttributes getType(String name) {
		for(DataCorrectionUpdateAttributes type : values()) {
			if(type.getName().equalsIgnoreCase(name)) return type;
		}
		return null;
	}
	
	public static int getId(String name) {
		DataCorrectionUpdateAttributes status = getType(name);
		return status == null ? 0: status.getId();
	}
}
