package com.honda.galc.client.ui.event;

public enum SelectionEventType {
	DIVISION		 	(1, "DIVISION"),
	LINE				(2, "LINE"),
	PROCESS_POINT		(3, "PROCESS_POINT"),
	DEVICE				(4, "Device");
	
	private int id = 0;
	private String name = "";

	private SelectionEventType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
