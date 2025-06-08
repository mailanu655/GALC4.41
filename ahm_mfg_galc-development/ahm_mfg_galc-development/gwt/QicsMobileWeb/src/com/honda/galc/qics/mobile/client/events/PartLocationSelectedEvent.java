package com.honda.galc.qics.mobile.client.events;



public class PartLocationSelectedEvent extends  AbstractEvent<PartLocationSelectedEvent, String>{
	

	public PartLocationSelectedEvent(String partLocation) {
		super( partLocation );
	}

}
