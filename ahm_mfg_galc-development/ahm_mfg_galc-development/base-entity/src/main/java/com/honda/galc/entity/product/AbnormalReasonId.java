package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class AbnormalReasonId implements Serializable {

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="ABNORMAL_TYPE")
	private String abnormalType;

	private static final long serialVersionUID = 1L;

	public AbnormalReasonId() {
		super();
	}

	public AbnormalReasonId(String productId, String abnormalType) {
		super();
		this.productId = productId;
		this.abnormalType = abnormalType;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAbnormalType() {
		return StringUtils.trim(this.abnormalType);
	}

	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbnormalReasonId)) {
			return false;
		}
		AbnormalReasonId other = (AbnormalReasonId) o;
		return this.productId.equals(other.productId)
				&& this.abnormalType.equals(other.abnormalType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.abnormalType.hashCode();
		return hash;
	}

}
