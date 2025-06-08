package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * 
 * 
 * <h3>RefreshDefectStatusAction Class description</h3>
 * <p> RefreshDefectStatusAction description </p>
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
 * Jun 10, 2020
 *
 *
 */
public class RefreshDefectStatusAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private CarrierFigure figure;
    
    public RefreshDefectStatusAction(CarrierFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
         return "Refresh Defect Status";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        GtsProduct product = figure.getCarrier().getProduct();
        getDrawing().getModel().refreshProductDefectStatus(product.getProductId());
    }
  
    public GtsDrawing getDrawing(){
        return figure.getDrawing();
    }
    
 
}
