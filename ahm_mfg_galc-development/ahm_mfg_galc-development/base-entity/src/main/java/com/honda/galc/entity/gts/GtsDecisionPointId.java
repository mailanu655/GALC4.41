package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsDecisionPointId Class description</h3>
 * <p> GtsDecisionPointId description </p>
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
public class GtsDecisionPointId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="DP_ID")
	private int decisionPointId;

	private static final long serialVersionUID = 1L;

	public GtsDecisionPointId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getDecisionPointId() {
		return this.decisionPointId;
	}

	public void setDecisionPointId(int dpId) {
		this.decisionPointId = dpId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsDecisionPointId)) {
			return false;
		}
		GtsDecisionPointId other = (GtsDecisionPointId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.decisionPointId == other.decisionPointId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + ((int) this.decisionPointId);
		return hash;
	}

}
