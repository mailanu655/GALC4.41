package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * 
 * <h3>GtsColorMapId Class description</h3>
 * <p> GtsColorMapId description </p>
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
public class GtsColorMapId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="COLOR_ID")
	private int colorId;

	private static final long serialVersionUID = 1L;

	public GtsColorMapId() {
		super();
	}

	public String getTrackingArea() {
		return this.trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getColorId() {
		return this.colorId;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsColorMapId)) {
			return false;
		}
		GtsColorMapId other = (GtsColorMapId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.colorId == other.colorId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + ((int) this.colorId);
		return hash;
	}

}
