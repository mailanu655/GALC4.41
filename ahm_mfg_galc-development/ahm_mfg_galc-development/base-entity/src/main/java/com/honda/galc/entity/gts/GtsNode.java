package com.honda.galc.entity.gts;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsNodeStatus;
import com.honda.galc.entity.enumtype.GtsOrientation;

/**
 * 
 * 
 * <h3>GtsNode Class description</h3>
 * <p> GtsNode description </p>
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
@Table(name="GTS_NODE_TBX")
public class GtsNode extends AuditEntry {
	@EmbeddedId
	private GtsNodeId id;

	@Column(name="NODE_NAME")
	private String nodeName;

	private int status;

	@Column(name="NODE_TYPE")
	private int nodeType;

	@Column(name="IS_VISIBLE")
	private int isVisible;

	@Column(name="IS_USER_SETTABLE")
	private int isUserSettable;

	private int x;

	private int y;

	@Transient
	private List<GtsLaneSegment> inputLaneSegments = new ArrayList<GtsLaneSegment>();

	@Transient
	private List<GtsLaneSegment> outputLaneSegments= new ArrayList<GtsLaneSegment>();

	private static final long serialVersionUID = 1L;

	
	public GtsNode() {
		super();
	}

	public GtsNode(GtsNode node) {
		this();
		this.id = new GtsNodeId(node.getId());
		this.nodeName = node.nodeName;
		this.status = node.status;
		this.nodeType = node.nodeType;
		this.isVisible = node.isVisible;
		this.isUserSettable = node.isUserSettable;
		this.x = node.x;
		this.y = node.y;
		this.setCreateTimestamp(node.getCreateTimestamp());
		this.setUpdateTimestamp(node.getUpdateTimestamp());
	}
	
	public GtsNode(String trackingArea,int nodeId) {
		this.id = new GtsNodeId(trackingArea,nodeId);
	}

	public GtsNodeId getId() {
		return this.id;
	}

	public void setId(GtsNodeId id) {
		this.id = id;
	}
	
	public int getNodeId() {
		return id.getNodeId();
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getStatus() {
		return this.status;
	}
	
	public boolean isGateOpen() {
		return status != GtsNodeStatus.CLOSE.getId() 
				&& status != GtsNodeStatus.LANE_CLOSED.getId();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setGateStatus(boolean gateStatus) {
		this.status = gateStatus ? 1:0;
	}
	
	public void closeLane() {
		this.status = GtsNodeStatus.LANE_CLOSED.getId();
	}
	
	public boolean isLaneClosed() {
		return this.status == GtsNodeStatus.LANE_CLOSED.getId();
	}
	
	public int getNodeType() {
		return this.nodeType;
	}
	
	public boolean isPLCGate() {
		return isGate() && nodeType == 2;
	}
	
	public boolean isNegativePLCGate() {
		return isGate() && nodeType == 3;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public boolean isVisible() {
		return this.isVisible > 0;
	}
	
	public boolean isGate() {
		return isVisible();
	}

	public void setVisible(int isVisible) {
		this.isVisible = isVisible;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible ? 1: 0;
	}

	public boolean isUserSettable() {
		return this.isUserSettable > 0;
	}

	public void setIsUserSettable(int isUserSettable) {
		this.isUserSettable = isUserSettable;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * get the direction based on the last element of the input and output lane segments
	 *
	 * @param		none
	 * @return		the direction of the node (EAST,SOUTH,WEST and NORTH)
	 *
	 */
	
	public GtsOrientation getDirection(){
		if (outputLaneSegments != null && !outputLaneSegments.isEmpty()) return outputLaneSegments.get(0).getInputDirection();
		if (inputLaneSegments != null &&!inputLaneSegments.isEmpty()) return inputLaneSegments.get(0).getOutputDirection();
		return GtsOrientation.EAST;
	}
	

    /**
     * check if the node is a gate of lane laneName
     * @param laneName
     * @return
     */
    
    public boolean isGateOfLane(String laneName){
        if(nodeName == null || laneName == null) return false;
        String[] tokens = getTokens();
        if(tokens == null || tokens.length != 2) return false;
        if(!tokens[0].equalsIgnoreCase("ENTRY") && !tokens[0].equalsIgnoreCase("EXIT")) return false;
        return tokens[1].equals(laneName);
        
    }
    
    public String getLaneName() {
    	if(nodeName == null) return "";
    	String[] tokens = getTokens();
        if(tokens == null || tokens.length != 2) return "";
        if(tokens[0].equalsIgnoreCase("ENTRY") || tokens[0].equalsIgnoreCase("EXIT")) return tokens[1];
        return "";
    }
    
    private String[] getTokens(){
        if(nodeName == null) return null;
        StringTokenizer st = new StringTokenizer(nodeName, Delimiter.HYPHEN);
        String[] tokens = new String[st.countTokens()];
        for(int i=0; st.hasMoreTokens();i++){
            tokens[i] = st.nextToken();
        }
        return tokens;
    }

	public List<GtsLaneSegment> getInputLaneSegments() {
		if(this.inputLaneSegments == null) this.inputLaneSegments = new ArrayList<GtsLaneSegment>();
		return this.inputLaneSegments;
	}

	public void setInputLaneSegments(List<GtsLaneSegment> inputLaneSegments) {
		this.inputLaneSegments = inputLaneSegments;
	}
	
	public void addInputLaneSegment(GtsLaneSegment laneSegment) {
		this.getInputLaneSegments().add(laneSegment);
	}

	public List<GtsLaneSegment> getOutputLaneSegments() {
		if(this.outputLaneSegments == null) this.outputLaneSegments = new ArrayList<GtsLaneSegment>();
		return this.outputLaneSegments;
	}

	public void setOutputLaneSegments(List<GtsLaneSegment> outputLaneSegments) {
		this.outputLaneSegments = outputLaneSegments;
	}
	
	public void addOutputLaneSegment(GtsLaneSegment laneSegment) {
		this.getOutputLaneSegments().add(laneSegment);
	}
	
	public void removeLaneSegment(GtsLaneSegment segment){
		getInputLaneSegments().remove(segment);
		getOutputLaneSegments().remove(segment);
    }
	
	 public double distance(Point2D.Double point){
	     return Math.sqrt((point.x - x)*(point.x -x) + ((point.y -y)*(point.y -y)));
	 }
	    
	 public double distance(GtsNode node){
	     return Math.sqrt((node.getX() - x)*(node.getX()-x) + ((node.getY() -y)*(node.getY() -y)));
	 }
	 
	 public boolean isSameLocation(GtsNode node) {
		 return node.getX()== x && node.getY() == y;
	 }
	 
	 public boolean isChanged(GtsNode node) {
		 return this.getNodeId() == node.getNodeId() && !isSameLocation(node);
	 }
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getNodeId(),getNodeName(),getNodeType(),getX(),
				getY(),isVisible(),isUserSettable(),getStatus());
	}
	
	public GtsNode copy() {
		return new GtsNode(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GtsNode other = (GtsNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
