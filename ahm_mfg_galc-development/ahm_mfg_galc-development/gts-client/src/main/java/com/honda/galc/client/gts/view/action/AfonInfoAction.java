package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawing;

/**
 * 
 * @author hcm_adm_008925
 *
 */

public class AfonInfoAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
     
    public AfonInfoAction(GtsDrawing drawing){
    	this.drawing = drawing;
         this.putValue(Action.NAME, "Near AFON Vins");
    }
 
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	AfonInforDialog.openDialog(drawing.getModel());
  	}
}
