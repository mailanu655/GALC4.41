package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsClientListId Class description</h3>
 * <p> GtsClientListId description </p>
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
 * Sep 9, 2015
 *
 *
 */
@Embeddable
public class GtsClientListId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="CLIENT_IP")
	private String clientIp;

	@Column(name="CLIENT_PORT")
	private int clientPort;

	private static final long serialVersionUID = 1L;

	public GtsClientListId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getClientIp() {
		return StringUtils.trim(this.clientIp);
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public int getClientPort() {
		return this.clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsClientListId)) {
			return false;
		}
		GtsClientListId other = (GtsClientListId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.clientIp.equals(other.clientIp)
			&& (this.clientPort == other.clientPort);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.clientIp.hashCode();
		hash = hash * prime + ((int) this.clientPort);
		return hash;
	}

}
