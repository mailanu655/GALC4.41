package com.honda.galc.client.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.ClientMain;
import com.honda.galc.entity.conf.ApplicationMenuEntry;

/**
 * 
 * <h3>ApplicationMenuAction Class description</h3>
 * <p> This class wraps menu action for each application menu entry </p>
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
 * Mar 26, 2010
 *
 */
public class ApplicationMenuAction extends AbstractAction{
	
	
	private static final long serialVersionUID = 1L;
	
	private ApplicationMenuEntry menuEntry;
	
	public ApplicationMenuAction(ApplicationMenuEntry menuEntry) {
		this.menuEntry = menuEntry;
		this.putValue(Action.NAME, getApplicationName());
	}
	
	public void actionPerformed(ActionEvent e) {
		ClientMain.getInstance().startApplication(menuEntry.getNodeName());
	}
	
	
    private String getApplicationName() {
    	
    	return menuEntry.getApplicationName();
    	
    }
}
