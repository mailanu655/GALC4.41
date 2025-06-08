package com.honda.galc.qics.mobile.shared.entity;

import java.util.HashMap;
import java.util.Map;

public enum DefectStatus {
	OUTSTANDING ( "Outstanding", 0),
	REPAIRED ( "Repaired", 1 ),
	DIRECT_PASS( "Direct Pass", 2),
	SCRAP ( "Scrap", 3),
	PREHEAT_SCRAP ("Preheat Scrap", 4);
	
	private final String name;
	private final int id;
	
	DefectStatus( String name, int id ) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	// Reverse-lookup map for getting a DefectStatus by name
    private static final Map<String, DefectStatus> lookupByName = new HashMap<String, DefectStatus>();
    static {
        for (DefectStatus d : DefectStatus.values())
        	lookupByName.put(d.name, d);
    }
    
    public static DefectStatus getByName(String name) {
        return lookupByName.get(name);
    }
    

} 
