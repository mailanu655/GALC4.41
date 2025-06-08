package com.honda.galc.client.gts.view.action;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jhotdraw.app.action.Actions;

import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.GtsTrackingModel;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentId;
import com.honda.galc.entity.gts.GtsNode;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>EditLaneSegmentAction</code> is a class to handle the user menu selection
 * to change the lane segment's properties. The user requests it include are,<br>
 * Edit the lane segment - user can change the name, powered flag, source,destination flag etc.<br>
 * Add a segment - add a new segment which connects the closes node of the current segment<br>
 * Remove a segment - remove the current segment<br>
 * Reverse segment direction - swap the nodes and control points to reverse the segment's direction<br>
 * Make segment horizontal - change the nodes and control points' y values to the same to make it horizontal<br>
 * Make segment vertical - change the nodes and control points' x values to the same to make it vertical<br>
 * Make segment uniform - change the segment's control points to its middle points. This makes the products 
 * evenly located along the segment<br>
 * Connect segment - connect the current segment to a nearest node of another lane segment<br>
 * Disconnect segment - disconnect the current segment to other lane segments<br>
 * 
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
 * <TD>Mar 5, 2008</TD>
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

public class EditLaneSegmentAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    public static final int EDIT_SEGMENT = 0;
    public static final int ADD_SEGMENT = 1;
    public static final int REMOVE_SEGMENT = 2;
    public static final int REVERSE_SEGMENT = 3;
    public static final int MAKE_HORIZONTAL = 4;
    public static final int MAKE_VERTICAL = 5;
    public static final int MAKE_EVEN = 6;
    public static final int CONNECT_NODE = 7;
    public static final int DISCONNECT_NODE = 8;
    
    private LaneSegmentFigure figure;
    private Point2D.Double point; 
    private int type = EDIT_SEGMENT;
    private static String[] names = {"Edit Segment","Add Segment","Remove Segment","Reverse Segment Direction",
                              "Make Horizontal","Make Vertical","Make Even",
                              "Connect Nearest Node","Disconnect Node"};
    
    
    public EditLaneSegmentAction(LaneSegmentFigure figure,Point2D.Double point,int type){
        this.figure = figure;
        this.point = point;
        if(type >= 0 && type < names.length)this.type = type;
        this.putValue(Action.NAME, getName());
    }
    
    public EditLaneSegmentAction(LaneSegmentFigure figure,Point2D.Double point,int type,String submenuKey){
        this(figure,point,type);
        this.putValue(Actions.SUBMENU_KEY, submenuKey);
    }
        
    
    public static Collection<Action> getActions(LaneSegmentFigure figure,Point2D.Double point,String submenuKey){
        LinkedList<Action> actions = new LinkedList<Action>();
        for(int i=0;i<names.length;i++){
            actions.add(new EditLaneSegmentAction(figure,point,i,submenuKey));
        }
        return actions;
    }
    
    
    private String getName(){
        return names[type];
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        switch(type){
            case EDIT_SEGMENT: 
                editSegment();
                break;

            case ADD_SEGMENT: 
                addSegment();
                break;
            case REMOVE_SEGMENT: 
                removeSegment();
                break;    
            case REVERSE_SEGMENT: 
                reverseSegment();
                break;    
          case MAKE_HORIZONTAL:
                makeHorizontal();
                break;
            case MAKE_VERTICAL:
                makeVertical();
                break;
            case MAKE_EVEN:
                makeEven();
                break;
            case CONNECT_NODE:
                connectNode();
                break;
            case DISCONNECT_NODE:
                disconnectNode();
                break;
        }
    }
    
    private void editSegment(){
        EditLaneSegmentDialog dialog = new EditLaneSegmentDialog(figure);
        dialog.setVisible(true);
    }
    
    private void addSegment(){
        GtsLaneSegment laneSegment = getModel().createLaneSegment(newLaneSegment());
        getDrawing().getController().laneSegmentCreated(laneSegment);
    }
    
    private void removeSegment(){
        getModel().removeLaneSegment(figure.getLaneSegment());
        getDrawing().remove(figure);
    }
    
    private void reverseSegment(){
    	GtsLaneSegment segment = figure.getLaneSegment().copy();
        segment.reverseDirection();
        
        getModel().updateLaneSegment(segment.copy());
        figure.getLaneSegment().reverseDirection();
        figure.refreshChange();
        if(segment.getInputNode().isVisible() || segment.getOutputNode().isVisible()) {
           getDrawing().refreshGateFigures();
        }
        getDrawing().initCarriers();
    }
    
    
    private void makeHorizontal(){
    	GtsLaneSegment segment = figure.getLaneSegment();
        GtsNode node = segment.getNearestNode(point);
        segment.makeHorizontal(node.getY());
        List<GtsNode> nodes = new ArrayList<GtsNode>();
        nodes.add(segment.getInputNode());
        nodes.add(segment.getOutputNode());
        getModel().updateNodes(nodes);
        getModel().updateLaneSegment(segment.copy());
        getDrawing().refreshLayout();
    }
    
    private void makeVertical(){
    	GtsLaneSegment segment = figure.getLaneSegment();
        GtsNode node = segment.getNearestNode(point);
        segment.makeVertical(node.getX());
        List<GtsNode> nodes = new ArrayList<GtsNode>();
        nodes.add(segment.getInputNode());
        nodes.add(segment.getOutputNode());
        getModel().updateNodes(nodes);
        getModel().updateLaneSegment(segment.copy());
        getDrawing().refreshLayout();
    }
    
    private void makeEven(){
        GtsLaneSegment segment = figure.getLaneSegment();
        double cpx1 = (segment.getInputNode().getX()*2.0 + segment.getOutputNode().getX())/3.0;
        double cpy1 = (segment.getInputNode().getY()*2.0 + segment.getOutputNode().getY())/3.0;
        double cpx2 = (segment.getInputNode().getX() + segment.getOutputNode().getX()*2.0)/3.0;
        double cpy2 = (segment.getInputNode().getY() + segment.getOutputNode().getY()*2.0)/3.0;
        segment.setControlPoint1((int)cpx1, (int)cpy1);
        segment.setControlPoint2((int)cpx2, (int)cpy2);
        getModel().updateLaneSegment(segment);
        figure.refreshControlPointsChange();
        getDrawing().initCarriers();
    }
    
    private void connectNode(){
    	GtsLaneSegment segment = figure.getLaneSegment();
        GtsNode node = segment.getNearestNode(point);
        GtsNode newNode = getModel().getNearestNode(node);
        if(newNode == null) return;
        getModel().updateLaneSegmentNode(segment,node.getNodeId(), newNode.getNodeId());
        
        if(segment.getInputNodeId() == node.getNodeId()) {
        	segment.setInputNodeId(newNode.getNodeId());
        	segment.setInputNode(newNode);
        }else if(segment.getOutputNodeId() == node.getNodeId()) {
        	segment.setOutputNodeId(newNode.getNodeId());
        	segment.setOutputNode(newNode);
        }
        
        getModel().removeNode(node);
        getModel().addNode(newNode);
        
        getDrawing().refreshLayout();

    }
    
    private void disconnectNode(){
        GtsLaneSegment segment = figure.getLaneSegment();
        GtsNode node = segment.getNearestNode(point);
        
        GtsNode newNode = node.copy();
        newNode.setX(node.getX() + 10);
        newNode.setY(node.getY() + 10);
        newNode = getModel().createNode(newNode);
        
        if(node.getNodeId() == segment.getInputNodeId()) {
        	segment.setInputNodeId(newNode.getNodeId());
        	segment.setInputNode(newNode);
        }else {
        	segment.setOutputNodeId(newNode.getNodeId());
        	segment.setOutputNode(newNode);
        }
        getModel().updateLaneSegment(segment);
        getDrawing().refreshLayout();
    }
    
    private GtsLaneSegment newLaneSegment(){
        GtsLaneSegment segment = new GtsLaneSegment();
        GtsLaneSegmentId id = new GtsLaneSegmentId();
        segment.setId(id);
        
        if(figure == null){
            segment.setInputNodeId(-1);
            segment.setOutputNodeId(-1);
            return segment;
        }
        
        GtsNode inNode = figure.getLaneSegment().getInputNode();
        GtsNode outNode = figure.getLaneSegment().getOutputNode();
        if(inNode.distance(point)< outNode.distance(point)){
            segment.setInputNodeId(inNode.getId().getNodeId());
            segment.setInputNode(inNode);
            segment.setControlPoint1(inNode.getX(),inNode.getY());
            segment.setOutputNodeId(-1);
            segment.setControlPoint2(inNode.getX() + 50,inNode.getY()+ 50);
        }else {
        	segment.setOutputNodeId(outNode.getId().getNodeId());
        	segment.setOutputNode(outNode);
            segment.setInputNodeId(-1);
            segment.setControlPoint1(outNode.getX()+ 50,outNode.getY() + 50);
            segment.setControlPoint2(outNode.getX(),outNode.getY());
        }
        return segment;
    }
    
    private GtsDrawing getDrawing(){
        return ((LaneSegmentFigure)figure).getDrawing();
   }
    
    private GtsTrackingModel getModel() {
    	return getDrawing().getModel();
    }
}
