package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsMoveConditionId Class description</h3>
 * <p> GtsMoveConditionId description </p>
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
public class GtsMoveConditionId implements Serializable {
	@Column(name="TRACKING_AREA", insertable=false, updatable=false)
	private String trackingArea;

	@Column(name="SOURCE_LANE_ID", insertable=false, updatable=false)
	private String sourceLaneId;

	@Column(name="DESTINATION_LANE_ID", insertable=false, updatable=false)
	private String destinationLaneId;

	@Column(name="INDICATOR_ID", insertable=false, updatable=false)
	private String indicatorId;

	private static final long serialVersionUID = 1L;

	public GtsMoveConditionId() {
		super();
	}
	
	public GtsMoveConditionId(String trackingArea,String soureLaneId,String destinationLaneId,String indicatorId) {
		this.trackingArea = trackingArea;
		this.sourceLaneId = soureLaneId;
		this.destinationLaneId = destinationLaneId;
		this.indicatorId = indicatorId;
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

	public String getIndicatorId() {
		return StringUtils.trim(this.indicatorId);
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsMoveConditionId)) {
			return false;
		}
		GtsMoveConditionId other = (GtsMoveConditionId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.sourceLaneId.equals(other.sourceLaneId)
			&& this.destinationLaneId.equals(other.destinationLaneId)
			&& this.indicatorId.equals(other.indicatorId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.sourceLaneId.hashCode();
		hash = hash * prime + this.destinationLaneId.hashCode();
		hash = hash * prime + this.indicatorId.hashCode();
		return hash;
	}

}
