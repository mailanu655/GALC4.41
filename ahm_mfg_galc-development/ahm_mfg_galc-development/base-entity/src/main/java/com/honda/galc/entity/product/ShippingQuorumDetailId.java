package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ShippingQuorumDetailId implements Serializable {
	@Column(name="QUORUM_DATE", updatable=false)
	private Date quorumDate;

	@Column(name="QUORUM_ID", updatable=false)
	private int quorumId;

	@Column(name="QUORUM_SEQ")
	private int quorumSeq;

	private static final long serialVersionUID = 1L;

	public ShippingQuorumDetailId() {
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

	public int getQuorumSeq() {
		return this.quorumSeq;
	}

	public void setQuorumSeq(int quorumSeq) {
		this.quorumSeq = quorumSeq;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ShippingQuorumDetailId)) {
			return false;
		}
		ShippingQuorumDetailId other = (ShippingQuorumDetailId) o;
		return this.quorumDate.equals(other.quorumDate)
			&& (this.quorumId == other.quorumId)
			&& (this.quorumSeq == other.quorumSeq);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.quorumDate.hashCode();
		hash = hash * prime + this.quorumId;
		hash = hash * prime + this.quorumSeq;
		return hash;
	}

}
