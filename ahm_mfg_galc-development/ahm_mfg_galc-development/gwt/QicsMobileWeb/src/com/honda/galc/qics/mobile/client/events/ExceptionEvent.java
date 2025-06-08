package com.honda.galc.qics.mobile.client.events;

public class ExceptionEvent extends
		AbstractEvent<ExceptionEvent, Throwable> {

	public ExceptionEvent(Throwable eventData) {
		this.setEventData(eventData);
	}


}
