/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 22, 2014
 */


@Embeddable
public class StragglerId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "PRODUCT_ID")
	private String productId;
	
	@Column(name = "PP_DELAYED_AT")
	private String ppDelayedAt;
	
	@Column(name="STRAGGLER_TYPE")
	private String stragglerType;

	public StragglerId() {
		super();
	}

	public StragglerId(String productId, String ppDelayedAt, String stragglerType) {
		super();
		this.productId = productId;
		this.ppDelayedAt = ppDelayedAt;
		this.stragglerType = stragglerType;
	}

	
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPpDelayedAt() {
		return StringUtils.trimToEmpty(ppDelayedAt);
	}

	public void setPpDelayedAt(String ppDelayedAt) {
		this.ppDelayedAt = ppDelayedAt;
	}

	public String getStragglerType() {
		return stragglerType;
	}

	public void setStragglerType(String stragglerType) {
		this.stragglerType = stragglerType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ppDelayedAt == null) ? 0 : ppDelayedAt.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((stragglerType == null) ? 0 : stragglerType.hashCode());
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
		StragglerId other = (StragglerId) obj;
		if (ppDelayedAt == null) {
			if (other.ppDelayedAt != null)
				return false;
		} else if (!ppDelayedAt.equals(other.ppDelayedAt))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (stragglerType == null) {
			if (other.stragglerType != null)
				return false;
		} else if (!stragglerType.equals(other.stragglerType))
			return false;
		
		return true;
	}

	@Override
	public String toString() {		
		return StringUtil.toString(getClass().getSimpleName(),getProductId(),getPpDelayedAt(),getStragglerType());
	}

	
	
}

