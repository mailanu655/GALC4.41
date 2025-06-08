package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsShapeId Class description</h3>
 * <p> GtsShapeId description </p>
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
public class GtsShapeId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="SHAPE_ID")
	private int shapeId;

	private static final long serialVersionUID = 1L;
	
	public GtsShapeId(GtsShapeId id) {
		this.trackingArea = id.trackingArea;
		this.shapeId = id.shapeId;
	}
	
	public GtsShapeId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getShapeId() {
		return this.shapeId;
	}

	public void setShapeId(int shapeId) {
		this.shapeId = shapeId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsShapeId)) {
			return false;
		}
		GtsShapeId other = (GtsShapeId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.shapeId == other.shapeId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + ((int) this.shapeId);
		return hash;
	}

}
