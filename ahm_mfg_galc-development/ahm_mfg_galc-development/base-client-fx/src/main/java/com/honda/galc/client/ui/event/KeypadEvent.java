package com.honda.galc.client.ui.event;
import com.honda.galc.client.ui.IEvent;

/**
 * @author Suriya Sena
 * @date 1 8, 2015
 */

public class KeypadEvent implements IEvent {
	
	private KeypadEventType eventType;
	private boolean isNavigatorSelected;
	private String text;

	public KeypadEvent(KeypadEventType eventType, boolean isNavigatorSelected) {
		this.eventType = eventType;
		this.isNavigatorSelected = isNavigatorSelected;
	}

	public boolean isNavigatorSelected() {
		return isNavigatorSelected;
	}
	
	public KeypadEventType getEventType() {
		return eventType;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return String.format("Event (%s), Nav Selected (%s)", eventType.name(), isNavigatorSelected);
	}
}
