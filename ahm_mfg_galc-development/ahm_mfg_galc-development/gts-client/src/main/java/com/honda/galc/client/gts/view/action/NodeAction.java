package com.honda.galc.client.gts.view.action;

import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jhotdraw.draw.Figure;


import com.honda.galc.client.gts.figure.GateFigure;
import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsNode;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>NodeAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 4, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class NodeAction extends AbstractAction{

    private static final long serialVersionUID = 1L;
    
    private Figure figure;
    private Point2D.Double point;
    
    
	public NodeAction(LaneSegmentFigure figure,Point2D.Double p){
        this.figure = figure;
        this.point = p;
        
		this.putValue(Action.NAME, getName());
	}
    
    public NodeAction(GateFigure figure,Point2D.Double p){
        this.figure = figure;
        this.point = p;
        
        this.putValue(Action.NAME, getName());
    }
	
    private GtsNode getNearestNode(){
        if(figure instanceof LaneSegmentFigure){
            return ((LaneSegmentFigure)figure).getLaneSegment().getNearestNode(point); 
        }else{
            return ((GateFigure)figure).getNode();
        }
    }
    
    private String getName(){
    	GtsNode node = getNearestNode();
 
        if(node.isVisible()) return "Disable Gate " + node.getNodeName();
        else {
            
            return "Enable Gate " + getGateName(node);
        }
    }
    
    
    private String getGateName(GtsNode node){
        String name;
        GtsLane lane = ((LaneSegmentFigure)figure).getLaneSegment().getLane();
        if(lane.isInputNode(node)){
            name = "ENTRY-" + lane.getLaneId();
        }else {
            name = "EXIT-" + lane.getLaneId();
        }
        return name;
    }
    
	public void actionPerformed(java.awt.event.ActionEvent e) {
	    GtsNode node = getNearestNode();
        if(node.isVisible()) {
            node.setVisible(false);
            node.setNodeName("");
        }else {
           node.setNodeName(getGateName(node));
           node.setVisible(true);
        }
 
        getDrawing().getModel().updateNode(node);
        getDrawing().updateGateFigure(node);
        
    }
    
    private GtsDrawing getDrawing(){
        if(figure instanceof LaneSegmentFigure){
            return ((LaneSegmentFigure)figure).getDrawing();
        }else return ((GateFigure)figure).getDrawing();
    }
		  
}
