package com.honda.galc.qics.mobile.client.events;


public class VinScannedEvent extends AbstractEvent<VinScannedEvent, String> {

	public VinScannedEvent(String vin) {
		this.setEventData(vin);
	}
}