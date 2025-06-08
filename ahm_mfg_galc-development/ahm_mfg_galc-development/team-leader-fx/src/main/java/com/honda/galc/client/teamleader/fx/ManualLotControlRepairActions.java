package com.honda.galc.client.teamleader.fx;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum ManualLotControlRepairActions implements IdEnum<ManualLotControlRepairActions>{

	EDIT_RESULTS(0, "Edit Results"),
	SHOW_HISTORY(1, "Show History"),
	INSERT_RESULTS(2, "Insert Results");

    private final int id;
    private final String name;
    
    private ManualLotControlRepairActions(int intValue, String name) {
		this.id = intValue;
		this.name = name;
	}

    public int getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public static ManualLotControlRepairActions getType(int id) {
        return EnumUtil.getType(ManualLotControlRepairActions.class, id);
    }
    
    public static ManualLotControlRepairActions getType(String name) {
  		for(ManualLotControlRepairActions type : values()) {
  			if(type.getName().equalsIgnoreCase(name)) return type;
  		}
  		return null;
  	}
    
    public static boolean isInsertResultsAction(ManualLotControlRepairActions action) {
    	return action == ManualLotControlRepairActions.INSERT_RESULTS;
    }
}
