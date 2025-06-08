package com.honda.galc.qics.mobile.client.events;

public class SettingsChangedEventData {
	
	private String key;
	private String oldValue;
	private  String newValue;
	
	public SettingsChangedEventData() {
		
	}

	public SettingsChangedEventData(String key, String oldValue, String newValue ) {
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getOldValue() {
		return oldValue;
	}


	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}


	public String getNewValue() {
		return newValue;
	}


	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}
