package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author vf036360
 *
 */
@Entity
@Table(name="DUNNAGE_TBX")
public class Dunnage extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DUNNAGE_ID")
	private String dunnageId;
	
	@Column(name="EXPECTED_QTY")
	private int expectedQty;
	
	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name="DUNNAGE_STATUS")
	private String dunnageStatus;

	public Dunnage() {
		super();
	}

	public String getId() {
		return this.getDunnageId();
	}

	public String getDunnageId() {
		return StringUtils.trim(dunnageId);
	}

	public void setDunnageId(String dunnageId) {
		this.dunnageId = dunnageId;
	}	
	
	public int getExpectedQty() {
		return expectedQty;
	}

	public void setExpectedQty(int expectedQty) {
		this.expectedQty = expectedQty;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getDunnageStatus() {
		return StringUtils.trim(dunnageStatus);
	}

	public void setDunnageStatus(String dunnageStatus) {
		this.dunnageStatus = dunnageStatus;
	}

	public String toString() {
        return getId().toString();
    }
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dunnageId == null) ? 0 : dunnageId.hashCode());
		result = prime * result + ((dunnageStatus == null) ? 0 : dunnageStatus.hashCode());
		result = prime * result + expectedQty;
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Dunnage other = (Dunnage) obj;
		if (dunnageId == null) {
			if (other.dunnageId != null) {
				return false;
			}
		} else if (!dunnageId.equals(other.dunnageId)) {
			return false;
		}
		if (dunnageStatus == null) {
			if (other.dunnageStatus != null) {
				return false;
			}
		} else if (!dunnageStatus.equals(other.dunnageStatus)) {
			return false;
		}
		if (expectedQty != other.expectedQty) {
			return false;
		}
		if (productSpecCode == null) {
			if (other.productSpecCode != null) {
				return false;
			}
		} else if (!productSpecCode.equals(other.productSpecCode)) {
			return false;
		}
		return true;
	}

}
