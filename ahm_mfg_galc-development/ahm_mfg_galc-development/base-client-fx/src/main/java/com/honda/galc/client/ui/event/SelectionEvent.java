package com.honda.galc.client.ui.event;

import com.honda.galc.client.ui.IEvent;

public class SelectionEvent implements IEvent {
	
	private SelectionEventType eventType;
	private Object targetObject;
	private Object source;
	
	public SelectionEvent(Object targetObject, SelectionEventType eventType) {
		this.targetObject = targetObject;
		this.eventType = eventType;
	}
	
	public SelectionEvent(Object source, Object targetObject, SelectionEventType eventType) {
		this.source = source;
		this.targetObject = targetObject;
		this.eventType = eventType;
	}

	public SelectionEventType getEventType() {
		return this.eventType;
	}

	public void setEventType(SelectionEventType eventType) {
		this.eventType = eventType;
	}

	public Object getSource() {
		return this.source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getTargetObject() {
		return this.targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

}
