package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawingView;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ZoneInfoAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 16, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class PreferenceAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawingView view;
    public PreferenceAction(GtsDrawingView view){
        
        this.view = view;
        this.putValue(Action.NAME, "Preferences");
    }
 
    public void actionPerformed(java.awt.event.ActionEvent e) {
        PreferenceDialog preferenceWindow = new PreferenceDialog(view);
        preferenceWindow.setVisible(true);
	}
}
