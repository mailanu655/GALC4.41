package com.honda.galc.entity.qics;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReuseProductResultId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	private static final long serialVersionUID = 1L;

	public ReuseProductResultId() {
		super();
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Date getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ReuseProductResultId)) {
			return false;
		}
		ReuseProductResultId other = (ReuseProductResultId) o;
		return this.productId.equals(other.productId)
			&& this.actualTimestamp.equals(other.actualTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.actualTimestamp.hashCode();
		return hash;
	}

}
