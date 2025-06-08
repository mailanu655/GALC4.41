package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CorrectCarrierAction</code> is ...
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
 * <TD>Mar 20, 2008</TD>
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

public class CorrectCarrierAction extends AbstractAction{
	
    private static final long serialVersionUID = 1L;
    
	private CarrierFigure figure;
	public CorrectCarrierAction(CarrierFigure figure){
		
        this.figure = figure;
        this.putValue(Action.NAME, "Correct Carrier Number");
        
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
        
        GtsLaneCarrier laneCarrier = figure.getCarrier();
        if (!figure.getDrawing().getModel().areLaneGatesClosed(laneCarrier.getLane().getLaneId())){
            JOptionPane.showMessageDialog(figure.getDrawing().getController().getWindow(), "Lane is Open. Cannot Correct a carrier number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String carrierId = JOptionPane.showInputDialog(figure.getDrawing().getController().getWindow(),"Please input the carrier number");
        if(carrierId == null) return;
        
        GtsCarrier  carrier = figure.getDrawing().getModel().findCarrier(carrierId);
        if(carrier == null) {
        	  JOptionPane.showMessageDialog(figure.getDrawing().getController().getWindow(),"Invalid Carrier Number ", "Error", JOptionPane.ERROR_MESSAGE);
        	  return;
        }
        
        GtsLaneCarrier lc= figure.getDrawing().getModel().hasDuplicatedLaneCarriers(carrierId);
        
        
        if(lc != null ) {
        	if(JOptionPane.showConfirmDialog(figure.getDrawing().getController().getWindow(),
        			"Duplicated carrier " + lc.getCarrierId() + " is on lane  " + lc.getId().getLaneId() + " - Do you want to continue?", "Error", JOptionPane.ERROR_MESSAGE) != JOptionPane.YES_OPTION)
        		 return;
        }
        
        figure.getDrawing().getModel().correctCarrierByUser(laneCarrier, carrier.getCarrierNumber());
            
        laneCarrier.setLaneCarrier(carrier.getCarrierNumber());
        figure.init();
	}
}
