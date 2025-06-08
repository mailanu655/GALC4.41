package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class DunnageHistId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="DUNNAGE_ID")
	private String dunnageId;

	@Column(name="ON_TIMESTAMP")
	private Timestamp onTimestamp;

	private static final long serialVersionUID = 1L;

	public DunnageHistId() {
		super();
	}
	
	public DunnageHistId(String productId, String dunnageId, Timestamp onTimestamp) {
		this.productId = productId;
		this.dunnageId = dunnageId;
		this.onTimestamp = onTimestamp;
	}


	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDunnageId() {
		return StringUtils.trim(this.dunnageId);
	}

	public void setDunnageId(String dunnageId) {
		this.dunnageId = dunnageId;
	}

	public Timestamp getOnTimestamp() {
		return this.onTimestamp;
	}

	public void setOnTimestamp(Timestamp onTimestamp) {
		this.onTimestamp = onTimestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof DunnageHistId)) {
			return false;
		}
		DunnageHistId other = (DunnageHistId) o;
		return this.productId.equals(other.productId)
			&& this.dunnageId.equals(other.dunnageId)
			&& this.onTimestamp.equals(other.onTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.dunnageId.hashCode();
		hash = hash * prime + this.onTimestamp.hashCode();
		return hash;
	}

}
