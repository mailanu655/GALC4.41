package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * * *
 * 
 * @version 1
 * @author Gangadhararao Gadde,Subu Kathiresan
 * @since Nov 01, 2012
 * */

@Embeddable
public class ProductPriorityPlanId implements Serializable {
	@Column(name = "TRACKING_STATUS")
	private String trackingStatus;

	@Column(name = "TRACKING_SEQUENCE_NO")
	private Double trackingSequenceNo;

	private static final long serialVersionUID = 1L;

	public ProductPriorityPlanId() {
		super();

	}

	public ProductPriorityPlanId(String trackingStatus,
			Double trackingSequenceNo) {
		super();
		this.trackingStatus = trackingStatus;
		this.trackingSequenceNo = trackingSequenceNo;
	}

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public Double getTrackingSequenceNo() {
		return trackingSequenceNo;
	}

	public void setTrackingSequenceNo(Double trackingSequenceNo) {
		this.trackingSequenceNo = trackingSequenceNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((trackingSequenceNo == null) ? 0 : trackingSequenceNo
						.hashCode());
		result = prime * result
				+ ((trackingStatus == null) ? 0 : trackingStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPriorityPlanId other = (ProductPriorityPlanId) obj;
		if (trackingSequenceNo == null) {
			if (other.trackingSequenceNo != null)
				return false;
		} else if (!trackingSequenceNo.equals(other.trackingSequenceNo))
			return false;
		if (trackingStatus == null) {
			if (other.trackingStatus != null)
				return false;
		} else if (!trackingStatus.equals(other.trackingStatus))
			return false;
		return true;
	}

}
