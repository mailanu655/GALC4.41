package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsLaneSegmentMapId Class description</h3>
 * <p> GtsLaneSegmentMapId description </p>
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
public class GtsLaneSegmentMapId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="LANE_ID")
	private String laneId;

	@Column(name="LANE_SEQ")
	private int laneSeq;

	private static final long serialVersionUID = 1L;

	public GtsLaneSegmentMapId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getLaneId() {
		return StringUtils.trim(this.laneId);
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public int getLaneSeq() {
		return this.laneSeq;
	}

	public void setLaneSeq(int laneSeq) {
		this.laneSeq = laneSeq;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsLaneSegmentMapId)) {
			return false;
		}
		GtsLaneSegmentMapId other = (GtsLaneSegmentMapId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.laneId.equals(other.laneId)
			&& (this.laneSeq == other.laneSeq);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.laneId.hashCode();
		hash = hash * prime + ((int) this.laneSeq);
		return hash;
	}

}
