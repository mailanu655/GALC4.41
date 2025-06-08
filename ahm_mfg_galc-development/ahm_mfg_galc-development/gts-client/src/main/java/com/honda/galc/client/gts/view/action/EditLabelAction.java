package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LabelFigure;

public class EditLabelAction extends AbstractAction{
       
    private static final long serialVersionUID = 1L;
    
    private LabelFigure figure;
    
    public EditLabelAction(LabelFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Edit Label";
    }
    
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        EditLabelDialog dialog = new EditLabelDialog(figure);
        dialog.setVisible(true);
    }
  
    

}
