package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ShippingQuorumId implements Serializable {
	@Column(name="QUORUM_DATE")
	private Date quorumDate;

	@Column(name="QUORUM_ID")
	private int quorumId;

	private static final long serialVersionUID = 1L;

	public ShippingQuorumId() {
		super();
	}

	public Date getQuorumDate() {
		return this.quorumDate;
	}

	public void setQuorumDate(Date quorumDate) {
		this.quorumDate = quorumDate;
	}

	public int getQuorumId() {
		return this.quorumId;
	}

	public void setQuorumId(int quorumId) {
		this.quorumId = quorumId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ShippingQuorumId)) {
			return false;
		}
		ShippingQuorumId other = (ShippingQuorumId) o;
		return this.quorumDate.equals(other.quorumDate)
			&& (this.quorumId == other.quorumId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.quorumDate.hashCode();
		hash = hash * prime + this.quorumId;
		return hash;
	}

}
