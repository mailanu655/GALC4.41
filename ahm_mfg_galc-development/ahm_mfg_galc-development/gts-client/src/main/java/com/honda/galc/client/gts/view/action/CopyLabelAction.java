package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsLabel;

public class CopyLabelAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private LabelFigure figure;
    
    public CopyLabelAction(LabelFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
         return "Copy Label";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        GtsLabel newLabel = figure.getLabel().copy();
        newLabel.getId().setLabelId(-1);
        newLabel.setX(newLabel.getX()+10);
        newLabel.setY(newLabel.getY()+10);
        GtsLabel label = getDrawing().getModel().createLabel(newLabel);
        getDrawing().add(new LabelFigure(label));
    }
  
    public GtsDrawing getDrawing(){
        return figure.getDrawing();
    }
}
