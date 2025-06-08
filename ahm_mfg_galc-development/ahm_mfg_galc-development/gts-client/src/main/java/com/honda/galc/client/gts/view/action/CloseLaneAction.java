package com.honda.galc.client.gts.view.action;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsLane;

public class CloseLaneAction extends AbstractAction{

    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private GtsLane lane;
    private boolean isClosed;
    
    public CloseLaneAction(GtsDrawing drawing,GtsLane lane,boolean isClosed){
        this.drawing = drawing;
        this.lane = lane;
        this.isClosed = isClosed;
        this.putValue(Action.NAME, isClosed ? "Open Lane " : "Close Lane");
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
   
        if(isClosed) drawing.openLane(lane);
        else drawing.closeLane(lane);
         
    }
    
}
