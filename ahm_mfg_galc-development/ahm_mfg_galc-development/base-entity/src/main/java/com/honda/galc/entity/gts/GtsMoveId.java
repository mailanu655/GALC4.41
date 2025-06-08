package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsMoveId Class description</h3>
 * <p> GtsMoveId description </p>
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
public class GtsMoveId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="SOURCE_LANE_ID")
	private String sourceLaneId;

	@Column(name="DESTINATION_LANE_ID")
	private String destinationLaneId;

	private static final long serialVersionUID = 1L;

	public GtsMoveId() {
		super();
	}
	
	public GtsMoveId(String trackingArea,String sourceLaneId,String destinationLaneId) {
		this.trackingArea = trackingArea;
		this.sourceLaneId = sourceLaneId;
		this.destinationLaneId = destinationLaneId;
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getSourceLaneId() {
		return StringUtils.trim(this.sourceLaneId);
	}

	public void setSourceLaneId(String sourceLaneId) {
		this.sourceLaneId = sourceLaneId;
	}

	public String getDestinationLaneId() {
		return StringUtils.trim(this.destinationLaneId);
	}

	public void setDestinationLaneId(String destinationLaneId) {
		this.destinationLaneId = destinationLaneId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsMoveId)) {
			return false;
		}
		GtsMoveId other = (GtsMoveId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.sourceLaneId.equals(other.sourceLaneId)
			&& this.destinationLaneId.equals(other.destinationLaneId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.sourceLaneId.hashCode();
		hash = hash * prime + this.destinationLaneId.hashCode();
		return hash;
	}

}
