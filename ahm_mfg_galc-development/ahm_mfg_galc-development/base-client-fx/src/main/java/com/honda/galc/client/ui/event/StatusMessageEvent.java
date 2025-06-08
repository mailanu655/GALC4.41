package com.honda.galc.client.ui.event;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.client.ui.IEvent;

/**
 * @author Subu Kathiresan
 * @date Sep 10, 2014
 */
public class StatusMessageEvent implements IEvent {
	
	private String message;
	private StatusMessageEventType eventType;
	private String applicationId;
	private Map<Object, Object> arguments;

	public StatusMessageEvent(String msg, StatusMessageEventType eventType) {
		this.message = msg;
		this.eventType = eventType;
	}
	
	public StatusMessageEvent(String msg, StatusMessageEventType eventType, String applicationId) {
		this.message = msg;
		this.eventType = eventType;
		this.applicationId = applicationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StatusMessageEventType getEventType() {
		return eventType;
	}

	public void setEventType(StatusMessageEventType eventType) {
		this.eventType = eventType;
	}
	
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Map<Object, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<Object, Object> arguments) {
		this.arguments = arguments;
	}
	public void addArgument(Object key, Object value) {
		if(getArguments() == null) {
			setArguments(new HashMap<Object, Object>());
		}
		getArguments().put(key, value);
	}
	
	public boolean hasArguments() {
		return arguments != null && !arguments.isEmpty();
	}
	
	public Object getArgument(Object key) {
		return hasArguments()
				? arguments.getOrDefault(key, null)
				: null;
	}
}
