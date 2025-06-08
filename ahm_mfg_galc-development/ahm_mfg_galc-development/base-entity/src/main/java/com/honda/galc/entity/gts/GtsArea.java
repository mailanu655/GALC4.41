package com.honda.galc.entity.gts;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsIndicatorType;

/**
 * 
 * 
 * <h3>GtsArea Class description</h3>
 * <p> GtsArea description </p>
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
 * Nov 14, 2014
 *
 *
 */

@Entity
@Table(name="GTS_AREA_TBX")
public class GtsArea extends AuditEntry {
	@Id
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="AREA_DESCRIPTION")
	private String areaDescription;

	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="TAG_TYPE")
	private int tagType;

	@Column(name="IS_ENABLED")
	private int isEnabled;

	@Column(name="TRACKING_MODE")
	private int trackingMode;

	private static final long serialVersionUID = 1L;
	
	@Transient
	private List<GtsLane> lanes;
	
	@Transient
	private List<GtsIndicator> indicators;
	
	@Transient
	private List<GtsNode> nodes;

	@Transient
	private List<GtsMove> moves;
	
	@Transient
	private List<GtsMoveCondition> moveConditions;
	
	@Transient
	private List<GtsDecisionPoint> decisionPoints;
	
	@Transient
	private List<GtsDecisionPointCondition> decisionPointConditions;
	
	
	public GtsArea() {
		super();
	}
	
	public String getId() {
		return getTrackingArea();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getAreaDescription() {
		return StringUtils.trim(this.areaDescription);
	}

	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}

	public String getProductType() {
		return StringUtils.trim(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getTagType() {
		return this.tagType;
	}

	public void setTagType(int tagType) {
		this.tagType = tagType;
	}

	public int getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getTrackingMode() {
		return this.trackingMode;
	}

	public void setTrackingMode(int trackingMode) {
		this.trackingMode = trackingMode;
	}
	
	public List<GtsLane> getLanes() {
		return lanes;
	}

	public void setLanes(List<GtsLane> lanes) {
		this.lanes = lanes;
	}

	public List<GtsIndicator> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<GtsIndicator> indicators) {
		this.indicators = indicators;
	}

	public List<GtsNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<GtsNode> nodes) {
		this.nodes = nodes;
	}

	public List<GtsMove> getMoves() {
		return moves;
	}

	public void setMoves(List<GtsMove> moves) {
		this.moves = moves;
	}

	public List<GtsMoveCondition> getMoveConditions() {
		return moveConditions;
	}

	public void setMoveConditions(List<GtsMoveCondition> moveConditions) {
		this.moveConditions = moveConditions;
	}
	
	public List<GtsDecisionPoint> getDecisionPoints() {
		return decisionPoints;
	}

	public void setDecisionPoints(List<GtsDecisionPoint> decisionPoints) {
		this.decisionPoints = decisionPoints;
	}

	public List<GtsDecisionPointCondition> getDecisionPointConditions() {
		return decisionPointConditions;
	}

	public void setDecisionPointConditions(
			List<GtsDecisionPointCondition> decisionPointConditions) {
		this.decisionPointConditions = decisionPointConditions;
	}

	public List<GtsIndicator> findIndicators(GtsIndicatorType type) {
		List<GtsIndicator> filteredIndicators = new ArrayList<GtsIndicator>();
		for(GtsIndicator indicator : getIndicators()) {
			if(type == indicator.getIndicatorType()) filteredIndicators.add(indicator);
		}
		return filteredIndicators;
	}
	
	public GtsIndicator findIndicator(String indicatorId) {
		for(GtsIndicator indicator : getIndicators()) {
			if(indicator.getIndicatorName().equals(indicatorId)) return indicator;
		}
		return null;
	}
	
	public GtsLane findLane(String laneId) {
		for(GtsLane lane : getLanes()) {
			if(lane.getLaneId().equals(laneId)) return lane;
		}
		return null;
	}
	
	public GtsNode getEntryNode(String laneName){
        String gate = "ENTRY-"+laneName;
        for(GtsNode node: this.getNodes()){
            if(node.isVisible() && node.getNodeName().equals(gate)) return node;
        }
        return null;
    }
	
	public GtsNode getExitNode(String laneName){
        String gate = "EXIT-"+laneName;
        for(GtsNode node: this.getNodes()){
            if(node.isVisible() && node.getNodeName().equals(gate)) return node;
        }
        return null;
    }
	
	public boolean isEntryGateOpen(String laneName){
	    GtsNode node = getEntryNode(laneName);
	    return node != null && node.isGateOpen();
	}
	    
	public boolean isExitGateOpen(String laneName){
	    GtsNode node = getExitNode(laneName);
	    return node != null && node.isGateOpen();
    }
	
	public boolean isLanePhysicallyFull(String laneName) {
		for(GtsIndicator indicator : getIndicators()) {
			 if(indicator.getLaneName().equals(laneName) && 
			       (indicator.getIndicatorType() == GtsIndicatorType.LINE_FULL || 
			        indicator.getIndicatorType() == GtsIndicatorType.CPLF))
				 
	                return indicator.isStatusOn();

		}
		return false;
	}

	public String toString() {
		return toString(getTrackingArea(),getProductType());
	}
	
}
