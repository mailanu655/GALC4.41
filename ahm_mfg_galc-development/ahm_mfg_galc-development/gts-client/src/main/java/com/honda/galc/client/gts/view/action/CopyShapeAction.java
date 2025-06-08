package com.honda.galc.client.gts.view.action;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsShape;

public class CopyShapeAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private ShapeFigure figure;
    
    public CopyShapeAction(ShapeFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
//      this.setEnabled(false);
    }
    
    private String getName(){
         return "Copy Shape";
    }
    
 
    
    public void actionPerformed(ActionEvent e) {
        GtsShape newShape = figure.getShape().copy();
        newShape.getId().setShapeId(-1);
        newShape.setX(newShape.getX()+10);
        newShape.setY(newShape.getY()+10);
        newShape = getDrawing().getModel().createShape(newShape);
        getDrawing().add(new ShapeFigure(newShape));
    }
  
    public GtsDrawing getDrawing(){
        return figure.getDrawing();
    }

}
