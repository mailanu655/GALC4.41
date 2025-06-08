package com.honda.galc.client.teamleader.qics.screen;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.teamleader.qics.controller.QicsMaintenanceController;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.model.QicsMaintenanceModel;
import com.honda.galc.client.ui.TabbedPanel;

/**
 * 
 * <h3>QicsMaintenanceTabbedPanel Class description</h3>
 * <p> QicsMaintenanceTabbedPanel description </p>
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
 * Oct 28, 2011
 *
 *
 */
public abstract class QicsMaintenanceTabbedPanel extends TabbedPanel implements ListSelectionListener{

	private static final long serialVersionUID = 1L;
	private QicsMaintenanceFrame mainWindow;

	public QicsMaintenanceTabbedPanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
	}

	public QicsMaintenanceFrame getMainWindow() {
		return mainWindow;
	}
	
	protected QicsMaintenanceModel getClientModel() {
		return getController().getClientModel();
	}
	
	protected QicsMaintenanceController getController() {
		return getMainWindow().getController();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		
		Exception exception = null;
		ListSelectionModel model= (ListSelectionModel)e.getSource();
		try {
			getMainWindow().setWaitCursor();
			getMainWindow().clearMessage();

			if (model.isSelectionEmpty() && !model.getValueIsAdjusting()) {
				deselected(model);
			} else if (!model.getValueIsAdjusting()) {
				selected(model);
			}
		}catch(Exception ex) {
			exception = ex;
		} finally {
			getMainWindow().setDefaultCursor();
		}
		handleException(exception);
	}
	
	public void setMainWindow(QicsMaintenanceFrame mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	protected int getLeftMargin(){
		return 5;
	}
	
	protected int getTopMargin(){
		return 5;
	}

	public abstract void deselected(ListSelectionModel model);
	
	public abstract void selected(ListSelectionModel model);
}
