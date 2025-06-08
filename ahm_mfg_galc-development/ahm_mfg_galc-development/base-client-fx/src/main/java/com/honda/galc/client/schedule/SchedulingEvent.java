package com.honda.galc.client.schedule;

import com.honda.galc.client.ui.IEvent;


/**
 * <h3>Class description</h3>
 * Scheduling Event Class Description
 * Events used by the client. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 */

public class SchedulingEvent implements IEvent{

	private Object targetObject;
	private SchedulingEventType eventType;
	
	public SchedulingEvent(Object targetObject, SchedulingEventType eventType) {
		this.targetObject = targetObject;
		this.eventType = eventType;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public SchedulingEventType getEventType() {
		return eventType;
	}

	public void setEventType(SchedulingEventType eventType) {
		this.eventType = eventType;
	}
	
}
