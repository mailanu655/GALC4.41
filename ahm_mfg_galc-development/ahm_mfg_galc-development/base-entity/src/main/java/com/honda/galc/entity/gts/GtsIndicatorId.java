package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsIndicatorId Class description</h3>
 * <p> GtsIndicatorId description </p>
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
public class GtsIndicatorId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="INDICATOR_NAME")
	private String indicatorName;

	private static final long serialVersionUID = 1L;

	public GtsIndicatorId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getIndicatorName() {
		return StringUtils.trim(this.indicatorName);
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsIndicatorId)) {
			return false;
		}
		GtsIndicatorId other = (GtsIndicatorId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.indicatorName.equals(other.indicatorName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.indicatorName.hashCode();
		return hash;
	}

}
