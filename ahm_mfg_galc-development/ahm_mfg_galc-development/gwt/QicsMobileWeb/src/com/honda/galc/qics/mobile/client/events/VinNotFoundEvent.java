package com.honda.galc.qics.mobile.client.events;


public class VinNotFoundEvent extends  AbstractEvent<VinNotFoundEvent, String>{
	
	public VinNotFoundEvent( String vinNotFound ) {
		super( vinNotFound);
	}
}
