package com.honda.galc.qics.mobile.client.events;

public class DefectsChangedEvent extends  AbstractEvent<DefectsChangedEvent, String>{
	
	
	public DefectsChangedEvent(String vin) {
		super( vin );
	}
	
}
