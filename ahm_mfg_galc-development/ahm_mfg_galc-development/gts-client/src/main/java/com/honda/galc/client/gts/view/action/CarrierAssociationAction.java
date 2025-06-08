package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * 
 * <h3>CarrierAssociationAction Class description</h3>
 * <p> CarrierAssociationAction description </p>
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
public class CarrierAssociationAction extends AbstractAction{
	
    private static final long serialVersionUID = 1L;
    
    /*
	 * the position of the carrier to be added 
	 * BEFORE,AFTER the selected carrier
	 * HEAD,TAIL of the lane of the selected carrier
	 */
	private boolean isAssociation; 
	private CarrierFigure figure;
    
	public CarrierAssociationAction(CarrierFigure figure, boolean isAssociation){
		this.figure= figure;
        this.isAssociation = isAssociation;
		this.putValue(Action.NAME, getName());
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
        
        GtsCarrier carrier = figure.getCarrier().getCarrier();
        String productId = "";
        if(!carrier.isNormal()) return;
        if(!this.isAssociation) {
            // deassociation
            // no product to be associated
            if(carrier.getProductId()== null) return;
            
            if(!figure.getDrawing().getModel().isInManualMode() || Preference.isConfirmMessage()) {
                
            	if(JOptionPane.showConfirmDialog(figure.getDrawing().getController().getWindow(),
                                             "Are you sure that you want to deassociate carrier " + 
                                             carrier.getCarrierNumber() + " from product " +  carrier.getProductId(), 
                                             "Confirmation", JOptionPane.YES_NO_OPTION)
                            != JOptionPane.YES_OPTION) return;
            }
        }else {
            
            // association
            
            if(carrier.getProduct() != null && (!figure.getDrawing().getModel().isInManualMode() || Preference.isConfirmMessage())) {
                
                if(JOptionPane.showConfirmDialog(figure.getDrawing().getController().getWindow(),
                                "Carrier is associated to product " + carrier.getProductId() + ", do you still want to change association?",
                                "Confirmation", JOptionPane.YES_NO_OPTION)
                                != JOptionPane.YES_OPTION) return;
            }    
            
            // user input the product id
            productId = getProductToAdd(carrier.getProductId());
                
            if(productId == null) return;
            
            for(GtsLane lane :getDrawing().getModel().getLanes()){
                for(GtsLaneCarrier lc: lane.getLaneCarriers()){
                    
                    if(productId.equals(lc.getProductId()) && !carrier.getId().getCarrierNumber().equals(lc.getCarrierId())){
                        
                    	if(!figure.getDrawing().getModel().isInManualMode() || Preference.isConfirmMessage()) {
                            
                    		if(JOptionPane.showConfirmDialog(figure.getDrawing().getController().getWindow(),
                                       "Product " + productId + " has been associated with carrier " + lc.getCarrierId() + 
                                       ", do you still want to change association?",
                                       "Confirmation", JOptionPane.YES_NO_OPTION)
                                       != JOptionPane.YES_OPTION) return;
                    	}
                    }
                }
            }
            
        }
        
        Message message = getDrawing().getModel().changeAssociation(carrier.getId().getCarrierNumber(),productId);
        
        if(message != null){
            JOptionPane.showMessageDialog(figure.getDrawing().getController().getWindow(), message.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        
    }
    
    
	private String getName(){
        
		if(this.isAssociation) return "Carrier Product Association";
        else return "Carrier Product Deassociation";
        
	}
    
    private String getProductToAdd(String productId){
        
        String str = null;
        boolean flag;
        do {
            str = JOptionPane.showInputDialog(figure.getDrawing().getController().getWindow(),"Please input the Product ID");
            
            if(str == null || str.length() == 0) return null;
            if(flag = str.equals(productId)) {
                JOptionPane.showMessageDialog(figure.getDrawing().getController().getWindow(), 
                            "The product Id you want to change to is same as the current one.Please input the new one",
                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }while(flag);
        
        return str;
    }
    
    private GtsDrawing getDrawing(){
        return figure.getDrawing();
    }
	  
}
