package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.LaneInfoWindow;
import com.honda.galc.entity.gts.GtsLane;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CarrierInfoAction</code> is ...
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

public class LaneInfoAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private GtsLane lane;
    
    public LaneInfoAction(GtsDrawing drawing,GtsLane lane){
    	this.drawing = drawing;
        this.lane = lane;
        this.putValue(Action.NAME, "Lane Information");
    }
 
    public void actionPerformed(java.awt.event.ActionEvent e) {
        LaneInfoWindow laneInfoWindow = new LaneInfoWindow(drawing,lane);
        laneInfoWindow.setVisible(true);
	}
}
