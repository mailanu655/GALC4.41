package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum StationConfigurationOperations implements IdEnum<StationConfigurationOperations> {
	RIGHT_SHIFT_DEPARTMENT(0,"rightShiftDepartment"), 					
	LEFT_SHIFT_DEPARTMENT(1,"leftShiftDepartment"), 					
    UPDATE_ENTRY_DEPARTMENT(2,"updateEntryDepartment"), 					
    RESET_ENTRY_DEPARTMENT(3,"resetEntryDepartment"), 					
    RIGHT_SHIFT_WRITEUP_DEPARTMENT(4,"rightShiftWriteUpDepartment"), 	
    LEFT_SHIFT_WRITEUP_DEPARTMENT(5,"leftShiftWriteUpDepartment"), 
    UPDATE_WRITEUP_DEPARTMENT(6,"updateWriteUpDepartment"), 
    RESET_WRITEUP_DEPARTMENT(7,"resetWriteUpDepartment"), 
    RIGHT_SHIFT_ENTRY_SCREEN(8,"rightShiftEntryScreen"),
    LEFT_SHIFT_ENTRY_SCREEN(9,"leftShiftEntryScreen"),
    UPDATE_ENTRY_SCREEN(10,"updateEntryScreen"),
    
    RESET_ENTRY_SCREEN(11,"resetEntryScreen"),
    RIGHT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS(12,"rightShiftEntryDepartmentDefectStatus"),
    LEFT_SHIFT_ENTRY_DEPARTMENT_DEFECT_STATUS(13,"leftShiftEntryDepartmentDefectStatus"),
    UPDATE_ENTRY_DEPARTMENT_DEFECT_BUTTON(14,"updateEntryDepartmenDefectStatustBtn"),
    RESET_ENTRY_DEPARTMENT_DEFECT_BUTTON(15,"resetEntryDepartmenDefectStatustBtn"),
    UPDATE_SETTINGS_PROPERTY(16,"updateSettingsProperty"),
    SAVE_ALL_SETTINGS(17,"saveAllSettings"),
    UPDATE_ENTRY_STATION_STATUS(18,"updateEntryStationStatus"),
    UPDATE_UPC_PART_BUTTON(19,"updateUPCPartBtn"),
    RESET_UPC_PART_BUTTON(20,"resetUPCPartBtn"),
    RIGHT_SHIFT_UPC_PART_BUTTON(21,"rightShiftUPCPartBtn"),
    LEFT_SHIFT_UPC_PART_BUTTON(22,"leftShiftUPCPartBtn"),
    
    RIGHT_SHIFT_RESPONSIBILITY_SITE(23,"rightShiftResponsibilitySite"),
	LEFT_SHIFT_RESPONSIBILITY_SITE(24,"leftShiftResponsibilitySite"),
	RIGHT_SHIFT_RESPONSIBILITY_PLANT(25,"rightShiftResponsibilityPlant"),
	LEFT_SHIFT_RESPONSIBILITY_PLANT(26,"leftShiftResponsibilityPlant"),
	RIGHT_SHIFT_RESPONSIBILITY_DEPT(27,"rightShiftResponsibilityDept"),
	LEFT_SHIFT_RESPONSIBILITY_DEPT(28,"leftShiftResponsibilityDept"),
	RIGHT_SHIFT_RESPONSIBILITY_LEVEL1(29,"rightShiftResponsibilityLevel1"),
	LEFT_SHIFT_RESPONSIBILITY_LEVEL1(30,"leftShiftResponsibilityLevel1"),
	UPDATE_RESPONSIBILITY(31,"updateResponsibility"),
	RESET_RESPONSIBILITY(32,"resetResponsibility"),
	SAVE_STATION_REPAIR_AREA(33,"saveStationRepairArea"),
 
	COPYSTN(34,"copyStation"),
	COPYSTN_RESET(35,"resetCopyStation"),
	COPYSTN_SELECT_ALL(36,"selectAllCopyStation"),
	COPYSTN_IMPORT(37,"importStation"),
	COPYSTN_EXPORT(38,"exportStation");
	
    private final int id;
    private final String name;

    private StationConfigurationOperations(int id,String name) {
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return id;
    }
    
    public String getName(){
    	return name;
    }

    public static StationConfigurationOperations getType(int id) {
        return EnumUtil.getType(StationConfigurationOperations.class, id);
    }
    public static StationConfigurationOperations getType(String name) {
    	for (StationConfigurationOperations type : values()) {
			if(type.getName().equals(name))
			return type;	
		}
    	return null;
    }
}
