package com.honda.galc.client.schedule;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.component.ObjectTablePane;

/**
 * 
 * <h3>ScheduleAction Class description</h3>
 * <p> ScheduleAction description </p>
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
 * Feb 1, 2013
 *
 *
 */
public class ScheduleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	public ObjectTablePane<?> tablePane;
	public ScheduleAction(String name, ObjectTablePane<?> tablePane) {
		this.putValue(Action.NAME, name);
		this.tablePane = tablePane;
	}
	
	public void actionPerformed(ActionEvent e) {
	
		SchedulingEvent schedulingEvent = 
			new SchedulingEvent(tablePane, SchedulingEventType.getType((String)this.getValue(Action.NAME)));
		EventBus.publish(schedulingEvent);
	}

}
