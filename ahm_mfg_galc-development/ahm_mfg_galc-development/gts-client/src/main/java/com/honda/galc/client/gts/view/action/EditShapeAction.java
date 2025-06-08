package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.ShapeFigure;

public class EditShapeAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;

    private ShapeFigure figure;
    
    public EditShapeAction(ShapeFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Edit Shape";
    }
    
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        EditShapeDialog dialog = new EditShapeDialog(figure);
        dialog.setVisible(true);
    }
  

}
