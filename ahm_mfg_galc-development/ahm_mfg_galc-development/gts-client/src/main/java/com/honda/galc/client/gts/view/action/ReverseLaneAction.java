package com.honda.galc.client.gts.view.action;

import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsLane;


public class ReverseLaneAction extends AbstractAction{

    private static final long serialVersionUID = 1L;
    
    private LaneSegmentFigure figure;
    
    
	public ReverseLaneAction(LaneSegmentFigure figure,Point2D.Double p){
        this.figure = figure;
        
		this.putValue(Action.NAME, getName());
//		this.setEnabled(false);
	}
    
    private String getName(){
        return "Reverse Lane Direction";
    }
    
    
	public void actionPerformed(java.awt.event.ActionEvent e) {
        GtsLane lane = figure.getLaneSegment().getLane();
        if(lane == null) return;
        getDrawing().getModel().reverseLaneSegment(lane);
        getDrawing().refreshLaneSegmentFigures();
        getDrawing().initCarriers();
    }
    
    
    
    private GtsDrawing getDrawing(){
         return ((LaneSegmentFigure)figure).getDrawing();
    }
    
    
		  
}
