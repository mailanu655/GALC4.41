package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class DunnageContentId implements Serializable {
	@Column(name="DUNNAGE_ID")
	private String dunnageId;

	@Column(name="PRODUCT_ID")
	private String productId;
	
	private static final long serialVersionUID = 1L;

	public DunnageContentId() {
		super();
	}
	
	public DunnageContentId(String dunnageId, String productId) {
		this.dunnageId = dunnageId;
		this.productId = productId;
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof DunnageContentId)) {
			return false;
		}
		DunnageContentId other = (DunnageContentId) o;
		return this.dunnageId.equals(other.dunnageId)
			&& this.productId.equals(other.productId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.dunnageId.hashCode();
		hash = hash * prime + this.productId.hashCode();
		return hash;
	}

}
