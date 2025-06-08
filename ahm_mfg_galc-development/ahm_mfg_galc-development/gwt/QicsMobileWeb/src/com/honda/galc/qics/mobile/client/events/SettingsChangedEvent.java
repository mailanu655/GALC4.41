package com.honda.galc.qics.mobile.client.events;

public class SettingsChangedEvent extends AbstractEvent<SettingsChangedEvent, SettingsChangedEventData>{
	
	public SettingsChangedEvent(SettingsChangedEventData settingsChangedEventData) {
		super( settingsChangedEventData );
	}

}
