package com.honda.galc.qics.mobile.client.events;


public class PartDefectSelectedEvent extends  AbstractEvent<PartDefectSelectedEvent, String>{
	
	public PartDefectSelectedEvent(String partDefect) {
		super( partDefect );
	}


}
