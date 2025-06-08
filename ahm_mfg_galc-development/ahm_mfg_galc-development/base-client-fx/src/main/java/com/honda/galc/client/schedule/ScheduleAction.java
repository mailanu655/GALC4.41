package com.honda.galc.client.schedule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ObjectTablePane;

/**
 * 
 * <h3>Schedule Action Class description</h3>
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
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class ScheduleAction implements EventHandler<ActionEvent> {

	public ObjectTablePane<?> tablePane;
	public ScheduleAction(ObjectTablePane<?> tableView) {
		this.tablePane = tableView;
	}

	@Override
	public void handle(ActionEvent arg0) {
		SchedulingEvent schedulingEvent = 
				new SchedulingEvent(tablePane, SchedulingEventType.getType(((MenuItem)arg0.getSource()).getText()));
			EventBusUtil.publish(schedulingEvent);
		
	}

}
