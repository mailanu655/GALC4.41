package com.honda.galc.client.gts.view.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.entity.gts.GtsLane;

/**
 * 
 * 
 * <h3>RemoveCarrierAction Class description</h3>
 * <p> RemoveCarrierAction description </p>
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
 * Jun 19, 2015
 *
 *
 */
public class RemoveCarrierAction extends AbstractAction{

    private static final long serialVersionUID = 1L;

    
    private int position; 
    private CarrierFigure figure;
    public RemoveCarrierAction(CarrierFigure figure,int position){
        this.figure = figure;
        this.position = position;
        this.putValue(Action.NAME, "Remove Carrier");
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        GtsLane lane = figure.getCarrier().getLane();
        if(lane == null) return;
        
        if (!figure.getDrawing().getModel().areLaneGatesClosed(lane.getLaneId())){
            JOptionPane.showMessageDialog(figure.getDrawing().getController().getWindow(), 
                            "Lane is Open. Cannot remove a carrier", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        figure.getView();
        
        if(!figure.getDrawing().getModel().isInManualMode() || Preference.isConfirmMessage()) {
            
        	if(JOptionPane.showConfirmDialog(figure.getDrawing().getController().getWindow(),
                        "Are you sure that you want to remove the carrier?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        }
        
        ((GtsDrawing)figure.getDrawing()).getController().getModel().removeCarrierByUser(figure.getCarrier(),position + 1);
    }
    
}
