package com.honda.galc.entity.enumtype;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum ApplicationType implements IdEnum<ApplicationType> {
    
	PROD(1,"Process Point", true), 
	PORD(2,"PORD", false),
	TEAM_LEAD(3,"Team Leader", false), 
	QICS(4,"QICS", true), 
	QICS_REPAIR(104, "QICS Repair", true);

	private static final int SUB_ID_FACTOR = 100;

    private final int id;
    private String typeString;
    private boolean processPointApplicationType;

    private ApplicationType(int id,String typeString, boolean processPointApplicationType) {
        this.id = id;
        this.typeString= typeString;
        this.processPointApplicationType = processPointApplicationType;
    }

    public int getId() {
        return id;
    }
    
    public int getMainId() {
    	return getId() % SUB_ID_FACTOR;
    }
    
	public int getSubId() {
		return getId() / SUB_ID_FACTOR;
	}
	
    public String getTypeString() {
    	return typeString;
    }

    public static ApplicationType getType(int id) {
        return EnumUtil.getType(ApplicationType.class, id);
    }

	public boolean isProcessPointApplicationType() {
		return processPointApplicationType;
	}

	public boolean isQics() {
		return getMainId() == QICS.getMainId();
	}
	
	// === utility methods === //
	public static List<ApplicationType> getMainApplicationTypes() {
		List<ApplicationType> list = new ArrayList<ApplicationType>();
		for (ApplicationType at : ApplicationType.values()) {
			if (at.getSubId() == 0) {
				list.add(at);
			}
		}
		return list;		
	}	
	
	public static List<ApplicationType> getByMainId(int mainId) {
		List<ApplicationType> list = new ArrayList<ApplicationType>();
		for (ApplicationType at : ApplicationType.values()) {
			if (at.getMainId() == mainId) {
				list.add(at);
			}
		}
		return list;		
	}
	
	public static List<ApplicationType> getProcessPointApplicationTypes() {
		List<ApplicationType> list = new ArrayList<ApplicationType>();
		for (ApplicationType at : ApplicationType.values()) {
			if (at.isProcessPointApplicationType()) {
				list.add(at);
			}
		}
		return list;
	}

	public static List<ApplicationType> getNonProcessPointApplicationTypes() {
		List<ApplicationType> list = new ArrayList<ApplicationType>();
		for (ApplicationType at : ApplicationType.values()) {
			if (!at.isProcessPointApplicationType()) {
				list.add(at);
			}
		}
		return list;
	}

	public static List<ApplicationType> getQicsApplicationTypes() {
		List<ApplicationType> list = new ArrayList<ApplicationType>();
		for (ApplicationType at : ApplicationType.values()) {
			if (at.isQics()) {
				list.add(at);
			}
		}
		return list;
	}
	
	public static List<Integer> getApplicationTypeIds(List<ApplicationType> types) {
 		List<Integer> ids =  new ArrayList<Integer>(); 
 		if (types == null || types.isEmpty()) {
 			return ids;
 		}
 		for (ApplicationType at : types) {
 			if (at == null) {
 				continue;
 			}
 			ids.add(at.getId());
 		}
 		return ids;
	}
}
