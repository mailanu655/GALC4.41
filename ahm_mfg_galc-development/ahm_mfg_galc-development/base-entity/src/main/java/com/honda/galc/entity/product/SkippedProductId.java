package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class SkippedProductId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	private static final long serialVersionUID = 1L;

	public SkippedProductId() {
		super();
	}

	public SkippedProductId(String productId, String processPointId) {
		this.productId = productId;
		this.processPointId = processPointId;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof SkippedProductId)) {
			return false;
		}
		SkippedProductId other = (SkippedProductId) o;
		return this.getProductId().equals(other.getProductId())
			&& this.getProcessPointId().equals(other.getProcessPointId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getProductId().hashCode();
		hash = hash * prime + this.getProcessPointId().hashCode();
		return hash;
	}

}
