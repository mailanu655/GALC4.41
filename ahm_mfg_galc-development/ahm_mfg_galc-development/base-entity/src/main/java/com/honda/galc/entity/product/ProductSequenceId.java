package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ProductSequenceId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	private static final long serialVersionUID = 1L;

	public ProductSequenceId() {
		super();
	}

	public ProductSequenceId(String productId, String processPointId) {
		setProductId(productId);
		setProcessPointId(processPointId);
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = StringUtils.trim(processPointId);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ProductSequenceId)) {
			return false;
		}
		ProductSequenceId other = (ProductSequenceId) o;
		return this.productId.equals(other.productId)
			&& this.processPointId.equals(other.processPointId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.processPointId.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		 StringBuilder sb = new StringBuilder();
	        sb.append("\"").append(getProductId()).append(",");
	        sb.append(getProcessPointId()).append("\"");
	        return sb.toString();
	}
	
	

}
