package com.honda.galc.qics.mobile.client.events;

public class PartGroupSelectedEvent extends  AbstractEvent<PartGroupSelectedEvent, String>{

	public PartGroupSelectedEvent(String partGroup) {
		super( partGroup );
	}

}
