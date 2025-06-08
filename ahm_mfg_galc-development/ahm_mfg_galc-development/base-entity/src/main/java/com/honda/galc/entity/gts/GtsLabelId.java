package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsLabelId Class description</h3>
 * <p> GtsLabelId description </p>
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
public class GtsLabelId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="LABEL_ID")
	private int labelId;

	private static final long serialVersionUID = 1L;

	public GtsLabelId(GtsLabelId id) {
		this.trackingArea = id.trackingArea;
		this.labelId = id.labelId;
	}
	
	public GtsLabelId() {
		super();
	}
	
	public GtsLabelId(String trackingArea,int labelId) {
		this.trackingArea = trackingArea;
		this.labelId = labelId;
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getLabelId() {
		return this.labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsLabelId)) {
			return false;
		}
		GtsLabelId other = (GtsLabelId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.labelId == other.labelId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.labelId;
		return hash;
	}

}
