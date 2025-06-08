package com.honda.galc.entity.product;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class BearingSelectResultId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

	public BearingSelectResultId() {
		super();
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BearingSelectResultId)) {
			return false;
		}
		BearingSelectResultId other = (BearingSelectResultId) o;
		return this.getProductId().equals(other.getProductId())
			&& this.actualTimestamp.equals(other.actualTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getProductId().hashCode();
		hash = hash * prime + this.actualTimestamp.hashCode();
		return hash;
	}

}
