package com.honda.galc.client.gts.view;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.ToolEvent;
import org.jhotdraw.draw.ToolListener;

import com.honda.galc.client.gts.figure.GateFigure;
import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsShape;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>GtsEditSelectionTool</code> is to handle mouse movement event
 * so that the figure changes can be captured and be sent to the database
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
 * <TD>Mar 7, 2008</TD>
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

public class GtsEditSelectionTool extends GtsSelectionTool{

	private List<GtsLaneSegment> selectedLaneSegments = new ArrayList<GtsLaneSegment>();
    private List<GtsNode> selectedNodes = new ArrayList<GtsNode>();
    
    private List<GtsLabel> labels = new ArrayList<GtsLabel>();
    
    private List<GtsShape> shapes = new ArrayList<GtsShape>();
    
    public GtsEditSelectionTool(){
        super();
     }
    
    public void toolStarted(ToolEvent event) {
        this.forwardToolStarted(event);
    }
    
    private void forwardToolStarted(ToolEvent event){
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == ToolListener.class) {
               ((ToolListener)listeners[i+1]).toolStarted(event);
            }
        }
    }
    
    /**
     * when mouse is pressed, get the currently selected figures and save copies of the objects
     */
    
    public void mousePressed(MouseEvent evt) {
        
        super.mousePressed(evt);
        
        // clear previously saved values
        selectedLaneSegments.clear();
        selectedNodes.clear();
        labels.clear();
        shapes.clear();
        
        // save the currently selected layout objects
        for(Figure fig:getView().getSelectedFigures()){
            if(fig instanceof LaneSegmentFigure){
                selectedLaneSegments.add(((LaneSegmentFigure) fig).getLaneSegment().copy());
            };
            
            if(fig instanceof GateFigure){
            	selectedNodes.add(((GateFigure)fig).getNode().copy());
            }
            
            if(fig instanceof LabelFigure){
                labels.add(((LabelFigure)fig).getLabel().copy());
            }
            
            if(fig instanceof ShapeFigure){
                shapes.add(((ShapeFigure)fig).getShape().copy());
            }
        }
    } 
    
    
            
    /**
     * when mouse is released, compare the figure attributes and the save copies if there are any changes
     * update the change to database
     */
    
    public void mouseReleased(MouseEvent evt) {
        
        super.mouseReleased(evt);
        
        checkNodeChange();
        
        checkLabelChange();
        
        checkShapeChange();
        
        getDrawing().refreshLaneSegmentFigures();
        
    }
    
    
    private void checkNodeChange(){
        Map<Integer,GtsLaneSegment> changedLaneSegments = new HashMap<Integer,GtsLaneSegment>();
        
        Map<Integer,GtsNode> changedNodes = new HashMap<Integer,GtsNode>();
       
        for (GtsNode node:selectedNodes){
        	
        	GateFigure gateFigure = getDrawing().findGateFigure(node.getNodeId());
        	if(gateFigure != null && gateFigure.isChanged(node)) {
        		changedNodes.put(node.getNodeId(),gateFigure.getNode());
        	}
          }
       
       for(GtsLaneSegment segment:selectedLaneSegments){
           
           GtsLaneSegment figureLaneSegment= getDrawing().findLaneSegmentFigure(segment.getId().getLaneSegmentId()).getLaneSegment();
           
           if(figureLaneSegment.isChanged(segment)) {
               changedLaneSegments.put(segment.getId().getLaneSegmentId(),figureLaneSegment.copy());
               if(segment.getInputNode().isChanged(figureLaneSegment.getInputNode())) {
            	   changedNodes.put(figureLaneSegment.getInputNode().getNodeId(),figureLaneSegment.getInputNode());
               }
               
               if(segment.getOutputNode().isChanged(figureLaneSegment.getOutputNode())) {
            	   changedNodes.put(figureLaneSegment.getOutputNode().getNodeId(),figureLaneSegment.getOutputNode());
               }
           }
       }
       
       if(!changedNodes.isEmpty()) getModel().updateNodes(new ArrayList<GtsNode>(changedNodes.values()));
       if(!changedLaneSegments.isEmpty()){
           getModel().updateLaneSegments(new ArrayList<GtsLaneSegment>(changedLaneSegments.values()));
       }
       
       if(!changedNodes.isEmpty() || !changedLaneSegments.isEmpty())
    	   getDrawing().reInitializeLayout();
    }
    
    private void checkLabelChange(){
        for(GtsLabel label:labels){
            LabelFigure figure = getDrawing().findLabelFigure(label.getId().getLabelId());
            if(figure.getLabel().getX() != label.getX() || 
               figure.getLabel().getY() != label.getY() ||
               figure.getLabel().getWidth() != label.getWidth() ||
               figure.getLabel().getHeight() != label.getHeight()){
                 getModel().updateLabel(figure.getLabel().copy());
            }
        }
    }
    
    private void checkShapeChange(){
        for(GtsShape shape:shapes){
            ShapeFigure figure = getDrawing().findShapeFigure(shape.getId().getShapeId());
            if(figure.getShape().getX() != shape.getX() || 
               figure.getShape().getY() != shape.getY() ||
               figure.getShape().getWidth() != shape.getWidth() ||
               figure.getShape().getHeight() != shape.getHeight()||
               !figure.getShape().getOrientation().equals(shape.getOrientation())){
                 getModel().updateShape(figure.getShape().copy());
            }
        }
    }
    
    public GtsDrawing getDrawing(){
        return (GtsDrawing)super.getDrawing();
    }
    
    public GtsDrawingView getView(){
        return (GtsDrawingView)super.getView();
    }
    
    private GtsTrackingController getController(){
        return getView().getController();
    }
    
    private GtsTrackingModel getModel() {
    	return getController().getModel();
    }
        
}
