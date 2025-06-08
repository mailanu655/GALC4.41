package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Mar 26, 2018
 */
@Embeddable
public class LetPassResultId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	public LetPassResultId() {
		super();
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetPassResultId other = (LetPassResultId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
	
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
