package com.honda.galc.client.schedule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Application;

/**
 * <h3>Class description</h3> Schedule Window Class Description <h4>
 * Description</h4> <h4>Special Notes</h4> <h4>Change History</h4>
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
 * </TABLE>
 */

public class ScheduleWindow extends DefaultWindow {

	private MenuItem refreshMenu;

	public ScheduleWindow(ApplicationContext appContext,
			Application application) {
		super(appContext, application);
		initialize();
	}
	
	protected void initialize() {
		ClientMainFx.getInstance().getStage().setOnShown(new EventHandler<WindowEvent>() {

			@Override
			public void handle(javafx.stage.WindowEvent arg0) {
				((ScheduleMainPanel) getPanel())
						.requestFocusOnProductId();

			}
		});
	}
	

	@Override
	protected Menu createSystemMenu() {
		Menu systemMenu = super.createSystemMenu();
		getRefreshMenu().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent arg0) {
				refresh();

			}
		});
		systemMenu.getItems().add(getRefreshMenu());

		return systemMenu;
	}

	private MenuItem getRefreshMenu() {
		if (refreshMenu == null)
			refreshMenu = UiFactory.createMenuItem("Refresh");

		return refreshMenu;
	}

	private void refresh() {
		((ScheduleMainPanel) getPanel()).getController()
				.retrievePreProductionLots();
	}
}
