package com.honda.galc.client.schedule;


/**
 * <h3>Class description</h3>
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
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * <TR>
 * <TD>Subu Kathiresan</TD>
 * <TD>Feb 25, 2013</TD>
 * <TD>2.0</TD>
 * <TD>Refactored to use SchedulingEventType</TD>
 * </TR>
 */

public class SchedulingEvent {

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
