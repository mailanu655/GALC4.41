package com.honda.galc.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.entity.conf.ApplicationMenuEntry;

/**
 * 
 * <h3>ApplicationMenuEventHandler Class description</h3>
 * <p>
 * This class provides common handler functionality for menu item actions
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
 * 
 * </TABLE>
 * 
 * @author Suriya Sena<br>
 *         Jan 24, 2014
 * 
 */

public class ApplicationMenuEventHandler implements EventHandler<ActionEvent> {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private final ApplicationMenuEntry menuEntry;

	public ApplicationMenuEventHandler(ApplicationMenuEntry menuEntry) {
		this.menuEntry = menuEntry;
	}

	@Override
	public void handle(ActionEvent arg0) {
		//click on menu item to open application in a new window
		ClientMainFx.getInstance().startApplication(menuEntry.getNodeName(), true);
	}
}
