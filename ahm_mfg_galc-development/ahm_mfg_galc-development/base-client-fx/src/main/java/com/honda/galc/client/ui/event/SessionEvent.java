package com.honda.galc.client.ui.event;

import java.util.Date;

import com.honda.galc.client.ui.IEvent;

public class SessionEvent implements IEvent {

	private final Date eventTime;
	private final SessionEventType eventType;
	private final String applicationId;

	public SessionEvent(SessionEventType eventType) {
		this.eventTime = new Date();
		this.eventType = eventType;
		this.applicationId = null;
	}

	public SessionEvent(SessionEventType eventType, String applicationId) {
		this.eventTime = new Date();
		this.eventType = eventType;
		this.applicationId = applicationId;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public SessionEventType getEventType() {
		return eventType;
	}

	public String getApplicationId() {
		return applicationId;
	}
}
