package com.honda.galc.entity.gts;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * <h3>GtsCarrierId Class description</h3>
 * <p> GtsCarrierId description </p>
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
public class GtsCarrierId implements Serializable {
	@Column(name="TRACKING_AREA")
	private String trackingArea;

	@Column(name="CARRIER_NUMBER")
	private String carrierNumber;

	private static final long serialVersionUID = 1L;
	
	public GtsCarrierId(String trackingArea, String carrierNumber) {
		this.trackingArea = trackingArea;
		this.carrierNumber = carrierNumber;
	}
	
	public GtsCarrierId() {
		super();
	}

	public String getTrackingArea() {
		return StringUtils.trim(this.trackingArea);
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getCarrierNumber() {
		return StringUtils.trim(this.carrierNumber);
	}

	public void setCarrierNumber(String carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof GtsCarrierId)) {
			return false;
		}
		GtsCarrierId other = (GtsCarrierId) o;
		return this.trackingArea.equals(other.trackingArea)
			&& this.carrierNumber.equals(other.carrierNumber);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trackingArea.hashCode();
		hash = hash * prime + this.carrierNumber.hashCode();
		return hash;
	}

}
