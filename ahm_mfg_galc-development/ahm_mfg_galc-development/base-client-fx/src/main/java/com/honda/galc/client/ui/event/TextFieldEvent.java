package com.honda.galc.client.ui.event;

import com.honda.galc.client.ui.IEvent;

public class TextFieldEvent implements IEvent {
	private String viewId;
	private String text;
	
	public TextFieldEvent(String viewId, String text) {
		this.viewId = viewId;
		this.text = text;
	}
	
	public String getViewId() {
		return this.viewId;
	}
	
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
