package com.honda.galc.client.ui.event;

public enum CacheEventType {
	REFRESH_PDC_CACHE		(0, "REFRESH_PDC_CACHE");

	private int id = 0;
	private String name = "";

	private CacheEventType(int id, String name) {
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
