package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsLaneId Class description</h3>
 * <p> GtsLaneId description </p>
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
public class GtsLaneId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="LANE_ID")
	private String laneId;

	private static final long serialVersionUID = 1L;

	public GtsLaneId() {
		super();
	}
	
	public GtsLaneId(String area, String laneId) {
		this.trackingArea = area;
		this.laneId = laneId;
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsLaneId)) {
			return false;
		}
		GtsLaneId other = (GtsLaneId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.laneId.equals(other.laneId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.laneId.hashCode();
		return hash;
	}

}
