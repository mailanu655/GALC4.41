package com.honda.galc.client.gts.view.action;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsLane;

/**
 * 
 * 
 * <h3>AddCarrierAction Class description</h3>
 * <p> AddCarrierAction description </p>
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
 * Jun 12, 2015
 *
 *
 */
public class AddCarrierAction extends AbstractAction{
	
    private static final long serialVersionUID = 1L;
    
    /*
	 * the position of the carrier to be added 
	 * BEFORE,AFTER the selected carrier
	 * HEAD,TAIL of the lane of the selected carrier
	 */
	private int mode; 
	private int position;
    private GtsLane lane;
    private GtsDrawing drawing;
    
	public AddCarrierAction(GtsDrawing drawing,GtsLane lane,int position, int mode){
		this.drawing= drawing;
        this.lane = lane;
        this.position = position;
		this.mode = mode;
		this.putValue(Action.NAME, getName());
//		this.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
        
        if (!drawing.getModel().areLaneGatesClosed(lane.getLaneName())){
            JOptionPane.showMessageDialog(drawing.getController().getWindow(), "Lane is Open. Cannot add a carrier", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (lane.isFull()){
            JOptionPane.showMessageDialog(drawing.getController().getWindow(), "Lane is Full. Cannot add a carrier", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String carrierId = getCarrierToAdd();
        if(carrierId == null) return;
        
        GtsCarrier carrier = drawing.getModel().findCarrier(carrierId);
        if(carrier == null) {
        	  JOptionPane.showMessageDialog(drawing.getController().getWindow(),"Invalid Carrier Number ", "Error", JOptionPane.ERROR_MESSAGE);
        	  return;
        }
 
        if(!drawing.getModel().isInManualMode() || Preference.isConfirmMessage()) {
        	if(JOptionPane.showConfirmDialog(drawing.getController().getWindow(), "Are you sure that you want to add a carrier to a lane ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        }
        drawing.getModel().addCarrierByUser(lane.getLaneId(),getPosition(),carrier.getCarrierNumber());
        
   	}
	
	private int getPosition(){
	    int pos = this.position + 1;
        switch(mode){
            case 0: break;
            case 1: pos++;break;
            case 2: pos =1;break;
            case 3: pos = lane.getLaneCapacity() + 1;
        }
        return pos;
    }
    
	private String getName(){
		switch(mode){
			case 0: return "Add Carrier Before";
			case 1: return "Add Carrier After";
			case 2: return "Add Head of Lane";
			case 3: return "Add End of Lane";
		}
		return "Add Before";
	}
    
    private String getCarrierToAdd(){
        String str = JOptionPane.showInputDialog(drawing.getController().getWindow(),"Please input the carrier number");
        return str;
    }
	  
}
