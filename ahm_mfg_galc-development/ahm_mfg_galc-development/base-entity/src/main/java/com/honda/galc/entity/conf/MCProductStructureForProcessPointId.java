package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 *  @author Fredrick Yessaian
 * Apr 01, 2016 :: New
 */

@Embeddable
public class MCProductStructureForProcessPointId implements Serializable {

	private static final long serialVersionUID = -1697024608900282952L;

	@Column(name="PRODUCT_ID", nullable=false, length=17)
	private String productId;
	
	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;
	
	@Column(name="PRODUCT_SPEC_CODE", nullable=false, length=30)
	private String productSpecCode;
	
	public MCProductStructureForProcessPointId() {}
	
	public MCProductStructureForProcessPointId(String productId, String processPointId, String productSpecCode){
		this.setProductId(productId);
		this.setProcessPointId(processPointId);
		this.setProductSpecCode(productSpecCode);
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getProductId(), getProcessPointId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		MCProductStructureForProcessPointId other = (MCProductStructureForProcessPointId) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
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
