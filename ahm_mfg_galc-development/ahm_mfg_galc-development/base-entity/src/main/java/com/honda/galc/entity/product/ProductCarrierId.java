package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ProductCarrierId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="CARRIER_ID")
	private String carrierId;

	@Column(name="ON_TIMESTAMP")
	private Timestamp onTimestamp;

	private static final long serialVersionUID = 1L;

	public ProductCarrierId() {
		super();
	}
	
	public ProductCarrierId(String productId, String carrierId, Timestamp onTimestamp) {
		this.productId = productId;
		this.carrierId = carrierId;
		this.onTimestamp = onTimestamp;
	}


	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCarrierId() {
		return StringUtils.trim(this.carrierId);
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
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
		if ( ! (o instanceof ProductCarrierId)) {
			return false;
		}
		ProductCarrierId other = (ProductCarrierId) o;
		return this.productId.equals(other.productId)
			&& this.carrierId.equals(other.carrierId)
			&& this.onTimestamp.equals(other.onTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.carrierId.hashCode();
		hash = hash * prime + this.onTimestamp.hashCode();
		return hash;
	}

}
