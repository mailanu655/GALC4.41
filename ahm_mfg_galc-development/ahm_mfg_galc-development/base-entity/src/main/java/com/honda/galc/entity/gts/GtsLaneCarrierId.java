package com.honda.galc.entity.gts;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsLaneQueueId Class description</h3>
 * <p> GtsLaneQueueId description </p>
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
public class GtsLaneCarrierId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="LANE_ID")
	private String laneId;

	@Column(name="LANE_POSITION")
	private Timestamp lanePosition;

	private static final long serialVersionUID = 1L;

	public GtsLaneCarrierId() {
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

	public Timestamp getLanePosition() {
		return this.lanePosition;
	}

	public void setLanePosition(Timestamp lanePosition) {
		this.lanePosition = lanePosition;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsLaneCarrierId)) {
			return false;
		}
		GtsLaneCarrierId other = (GtsLaneCarrierId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.getLaneId().equals(other.getLaneId())
			&& ObjectUtils.equals(this.lanePosition,other.lanePosition);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.getLaneId().hashCode();
		hash = hash * prime + this.lanePosition.hashCode();
		return hash;
	}

}
