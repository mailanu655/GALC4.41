package com.honda.galc.client.gts.figure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;

import org.jhotdraw.draw.ArrowTip;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.FigureListener;
import org.jhotdraw.draw.Handle;
import org.jhotdraw.geom.BezierPath;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.LaneInfoWindow;
import com.honda.galc.client.gts.view.action.AddCarrierAction;
import com.honda.galc.client.gts.view.action.BezierDecorationHandle;
import com.honda.galc.client.gts.view.action.CloseLaneAction;
import com.honda.galc.client.gts.view.action.EditLaneSegmentAction;
import com.honda.galc.client.gts.view.action.LaneInfoAction;
import com.honda.galc.client.gts.view.action.NodeAction;
import com.honda.galc.client.gts.view.action.ReverseLaneAction;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LaneSegmentFigure</code> is an graphical object which
 * has 2 end points(nodes) and 2 control points. It is a 
 * special bezier curve (only have 2 control points).
 * When changing the position of the 2 control points, the shape
 * of the lane segment can be changed. So some basic line segment
 * shape(such as straight line, curve, arc) can be created. Multiple
 * lane segments connected at their nodes can form a lane. Multiple 
 * connected lanes thus form a area.<br>
 * In editing mode, user can select and move the lane segment's handles
 * (end points)and (control points) to change its shape. If the node of
 * a lane segment is moved, the shapes of all the lane segments which
 * are connected at this node will be changed accordingly. The change will
 * also be sent to server to be saved into the data base and be published
 * to the other clients.<br>
 * If the lane segment is powered the lane segment is displayed as solid line,
 * otherwise it is displayed as dashed line.
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
 * <TD>Feb 27, 2008</TD>
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

public class LaneSegmentFigure extends BezierFigure{

    private static final long serialVersionUID = 1L;
    
    private GtsLaneSegment laneSegment;
    
    private FigureListener figureListener;
    
    private List<GtsMove> currentMoves = new ArrayList<GtsMove>();
    
    
    public LaneSegmentFigure(GtsLaneSegment laneSegment,double laneSegmentWidth){
    	setLaneSegment(laneSegment, laneSegmentWidth);
    }
    
	
    
    public void setLaneSegment(GtsLaneSegment segment,double laneSegmentWidth){
        this.laneSegment = segment;
        if(figureListener != null) this.removeFigureListener(figureListener);
        setBounds(getLaneSegment().getStartPoint(),getLaneSegment().getEndPoint());
        setControlPoints(getLaneSegment().getControlPoint1(),getLaneSegment().getControlPoint2());
        setAttribute(AttributeKeys.STROKE_WIDTH, laneSegmentWidth);
        this.setPoweredFlag(getLaneSegment().isPowered());
        figureListener = new LaneSegmentFigureHandler(this);
        this.addFigureListener(figureListener);
    }
	
	public void setControlPoints(Point2D.Double cp1,Point2D.Double cp2){
		BezierPath.Node aNode = path.get(0);
		aNode.setControlPoint(2, cp1);
		aNode.setMask(BezierPath.C2_MASK);
		aNode = path.get(1);
		aNode.setControlPoint(1, cp2);
		aNode.setMask(BezierPath.C1_MASK);
	}
	
	public void setControlPoints(int cpx1,int cpy1, int cpx2, int cpy2){
		setControlPoints(new Point2D.Double(cpx1,cpy1), new Point2D.Double(cpx2,cpy2));
	}
	
	public Point2D.Double getControlPoint1(){
        BezierPath.Node aNode = path.get(0);
        return aNode.getControlPoint(2);
    }
    
    public Point2D.Double getControlPoint2(){
        BezierPath.Node aNode = path.get(1);
        return aNode.getControlPoint(1);
    }
    
    
    public void setPoweredFlag(boolean aFlag){
		if(aFlag)AttributeKeys.STROKE_DASHES.basicSet(this, null);
		else AttributeKeys.STROKE_DASHES.basicSet(this, new double[]{3.0});
 	}
	
    
    
	public double getLengthOfPath(){
		path.validatePath();
		return path.getLengthOfPath(3.0);
	}
	public Point2D.Double getPointOfPath(double relative){
		path.validatePath();
		return path.getPointOnPath(relative, 3.0);
	}
	
     public Collection<Handle> createHandles(int detailLevel) {
         if(GtsDrawing.isEditingMode()){
             Collection<Handle> handles = super.createHandles(detailLevel);
             handles.add(new BezierDecorationHandle(this,new ArrowTip(0.35, 6, 5.65)));
             return handles;
         }else{
             LinkedList<Handle> handles = new LinkedList<Handle>();
             return handles;
         }
     }
    
     /**
      * Handles a mouse click.
      */
     @Override 
     public boolean handleMouseClick(Point2D.Double p, MouseEvent evt, DrawingView view) {
         
         if(GtsDrawing.isEditingMode()) return false;
         
         if(getLaneSegment().getLane() != null){
            
             LaneInfoWindow laneInfoWindow = new LaneInfoWindow(getDrawing(),getLaneFromModel());
             laneInfoWindow.setVisible(true);
             
         }
         return false;
     }
    
    @Override public Collection<Action> getActions(Point2D.Double p) {
        
        LinkedList<Action> actions = new LinkedList<Action>();
        if(GtsDrawing.isProductionMode()){

            GtsLane lane = getLaneSegment().getLane();
        	//          only display add/remove carrier menu when the user has the authority
            if(lane != null){
                
                actions.add(new LaneInfoAction(getDrawing(),getLaneFromModel()));
                if(getDrawing().getModel().isUserControllable()){
                
                    // allow to add carrier only when this lane segment belongs to a lane
                    
                    actions.add(new AddCarrierAction(getDrawing(),getLaneFromModel(),0,2));
                    actions.add(new AddCarrierAction(getDrawing(),getLaneFromModel(),0,3));
                    
                    if(getDrawing().getModel().hasGates(lane.getLaneName()))
                       	actions.add(new CloseLaneAction(getDrawing(),lane,getDrawing().getModel().isLaneClosed(lane.getLaneName())));
                }
            }

        } else{
            
            if(getLaneSegment().isLaneEndPoint(p))actions.add(new NodeAction(this,p));
            
            actions.addAll(EditLaneSegmentAction.getActions(this, p,"Lane Segment"));
            actions.add(new ReverseLaneAction(this,p));
            
        }
        
        return actions;
    }
    
    public void refreshChange(){
        if (isChanged()){
            setBounds(getLaneSegment().getStartPoint(),getLaneSegment().getEndPoint());
            setControlPoints(getLaneSegment().getControlPoint1(),getLaneSegment().getControlPoint2());
            invalidate();
        }
    }
    
    public void refreshControlPointsChange(){
        setControlPoints(getLaneSegment().getControlPoint1(),getLaneSegment().getControlPoint2());
        invalidate();
    }
    
    private boolean isChanged(){
        return isStartPointChanged() || isEndPointChanged() || 
               isControlPoint1Changed() || isControlPoint2Changed();
                
    }
    
    private boolean isStartPointChanged(){
        return !this.getStartPoint().equals(getLaneSegment().getStartPoint());
    }
    
    private boolean isEndPointChanged(){
        return !this.getEndPoint().equals(getLaneSegment().getEndPoint());
    }
    
    private boolean isControlPoint1Changed(){
        return !this.getControlPoint1().equals(getLaneSegment().getControlPoint1());
    }
    
    private boolean isControlPoint2Changed(){
        return !this.getControlPoint2().equals(getLaneSegment().getControlPoint2());
    }
    
    
    /**
     * Transforms the figure.
     */
    public void transform(AffineTransform tx) {
        if(GtsDrawing.isEditingMode()) {
         	super.transform(tx);
        }
    }
    
    /**
     * Check if this lane segment has the input node or output node id = nodeId
     * @param nodeId
     * @return
     */
    
    public boolean isNodeId(int nodeId){
        return isInputNode(nodeId) || isOutputNode(nodeId);
    }
    
    public boolean isInputNode(int nodeId){
        return getLaneSegment().getInputNodeId() == nodeId;
    }
    
    public boolean isOutputNode(int nodeId){
        return getLaneSegment().getOutputNodeId() == nodeId;
    }
    
    public GtsDrawing getDrawing() {
        return (GtsDrawing)super.getDrawing();
    }
    
    /**
     * update the lane segment color based on the move status
     * @param move
     */
    
    public void updateMoveStatus(GtsMove move){
        
        if(move.isFinished()) {
        	currentMoves.remove(move);
        }else addMove(move);
        
        Color color = deriveLineColor();
        
        getLaneSegment().setLineColor(color);
        
        this.invalidate();
    }
    
    private void addMove(GtsMove move) {
    	for(GtsMove item : currentMoves) {
    		if(item.equals(move)) {
    			item.setMoveStatus(move.getMoveStatus());
    			return;
    		}
    	}
    	currentMoves.add(move);
    }
    
    private Color deriveLineColor() {

    	if(isMoveCreated()) return  Color.blue;
    	else if(!currentMoves.isEmpty()) return Color.red;
    	else return AttributeKeys.STROKE_COLOR.getDefaultValue();
    	
    }
    
    private boolean isMoveCreated() {
    	for(GtsMove move : currentMoves) {
    		if(move.isCreated()) return true;
    	}
    	return false;
    }
    
    public void updateLaneStatus(boolean status) {
    	Color color = status ? Color.red : AttributeKeys.STROKE_COLOR.getDefaultValue();
        getLaneSegment().setLineColor(color);
        this.invalidate();
    }
    
    
    protected void drawStroke(Graphics2D g) {
        g.setColor(getLaneSegment().getLineColor());
        super.drawStroke(g);
    }    
    
    public void updateLaneSegment(){
    	getLaneSegment().getInputNode().setX((int)getStartPoint().getX());
    	getLaneSegment().getInputNode().setY((int)getStartPoint().getY());
    	getLaneSegment().getOutputNode().setX((int)getEndPoint().getX());
    	getLaneSegment().getOutputNode().setY((int)getEndPoint().getY());
    	getLaneSegment().setControlPoint1((int)getControlPoint1().getX(), (int)getControlPoint1().getY());
    	getLaneSegment().setControlPoint2((int)getControlPoint2().getX(), (int)getControlPoint2().getY());
    }
    
    public GtsLaneSegment getLaneSegment() {
    	return laneSegment;
    }
    
    private class LaneSegmentFigureHandler extends FigureAdapter{
        private LaneSegmentFigure owner;
        
        public LaneSegmentFigureHandler(LaneSegmentFigure figure){
            this.owner = figure;
        }
        
        
        public void figureChanged(FigureEvent e) {
            owner.updateLaneSegment();
        }
    }
    
    GtsLane getLaneFromModel() {
    	String laneId = getLaneSegment().getLane().getLaneId();
    	return getDrawing().getModel().findLane(laneId);
    }

}
