package com.honda.galc.entity.enumtype;

public enum OperationMode {

	AUTO_MODE(0,"AUTO MODE"),
	MANUAL_MODE(1,"MANUAL SCAN MODE"),
	AUTO_RELINK_MODE(2,"AUTO RELINK MODE"),
	MANUAL_RELINK_MODE(3,"MANUAL RELINK MODE");

	int _id;
	String _name;

	private OperationMode(int id,String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}
	public int getId(){
	    return _id;
	}

	
}
