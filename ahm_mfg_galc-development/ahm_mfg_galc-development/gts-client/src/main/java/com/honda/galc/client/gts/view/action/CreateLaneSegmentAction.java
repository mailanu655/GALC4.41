package com.honda.galc.client.gts.view.action;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.GtsSelectionTool;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentId;

public class CreateLaneSegmentAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private GtsSelectionTool tool;
    
    public CreateLaneSegmentAction(GtsDrawing drawing, GtsSelectionTool tool){
        this.drawing = drawing;
        this.tool = tool;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Create Lane Segment";
    }
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        GtsLaneSegment newSegment = getDrawing().getModel().createLaneSegment(newLaneSegment());
        
        if(newSegment != null) {
        	getDrawing().getModel().laneSegmentAdded(newSegment);
        	LaneSegmentFigure figure = getDrawing().addLaneSegment(newSegment); 
	   		 getDrawing().refreshLayout();
	   		 
	   		 EditLaneSegmentDialog dialog = new EditLaneSegmentDialog(figure);
	         dialog.setVisible(true);
        }
    }
    
    private GtsLaneSegment newLaneSegment(){
    	GtsLaneSegment segment = new GtsLaneSegment();
    	GtsLaneSegmentId id = new GtsLaneSegmentId();
    	segment.setId(id);
    	segment.setInputNodeId(-1);
    	segment.setOutputNodeId(-1);
        segment.setControlPoint1((int)tool.getMousePoint().getX(), (int)tool.getMousePoint().getY());
        segment.setControlPoint2((int)tool.getMousePoint().getX()+ 50, (int)tool.getMousePoint().getY() + 50);
        return segment;
    }
    
//    private GtsNode getNode(int nodeId){
//        GtsArea area = getDrawing().getController().getArea();
//        GtsNode node = area.findNode(nodeId);
//        if(node == null){
//            node = getDrawing().getRequestInvoker().getNode(nodeId);
//            area.addNode(node);
//        }
//        return node;
//    }
    
    private GtsDrawing getDrawing(){
         return drawing;
    }
}
