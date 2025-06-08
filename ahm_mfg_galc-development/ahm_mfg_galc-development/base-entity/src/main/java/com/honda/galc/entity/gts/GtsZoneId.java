package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsZoneId Class description</h3>
 * <p> GtsZoneId description </p>
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
public class GtsZoneId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	private String zone;

	private static final long serialVersionUID = 1L;

	public GtsZoneId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getZone() {
		return StringUtils.trim(this.zone);
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsZoneId)) {
			return false;
		}
		GtsZoneId other = (GtsZoneId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.zone.equals(other.zone);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.zone.hashCode();
		return hash;
	}

}
