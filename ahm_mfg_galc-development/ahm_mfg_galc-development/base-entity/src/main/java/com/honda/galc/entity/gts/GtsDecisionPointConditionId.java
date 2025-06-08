package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsDpConditionId Class description</h3>
 * <p> GtsDpConditionId description </p>
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
public class GtsDecisionPointConditionId implements Serializable {
	@Column(name="TRACKING_AREA", insertable=false, updatable=false)
	private String trackingArea;

	@Column(name="INDICATOR_ID", insertable=false, updatable=false)
	private String indicatorId;

	@Column(name="DP_ID")
	private int decisionPointId;

	private static final long serialVersionUID = 1L;

	public GtsDecisionPointConditionId() {
		super();
	}
	
	public GtsDecisionPointConditionId(String trackingArea,int decisionPointId,String indicatorId){
		this.trackingArea = trackingArea;
		this.decisionPointId = decisionPointId;
		this.indicatorId = indicatorId;
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getIndicatorId() {
		return StringUtils.trim(this.indicatorId);
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	public int getDecisionPointId() {
		return this.decisionPointId;
	}

	public void setDpId(int dpId) {
		this.decisionPointId = dpId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsDecisionPointConditionId)) {
			return false;
		}
		GtsDecisionPointConditionId other = (GtsDecisionPointConditionId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.decisionPointId == other.decisionPointId)
			&& this.indicatorId.equals(other.indicatorId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + ((int) this.decisionPointId);
		hash = hash * prime + this.indicatorId.hashCode();
		return hash;
	}

}
