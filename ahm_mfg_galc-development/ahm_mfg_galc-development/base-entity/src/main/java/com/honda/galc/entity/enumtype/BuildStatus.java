package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.IdEnum;

public enum BuildStatus implements IdEnum<BuildStatus>{
	
	
	PARTIAL(0,"PARTIAL"),
	FINAL(1, "FINAL"),
	SCRAP(2,"SCRAP"),
	REMOVE(3,"REMOVE");
	
	
	private final int id;
	private String name;
      
    
    private BuildStatus(int intValue, String name) {
		this.id = intValue;
		this.name = name;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getName() {
	    	return name;
	}
}
