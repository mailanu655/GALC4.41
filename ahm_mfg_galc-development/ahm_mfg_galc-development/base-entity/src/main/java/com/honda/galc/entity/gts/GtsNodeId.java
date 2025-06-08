package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsNodeId Class description</h3>
 * <p> GtsNodeId description </p>
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
public class GtsNodeId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="NODE_ID")
	private int nodeId;

	private static final long serialVersionUID = 1L;

	public GtsNodeId(GtsNodeId id) {
		this.trackingArea = id.trackingArea;
		this.nodeId = id.nodeId;
	}
	
	public GtsNodeId(String trackingArea, int nodeId) {
		this.trackingArea = trackingArea;
		this.nodeId = nodeId;
	}
	
	public GtsNodeId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public int getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsNodeId)) {
			return false;
		}
		GtsNodeId other = (GtsNodeId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& (this.nodeId == other.nodeId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + ((int) this.nodeId);
		return hash;
	}

}
