package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AbstractEntity;
import com.honda.galc.entity.enumtype.TrailerStatus;

/**
 * 
 * 
 * <h3>ShippingTrailer Class description</h3>
 * <p> ShippingTrailer description </p>
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
 * Sep 22, 2014
 *
 *
 */
@Entity
@Table(name="TRAILER_TBX")
public class ShippingTrailer extends AbstractEntity {
	@Id
	@Column(name="TRAILER_NUMBER")
	private String trailerNumber;

	@Column(name="TRAILER_CAPACITY")
	private int trailerCapacity;

	@Column(name="STATUS")
	private int statusId;

	@Column(name="EMPTY_CARRIER_COUNT")
	private int emptyCarrierCount;
	
	private static final long serialVersionUID = 1L;

	public ShippingTrailer() {
		super();
	}

	public String getId() {
		return getTrailerNumber();
	}


	public String getTrailerNumber() {
		return StringUtils.trim(this.trailerNumber);
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public TrailerStatus getStatus() {
		return TrailerStatus.getType(statusId);
	}
	
	public void setStatus(TrailerStatus status) {
		this.statusId = status.getId();
	}

	public void setTrailerCapacity(int trailerCapacity) {
		this.trailerCapacity = trailerCapacity;
	}

	public int getTrailerCapacity() {
		return trailerCapacity;
	}

	public void setEmptyCarrierCount(int emptyCarrierCount) {
		this.emptyCarrierCount = emptyCarrierCount;
	}

	public int getEmptyCarrierCount() {
		return emptyCarrierCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((trailerNumber == null) ? 0 : trailerNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingTrailer other = (ShippingTrailer) obj;
		if (trailerNumber == null) {
			if (other.trailerNumber != null)
				return false;
		} else if (!trailerNumber.equals(other.trailerNumber))
			return false;
		return true;
	}
	
	

}
