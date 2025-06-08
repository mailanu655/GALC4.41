package com.honda.galc.client.ui.event;

import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.ui.IEvent;

/**
 * <h3>Class description</h3>
 * ObservableListChangeEvent Class Description
 * Event Types used to process (like selection of other value) on observableArrayList from ChangeListener.
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
 * <TD>Prakash Dalvi</TD>
 * <TD>Feb 04 2017</TD>
 * <TD>1.0</TD>
 * </TD>
 * <TD>Initial Release</TD>
 * </TR>
 */
public class ObservableListChangeEvent implements IEvent {
	
	private Object object;
	private Object value;
	private ObservableListChangeEventType eventType;
	
	public ObservableListChangeEvent(Object object, ObservableListChangeEventType eventType) {
		this.object = object;
		this.eventType = eventType;
	}
	
	public ObservableListChangeEvent(Object object, Object value, ObservableListChangeEventType eventType) {
		this.object = object;
		this.value = value;
		this.eventType = eventType;
	}

	public ObservableListChangeEventType getEventType() {
		return eventType;
	}

	public void setEventType(ObservableListChangeEventType eventType) {
		this.eventType = eventType;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
