package com.honda.galc.client.ui.event;

/**
 * 
 * <h3>Event Class description</h3>
 * <p> General event for event bus </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 9, 2011
 *
 *
 */
public class Event {
	private Object source;
	private Object target;
	
	private int eventType;
	
	
	public Event(Object source,int eventType) {
		this.source = source;
		this.eventType = eventType;
	}
	
	public Event(Object source, Object object, int eventType) {
		this(source,eventType);
		this.target = object;
	}	
	
	//Getters & Setters
	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
	
	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public boolean isEventFromSource(int eventType,Object object) {
		return isEvent(eventType) && isSource(object);
	}
	
	public boolean isSource(Object object) {
		return source.equals(object);
	}
	
	public boolean isEvent(int eventType) {
		return this.eventType == eventType;
	}


}
