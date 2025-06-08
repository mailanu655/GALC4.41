package com.honda.galc.qics.mobile.client.events;

public class DataAccessProgressEvent extends AbstractEvent<DataAccessProgressEvent, Boolean> {

	public DataAccessProgressEvent(boolean inProgress) {
		super(inProgress);
	}

}
