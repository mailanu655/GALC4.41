package com.honda.galc.entity.gts;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsLaneType;
import com.honda.galc.entity.enumtype.GtsOrientation;
/**
 * 
 * 
 * <h3>GtsLaneSegment Class description</h3>
 * <p> GtsLaneSegment description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_LANE_SEGMENT_TBX")
public class GtsLaneSegment extends AuditEntry {
	@EmbeddedId
	private GtsLaneSegmentId id;

	@Column(name="LANE_SEGMENT_NAME")
	private String laneSegmentName;

	@Column(name="LANE_SEGMENT_CAPACITY")
	private int capacity;

	private int powered = 1;

	@Column(name="INPUT_NODE_ID")
	private int inputNodeId;

	@Column(name="OUTPUT_NODE_ID")
	private int outputNodeId;

	@Column(name="INPUT_CPX")
	private int inputCpx;

	@Column(name="INPUT_CPY")
	private int inputCpy;

	@Column(name="OUTPUT_CPX")
	private int outputCpx;

	@Column(name="OUTPUT_CPY")
	private int outputCpy;

	@Column(name="CARRIER_VISIBLE")
	private int carrierVisible = 1;

	@Column(name="IS_SOURCE")
	private int isSource = 1;

	@Column(name="IS_DESTINATION")
	private int isDestination = 0;
	
	@Transient
	private Color lineColor = Color.black;
	
	@Transient
	private GtsNode outputNode;

	@Transient
	private GtsNode inputNode;

	@Transient
	private List<GtsLaneSegmentMap> laneSegmentMaps;

	private static final long serialVersionUID = 1L;
	
	public GtsLaneSegment(GtsLaneSegment laneSegment) {
		this.id = new GtsLaneSegmentId(laneSegment.getId());
		this.laneSegmentName = laneSegment.laneSegmentName;
		this.capacity = laneSegment.capacity;
		this.powered = laneSegment.powered;
		this.inputCpx = laneSegment.inputCpx;
		this.inputCpy = laneSegment.inputCpy;
		this.outputCpx = laneSegment.outputCpx;
		this.outputCpy = laneSegment.outputCpy;
		this.carrierVisible = laneSegment.carrierVisible;
		this.isSource = laneSegment.isSource;
		this.isDestination = laneSegment.isDestination;
		this.inputNodeId = laneSegment.inputNodeId;
		this.outputNodeId = laneSegment.outputNodeId;
		this.inputNode = laneSegment.inputNode.copy();
		this.outputNode = laneSegment.outputNode.copy();
		this.lineColor = laneSegment.lineColor;
		this.setCreateTimestamp(laneSegment.getCreateTimestamp());
		this.setUpdateTimestamp(laneSegment.getUpdateTimestamp());
	}
	
	public GtsLaneSegment() {
		super();
	}

	public GtsLaneSegmentId getId() {
		return this.id;
	}

	public void setId(GtsLaneSegmentId id) {
		this.id = id;
	}

	public String getLaneSegmentName() {
		return this.laneSegmentName;
	}

	public void setLaneSegmentName(String laneSegmentName) {
		this.laneSegmentName = laneSegmentName;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getInputNodeId() {
		return inputNodeId;
	}

	public void setInputNodeId(int inputNodeId) {
		this.inputNodeId = inputNodeId;
	}

	public int getOutputNodeId() {
		return outputNodeId;
	}

	public void setOutputNodeId(int outputNodeId) {
		this.outputNodeId = outputNodeId;
	}

	public int getPowered() {
		return this.powered;
	}
	
	public boolean isPowered() {
		return getPowered() == 1;
	}

	public void setPowered(int powered) {
		this.powered = powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered ? 1:0;
	}
	
	public int getInputCpx() {
		return this.inputCpx;
	}

	public void setInputCpx(int inputCpx) {
		this.inputCpx = inputCpx;
	}

	public int getInputCpy() {
		return this.inputCpy;
	}

	public void setInputCpy(int inputCpy) {
		this.inputCpy = inputCpy;
	}

	public int getOutputCpx() {
		return this.outputCpx;
	}

	public void setOutputCpx(int outputCpx) {
		this.outputCpx = outputCpx;
	}

	public int getOutputCpy() {
		return this.outputCpy;
	}

	public void setOutputCpy(int outputCpy) {
		this.outputCpy = outputCpy;
	}

	public int getCarrierVisible() {
		return this.carrierVisible;
	}
	
	public boolean isCarrierVisible() {
		return getCarrierVisible() > 0;
	}

	public void setCarrierVisible(int carrierVisible) {
		this.carrierVisible = carrierVisible;
	}
	
	public void setCarrierVisible(boolean carrierVisible) {
		this.carrierVisible = carrierVisible ? 1:0;
	}

	public boolean isSource() {
		return this.isSource > 0;
	}

	public void setSource(int isSource) {
		this.isSource = isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource ? 1:0;
	}

	public boolean isDestination() {
		return this.isDestination > 0;
	}

	public void setDestination(int isDestination) {
		this.isDestination = isDestination;
	}
	
	public void setDestination(boolean isDestination) {
		this.isDestination = isDestination ? 1:0;
	}

	public GtsNode getInputNode() {
		return this.inputNode;
	}
	
	public void setInputNode(GtsNode inputNode) {
		this.inputNode = inputNode;
	}

	public GtsNode getOutputNode() {
		return this.outputNode;
	}

	public void setOutputNode(GtsNode outputNode) {
		this.outputNode = outputNode;
	}
	
	public double getStartX(){
		return inputNode.getX();
	}
	
	public double getStartY(){
		return inputNode.getY();
	}
	
	public Point2D.Double getStartPoint(){
		return new Point2D.Double(getStartX(),getStartY());
	}
	
	public double getEndX(){
		return outputNode.getX();
	}
	
	public double getEndY(){
		return outputNode.getY();
	}
	
	public void setControlPoint1(int x, int y){
        this.inputCpx = x;
        this.inputCpy = y;
    }
    
	public int getControlPointX1(){
		return inputCpx;
	}
    
    public void setControlPointX1(int value){
    	inputCpx = value;
    }
	
	public int getControlPointY1(){
		return inputCpy;
	}
	
    public void setControlPointY1(int value){
    	inputCpy = value;
    }
    
	public Point2D.Double getControlPoint1(){
           return new Point2D.Double(inputCpx,inputCpy);
	}
	
	public int getControlPointX2(){
		return outputCpx;
	}
	
    public void setControlPointX2(int value){
    	outputCpx = value;
    }
    
	public int getControlPointY2(){
		return outputCpy;
	}
    
     public void setControlPointY2(int value){
    	 outputCpy = value;
     }
	
    public void setControlPoint2(int x, int y){
        this.outputCpx = x;
        this.outputCpy = y;
    }
    
    public Point2D.Double getControlPoint2(){
        return new Point2D.Double(outputCpx,outputCpy);
	}
	
    public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Point2D.Double getEndPoint(){
        return new Point2D.Double(getEndX(),getEndY());
	}
	
	public void makeVertical(int value){
        this.getInputNode().setX(value);
        this.getOutputNode().setX(value);
        this.inputCpx = value;
        this.outputCpx = value;
    }
    
    public void makeHorizontal(int value){
        this.getInputNode().setY(value);
        this.getOutputNode().setY(value);
        this.inputCpy = value;
        this.outputCpy = value;
    }
    
    public boolean isChanged(GtsLaneSegment laneSegment) {
    	if(this.getId().getLaneSegmentId() != laneSegment.getId().getLaneSegmentId()) return false;
    	if(this.getInputNodeId() != laneSegment.getInputNodeId()) return false;
    	if(this.getOutputNodeId() != laneSegment.getOutputNodeId()) return false;
    	
    	return getInputCpx()  != laneSegment.getInputCpx() ||
               getInputCpy()  != laneSegment.getInputCpy() ||
               getOutputCpx() != laneSegment.getOutputCpx() ||
               getOutputCpy() != laneSegment.getOutputCpy() ||
               getInputNode().distance(laneSegment.getInputNode()) > 0.0 ||
               getOutputNode().distance(laneSegment.getOutputNode()) > 0.0;
    }
	
	 /**
    * Returns a relative point on the path.
    * Where 0 is the start point of the path and 1 is the end point of the
    * path.
    *
    * @param t a value between 0 and 1.
    * @return coordinate(x,y)
    */
 
	public Point2D.Double getPointOnPath(double t){
		
		if(t <=0.0)return this.getStartPoint();
		if(t >=1.0)return this.getEndPoint();
		
		return new Point2D.Double(
			(1-t)*(1-t)*(1-t)*getStartX() + 3.0*t*(1-t)*(1-t)*inputCpx + 3*t*t*(1-t)*outputCpx + t*t*t*getEndX(),
			(1-t)*(1-t)*(1-t)*getStartY() + 3.0*t*(1-t)*(1-t)*inputCpy + 3*t*t*(1-t)*outputCpy + t*t*t*getEndY());
	}
	
	/**
     * get the direction of the input node
     * @return direction EAST,SOUTH,WEST,NORTH
     */
    
	public GtsOrientation getInputDirection(){
		Point2D.Double cp = getPointOnPath(0.1);
		return getDirection(cp.x - getStartX(), cp.y - getStartY());
	}
    
    /**
     * get the direction of the output node
     * @return direction EAST,SOUTH,WEST,NORTH
     */
    
	public GtsOrientation getOutputDirection(){
		Point2D.Double cp = getPointOnPath(0.9);
		return getDirection(getEndX() - cp.x, getEndY() - cp.y);
	}
	
	/**
     * get the horizontal direction of the line segment
     * determined by the end point and start point's coordinates 
     * @return EAST or WEST direction
	 */
    public GtsOrientation getDirection(){
	    if(getEndX() >= getStartX()) return GtsOrientation.EAST;
        else return GtsOrientation.WEST;
    }
	
	/**
     * get the direction of the point(x,y) to point(0,0)
     * @param x
     * @param y
     * @return direction - EAST,SOUTH,WEST,NORTH
     */
    
    private GtsOrientation getDirection(double x,double y){
		double degree =Math.toDegrees(Math.atan2(x, y));
		if (degree > -45.0 && degree <= 45.0) return GtsOrientation.SOUTH;
		else if (degree >45.0 && degree <=135.0) return GtsOrientation.EAST;
		else if (degree > -135.0 && degree<= -45.0)return GtsOrientation.WEST;
		return GtsOrientation.NORTH;
	}
    
    /**
     * 
     * @param pos
     * @return
     */
    
    public GtsOrientation getDirectionForPosition(int pos){
        return getDirection(getParametricValue(pos));
    }
    
    /**
     * reverse the direction of the lane segment
     *
     */
    
    public void reverseDirection(){
        // swap input node id and output node id;
        int nodeId = this.inputNodeId;
        this.inputNodeId = this.outputNodeId;
        this.outputNodeId = nodeId;
        // swap input node and output node
        GtsNode node = this.inputNode;
        this.inputNode = this.outputNode;
        this.outputNode = node;
        // swap cpx1 and cpx2
        int cpx1 = this.inputCpx;
        this.inputCpx = this.outputCpx;
        this.outputCpx = cpx1;
        // swap cpy1 and cpy2;
        int cpy1 = this.inputCpy;
        this.inputCpy = this.outputCpy;
        this.outputCpy = cpy1;
    }
    
    /**
     * calculate the parametric value from a position pos
     * parametric value is parameter t of the bezier path
     * t is between 0 and 1
     * @param pos (between 1 and capacity)
     * @return
     */
    
    private double getParametricValue(int pos){
        return 1 - (2.0 * pos - 1.0) /(capacity * 2.0);
    }
    
    /*
     * get the direction at point with parameter value t
     */
    
    public GtsOrientation getDirection(double t){
        
        if(t <= 0) t = 0;
        if(t >= 1.0) t = 1.0;
        
        // get directive at point t
        double dx = 3.0*(inputCpx-getStartX()) + 6*t*(getStartX()+outputCpx - 2.0*inputCpx) + 
                    3*t*t*(getEndX() - getStartX() + 3.0*inputCpx - 3.0*outputCpx);
        double dy = 3.0*(inputCpy-getStartY()) + 6*t*(getStartY()+outputCpy - 2.0*inputCpy) + 
                    3*t*t*(getEndY() - getStartY() + 3.0*inputCpy - 3.0*outputCpy);
        
        return getDirection(dx,dy);
        
    }
    
    /**
     * get the coordinate point from the carrier's position on this lane segment
     * assume the "pos" is less than our capacity 
     * @param pos - carrier position
     * @return coordinate point
	 */
    
	public Point2D.Double getPointForPosition(int pos){
        
        return getPointOnPath(getParametricValue(pos));
	}
    

	public List<GtsLaneSegmentMap> getLaneSegmentMaps() {
		if(this.laneSegmentMaps == null)
			laneSegmentMaps = new ArrayList<GtsLaneSegmentMap>();
		return this.laneSegmentMaps;
	}
	
	public GtsLaneSegmentMap getLaneSegmentMap(String laneId) {
		for(GtsLaneSegmentMap laneSegmentMap : this.getLaneSegmentMaps()) {
			GtsLane lane = laneSegmentMap.getLane();
			if(lane != null && lane.getLaneId().equals(laneId)) return laneSegmentMap;
		}
		return null;
	}

	public void setLaneSegmentMaps(List<GtsLaneSegmentMap> laneSegmentMaps) {
		this.laneSegmentMaps = laneSegmentMaps;
	}
	
	public void addLaneSegmentMap(GtsLaneSegmentMap laneSegmentMap) {
		this.getLaneSegmentMaps().add(laneSegmentMap);
	}
	
	public void removeLaneSegmentMap(GtsLaneSegmentMap laneSegmentMap) {
		this.getLaneSegmentMaps().remove(laneSegmentMap);
	}
	public GtsNode getNearestNode(Point2D.Double point){
        if(getInputNode().distance(point)< getOutputNode().distance(point)) return getInputNode();
        else return getOutputNode();
    }
	
	public GtsLane getLane(String laneId) {
		
		for(GtsLaneSegmentMap laneSegmentMap : this.getLaneSegmentMaps()) {
			GtsLane lane = laneSegmentMap.getLane();
			if(lane != null && lane.getLaneId().equals(laneId)) return lane;
		}
		return null;
	}
	
	public GtsLane getLane() {
		for(GtsLaneSegmentMap laneSegmentMap : this.getLaneSegmentMaps()) {
			GtsLane lane = laneSegmentMap.getLane();
			if(lane != null && !lane.getLaneType().equals(GtsLaneType.MOVE_PROGRESS)) return lane;
		}
		return null;
	}
	
	public boolean isLaneEndPoint(Point2D.Double point){
        if(getLane() == null) return false;
        if(getLane().getInputNode().equals(getNearestNode(point))) return true;
        if(getLane().getOutputNode().equals(this.getNearestNode(point))) return true;
        return false;
    }
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getLaneSegmentId(),getInputCpx(),
				getInputCpy(),getOutputCpx(),getOutputCpy(),isSource(),isDestination());
		
	}
	
	public GtsLaneSegment copy() {
		return new GtsLaneSegment(this);
	}

}
