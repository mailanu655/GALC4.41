/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date Jan 5, 2012
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class HoldResultId implements Serializable {
	public HoldResultId(String productId, int holdType) {
		super();
		this.productId = productId;
		this.holdType = holdType;
		this.actualTimestamp = new Timestamp(System.currentTimeMillis());
	}

	@Column(name = "PRODUCT_ID")
	private String productId;

	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name = "HOLD_TYPE")
	private int holdType;
	private static final long serialVersionUID = 1L;

	public HoldResultId() {
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
	
	public int getHoldType() {
		return this.holdType;
	}

	public void setHoldType(int holdType) {
		this.holdType = holdType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + holdType;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
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
		HoldResultId other = (HoldResultId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (holdType != other.holdType)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
}

