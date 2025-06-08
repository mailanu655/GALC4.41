package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.jhotdraw.app.action.Actions;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.common.message.Message;
import com.honda.galc.entity.enumtype.GtsCarrierType;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsProduct;

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

public class SetScrapAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private CarrierFigure figure;
    
    public SetScrapAction(GtsDrawing drawing,CarrierFigure figure){
        this.drawing= drawing;
        this.figure = figure;
        this.putValue(Action.NAME, "Set Scrap");
    }

    
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	GtsCarrier carrier = figure.getCarrier().getCarrier();
    	
    	GtsCarrierType carrierType = getUserSelection(carrier);
    	
    	if(carrier.getCarrierType().equals(carrierType)) return;
    	
    	carrier.setCarrierType(carrierType);
    	
    	drawing.getModel().updateCarrierType(carrier.getCarrierNumber(), carrierType.getId());

    }
    
    private GtsCarrierType getUserSelection(GtsCarrier carrier){
    	 
    	String scrapName = (String)JOptionPane.showInputDialog(
        		figure.getDrawing().getController().getWindow(),
                "Please set scrap",
                "Scrap Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[] {GtsCarrierType.NORMAL.getName(),GtsCarrierType.SCRAP.getName(),GtsCarrierType.SCRAP1.getName(),GtsCarrierType.SCRAP2.getName()},
                GtsCarrierType.NORMAL.getName());
       return getCarrierType(scrapName);
    	
    }
    
    private GtsCarrierType getCarrierType(String scrapName) {
    	for (GtsCarrierType type: GtsCarrierType.values()) {
    		if (type.getName().equalsIgnoreCase(scrapName)) return type;
    	}
    	return null;
    }
}
