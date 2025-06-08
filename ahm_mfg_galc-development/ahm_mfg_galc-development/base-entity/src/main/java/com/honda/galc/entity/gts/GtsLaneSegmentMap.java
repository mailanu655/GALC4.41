package com.honda.galc.entity.gts;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsLaneSegmentMap Class description</h3>
 * <p> GtsLaneSegmentMap description </p>
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
@Table(name="GTS_LANE_SEGMENT_MAP_TBX")
public class GtsLaneSegmentMap extends AuditEntry {
	@EmbeddedId
	private GtsLaneSegmentMapId id;
	
	@Column(name="LANE_SEGMENT_ID")
	private int laneSegmentId;
	
	@Transient
	private GtsLane lane;

	
	@Transient
	private GtsLaneSegment laneSegment;

	private static final long serialVersionUID = 1L;

	public GtsLaneSegmentMap() {
		super();
	}
	
	public GtsLaneSegmentMap(String trackingArea, String laneId, int seq) {
		id = new GtsLaneSegmentMapId();
		id.setTrackingArea(trackingArea);
		id.setLaneId(laneId);
		id.setLaneSeq(seq);
	}

	public GtsLaneSegmentMapId getId() {
		return this.id;
	}

	public void setId(GtsLaneSegmentMapId id) {
		this.id = id;
	}
	
	public int getLaneSeq() {
		return id.getLaneSeq();
	}

	public GtsLane getLane() {
		return this.lane;
	}

	public void setLane(GtsLane lane) {
		this.lane = lane;
	}
	
	public int getLaneSegmentId() {
		return laneSegmentId;
	}

	public void setLaneSegmentId(int laneSegmentId) {
		this.laneSegmentId = laneSegmentId;
	}

	public GtsLaneSegment getLaneSegment() {
		return this.laneSegment;
	}

	public void setLaneSegment(GtsLaneSegment laneSegment) {
		this.laneSegment = laneSegment;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getLaneId(),getId().getLaneSeq(),getLaneSegmentId());
	}

}
