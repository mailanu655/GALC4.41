package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * 
 * <h3>ShippingTrailerRackId Class description</h3>
 * <p> ShippingTrailerRackId description </p>
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
 * Jan 29, 2015
 *
 *
 */
@Embeddable
public class ShippingTrailerRackId implements Serializable {
	@Column(name="TRAILER_NUMBER")
	private String trailerNumber;

	@Column(name="RACK_TYPE")
	private String rackType;

	private static final long serialVersionUID = 1L;

	public ShippingTrailerRackId() {
		super();
	}
	
	public String getTrailerNumber() {
		return StringUtils.trim(trailerNumber);
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getRackType() {
		return StringUtils.trim(rackType);
	}


	public void setRackType(String rackType) {
		this.rackType = rackType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ShippingTrailerRackId)) {
			return false;
		}
		ShippingTrailerRackId other = (ShippingTrailerRackId) o;
		return this.trailerNumber.equals(other.trailerNumber)
			&& (this.rackType == other.rackType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.trailerNumber.hashCode();
		hash = hash * prime + this.rackType.hashCode();
		return hash;
	}

}
