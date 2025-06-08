package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.jhotdraw.app.action.Actions;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsMove;

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

public class ManualMoveAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private GtsMove move;
    
    public ManualMoveAction(GtsDrawing drawing,GtsMove move){
        this.drawing= drawing;
        this.move = move;
        this.putValue(Action.NAME, move.getId().getSourceLaneId() + " -> " + move.getId().getDestinationLaneId());
        this.putValue(Actions.SUBMENU_KEY, "Manual Move");
        
    }

    
    public void actionPerformed(java.awt.event.ActionEvent e) {
    
        Message message = drawing.getModel().checkMovePossible(move);
        
        if(message != null){
            JOptionPane.showMessageDialog(drawing.getController().getWindow(), message.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }else {
        	if(!drawing.getModel().isInManualMode() || Preference.isConfirmMessage()) {
        		if(JOptionPane.showConfirmDialog(drawing.getController().getWindow(), "Are you sure that you want to create move request from " + move.getSource() + " to " + move.getDestination()
                            , "Confirmation", JOptionPane.YES_NO_OPTION)
                            != JOptionPane.YES_OPTION) return;
        	}
            message = drawing.getModel().createMoveRequest(move);
            
            if(message != null){
                JOptionPane.showMessageDialog(drawing.getController().getWindow(), message.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
