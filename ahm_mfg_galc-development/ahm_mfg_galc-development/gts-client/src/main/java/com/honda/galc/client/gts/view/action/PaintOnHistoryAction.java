package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawing;

/**
 * 
 * 
 * 
 * <h3>PaintProductionProgressAction Class description</h3>
 * <p> PaintProductionProgressAction description </p>
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
 * Apr 9, 2018
 *
 *
 */

public class PaintOnHistoryAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    public PaintOnHistoryAction(GtsDrawing drawing){
        
        this.drawing = drawing;
        this.putValue(Action.NAME, "Paint On Details");
    }
 
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	PaintOnHistoryWindow paintOnHistoryWindow = new PaintOnHistoryWindow(drawing);
    	paintOnHistoryWindow.setVisible(true);
	}
}
