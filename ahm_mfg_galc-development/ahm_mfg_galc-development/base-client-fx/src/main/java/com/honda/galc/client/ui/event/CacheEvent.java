package com.honda.galc.client.ui.event;

import com.honda.galc.client.ui.IEvent;

public class CacheEvent implements IEvent {

	private CacheEventType eventType;
	private String cacheKey;
	private String applicationId;

	public CacheEvent(CacheEventType eventType, String cacheKey) {
		this.eventType = eventType;
		this.cacheKey = cacheKey;
	}

	public CacheEvent(CacheEventType eventType, String cacheKey, String applicationId) {
		this.eventType = eventType;
		this.cacheKey = cacheKey;
		this.applicationId = applicationId;
	}

	public CacheEventType getEventType() {
		return eventType;
	}

	public void setEventType(CacheEventType eventType) {
		this.eventType = eventType;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
