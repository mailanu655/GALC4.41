package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsLaneSegmentId Class description</h3>
 * <p> GtsLaneSegmentId description </p>
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
 * Nov 17, 2014
 *
 *
 */
@Embeddable
public class GtsLaneSegmentId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="LANE_SEGMENT_ID")
	private int laneSegmentId;

	private static final long serialVersionUID = 1L;
	
	public GtsLaneSegmentId(GtsLaneSegmentId id) {
		this.trackingArea = id.trackingArea;
		this.laneSegmentId = id.laneSegmentId;
	}
	
	public GtsLaneSegmentId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getLaneSegmentId() {
		return this.laneSegmentId;
	}

	public void setLaneSegmentId(int laneSegmentId) {
		this.laneSegmentId = laneSegmentId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsLaneSegmentId)) {
			return false;
		}
		GtsLaneSegmentId other = (GtsLaneSegmentId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.laneSegmentId == other.laneSegmentId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.laneSegmentId;
		return hash;
	}

}
