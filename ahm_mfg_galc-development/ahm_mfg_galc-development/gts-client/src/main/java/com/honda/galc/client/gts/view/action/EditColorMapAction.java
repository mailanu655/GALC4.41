package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawingView;


public class EditColorMapAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	private GtsDrawingView view;
    
    public EditColorMapAction(GtsDrawingView view){
        this.view = view;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Edit Color Map";
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
    	
    	if(!view.getDrawing().isEditingMode() && !view.getDrawing().login()) return;
    	
    	EditColorMapDialog dialog = new EditColorMapDialog(view);
        dialog.setVisible(true);
    }
}
