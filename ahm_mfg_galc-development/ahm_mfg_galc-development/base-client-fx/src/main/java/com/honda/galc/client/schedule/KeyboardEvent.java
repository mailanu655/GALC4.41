package com.honda.galc.client.schedule;

import com.honda.galc.client.enumtype.KeyboardEventType;
import com.honda.galc.client.ui.IEvent;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>KeyboardEvent</code> is Event class for Virtual Keyboard.
 * </p>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author L&T Infotech
 */
public class KeyboardEvent implements IEvent{

	private KeyboardEventType eventType;
	
	public KeyboardEvent(KeyboardEventType eventType) {
		super();
		this.eventType = eventType;
	}

	public KeyboardEventType getEventType() {
		return eventType;
	}

	public void setEventType(KeyboardEventType eventType) {
		this.eventType = eventType;
	}
}

