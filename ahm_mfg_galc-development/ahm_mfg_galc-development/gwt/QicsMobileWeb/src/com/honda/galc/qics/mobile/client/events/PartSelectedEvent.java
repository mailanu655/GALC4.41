package com.honda.galc.qics.mobile.client.events;


public class PartSelectedEvent extends  AbstractEvent<PartSelectedEvent, String> {
	

	public PartSelectedEvent(String part) {
		super( part );
	}

}
