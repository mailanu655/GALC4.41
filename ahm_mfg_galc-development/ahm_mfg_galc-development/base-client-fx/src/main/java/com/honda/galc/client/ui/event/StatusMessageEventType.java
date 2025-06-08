package com.honda.galc.client.ui.event;

/**
 * @author Subu Kathiresan
 * @date Sep 10, 2014
 */
public enum StatusMessageEventType {
	INFO		(0, "INFO"),
	WARNING 	(1, "WARNING"),
	ERROR		(2, "ERROR"),
	CLEAR		(3, "CLEAR"),
	DIALOG_ERROR (4, "DIALOG_ERROR"),
	DIALOG_INFO  (5,"DIALOG_INFO");
	
	private int id = 0;
	private String name = "";

	private StatusMessageEventType(int id, String name){
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
