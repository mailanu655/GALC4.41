package com.honda.galc.qics.mobile.client.events;

public class DataAccessErrorEvent extends
		AbstractEvent<DataAccessErrorEvent, DataAccessErrorEventData> {

	public DataAccessErrorEvent(DataAccessErrorEventData eventData) {
		this.setEventData(eventData);
	}

	public DataAccessErrorEvent(String message, Throwable exception) {
		super();
		DataAccessErrorEventData eventData = new DataAccessErrorEventData(
				message, exception);
		setEventData(eventData);
	}

	public DataAccessErrorEvent(String message) {
		super();
		DataAccessErrorEventData eventData = new DataAccessErrorEventData(
				message, null);
		setEventData(eventData);
	}
}
