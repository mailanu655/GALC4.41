package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawingView;


public class EditDecisionPointAction extends AbstractAction{
   
    private static final long serialVersionUID = 1L;
    
    public static final int EDIT_NAME = 0;
    public static final int EDIT_FONT = 1;
    public static final int EDIT_TEXT_COLOR = 2;
    public static final int EDIT_FILL_COLOR = 3;
    private GtsDrawingView view;
    
    public EditDecisionPointAction(GtsDrawingView view){
        this.view = view;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Configure Decision Points";
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        EditDecisionPointDialog dialog = new EditDecisionPointDialog(view);
        dialog.setVisible(true);
    }
}
