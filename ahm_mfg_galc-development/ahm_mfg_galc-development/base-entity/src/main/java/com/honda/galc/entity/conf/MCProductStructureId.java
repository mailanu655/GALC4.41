package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 *  @author Fredrick Yessaian
 * Mar 04, 2015 :: Modified to include division id and product_spec_code
 */

@Embeddable
public class MCProductStructureId implements Serializable {

	private static final long serialVersionUID = -1697024608900282952L;

	@Column(name="PRODUCT_ID", nullable=false, length=17)
	private String productId;
	
	@Column(name="DIVISION_ID", nullable=false, length=16)
	private String divisionId;
	
	@Column(name="PRODUCT_SPEC_CODE", nullable=false, length=30)
	private String productSpecCode;
	
	public MCProductStructureId() {}
	
	public MCProductStructureId(String productId, String divisionId, String productSpecCode){
		this.setProductId(productId);
		this.setDivisionId(divisionId);
		this.setProductSpecCode(productSpecCode);
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDivisionId() {
		return StringUtils.trim(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getProductId(), getDivisionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MCProductStructureId))
			return false;
		MCProductStructureId other = (MCProductStructureId) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		return true;
	}

}
