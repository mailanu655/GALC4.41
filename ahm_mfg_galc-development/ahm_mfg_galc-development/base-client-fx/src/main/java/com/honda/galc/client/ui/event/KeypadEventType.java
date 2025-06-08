package com.honda.galc.client.ui.event;

/**
 * author
 * Suriya Sena
 * 1/8/2015
 */
public enum KeypadEventType {
	KEY_LEFT    (1, "KEY_LEFT"),
	KEY_RIGHT    (2, "KEY_RIGHT"),
	KEY_COMPLETE	 (3, "KEY_COMPLETE"),
	KEY_TOGGLEPANE	 (4, "KEY_TOGGLEPANE"),
	KEY_SKIPOPERATION	 (5, "KEY_SKIPOPERATION"),
	KEY_SKIPTASK	 (6, "KEY_SKIPTASK"),
	KEY_SKIPPRODUCT	 (7, "KEY_SKIPPRODUCT"),
	KEY_PREVTASK	 (8, "KEY_PREVTASK"),
	KEY_PREVOPERATION	 (9, "KEY_PREVOPERATION"),
	KEY_ENTER	(10,"KEY_ENTER"),
	KEY_REJECT	 (11, "KEY_REJECT"),;
	

	private int id = 0;
	private String name = "";

	private KeypadEventType(int id, String name){
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
