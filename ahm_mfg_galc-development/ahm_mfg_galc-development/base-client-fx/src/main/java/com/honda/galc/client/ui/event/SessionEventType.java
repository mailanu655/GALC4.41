package com.honda.galc.client.ui.event;

public enum SessionEventType {
	SESSION_START		(0, "SESSION_START"),
	SESSION_END			(1, "SESSION_END");

	private int id = 0;
	private String name = "";

	private SessionEventType(int id, String name) {
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
