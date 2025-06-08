package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawing;

public class ShowCarrierAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    public ShowCarrierAction(GtsDrawing drawing){
        this.drawing = drawing;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        if(drawing.getCarrierVisibleFlag()) 
            return "Hide Carriers";
        else return "Show Carriers";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        drawing.toggleCarrierVisibleFlag();
        this.putValue(Action.NAME, getName());
    }
}
