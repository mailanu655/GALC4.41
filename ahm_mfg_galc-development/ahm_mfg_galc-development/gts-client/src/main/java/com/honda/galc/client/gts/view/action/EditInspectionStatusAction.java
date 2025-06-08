package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * 
 * 
 * <h3>EditInspectionStatusAction Class description</h3>
 * <p> EditInspectionStatusAction description </p>
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
 * Jun 17, 2015
 *
 *
 */
public class EditInspectionStatusAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private CarrierFigure figure;
    
    public EditInspectionStatusAction(CarrierFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
         return "Edit Inspection Status";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        GtsProduct product = figure.getCarrier().getProduct();
        GtsInspectionStatus status = getUserSelection(product);
        
        if(status == null) return;
        
        if(figure.getCarrier().getProduct().getInspectionStatus() != status) {
            product.setInspectionStatus(status.getId());
            figure.refreshStatus();
            getDrawing().getModel().updateInspectStatus(product.getProductId(), product.getInspectionStatusValue());
        }
        
    }
  
    public GtsDrawing getDrawing(){
        return figure.getDrawing();
    }
    
    private GtsInspectionStatus getUserSelection(GtsProduct product){
 
    	// Create the JList containing the items:
    	JComboBox<GtsInspectionStatus> combo = new JComboBox<GtsInspectionStatus>(GtsInspectionStatus.values());
    	combo.setSelectedItem(product.getInspectionStatus());
    	combo.setMaximumRowCount(GtsInspectionStatus.values().length + 1);
    	Object[] message = { "Please select the inspection status", combo };
    		
    	// Use showOptionDialog () interface which offers the most complete
    	int resp = JOptionPane.showOptionDialog(
    			figure.getDrawing().getController().getWindow(), message,"Inspection Status Selection",
    			JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
    				null, null, null);
    		
    		// And we treat the return val:
   		if (resp == JOptionPane.OK_OPTION) {
    			return (GtsInspectionStatus)combo.getSelectedItem();
    	}
   		
    	return null;
 
    }
    
}
