package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;

@Embeddable
public class PartLinkId implements Serializable {
	@Column(name="PARENT_PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String parentPartName;
	
	@Column(name="PRODUCT_SPEC_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String productSpecCode;
	
	@Column(name="CHILD_PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
	private String childPartName;
	
	private static final long serialVersionUID = 1L;

	public PartLinkId() {
		super();
	}
	
	public PartLinkId(String parentPartName, String productSpecCode, String childPartName) {
		super();
		this.parentPartName = parentPartName;
		this.productSpecCode = productSpecCode;
		this.childPartName = childPartName;
	}

	public String getParentPartName() {
		return StringUtils.trim(parentPartName);
	}

	public void setParentPartName(String parentPartName) {
		this.parentPartName = parentPartName;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getChildPartName() {
		return StringUtils.trim(childPartName);
	}

	public void setChildPartName(String childPartName) {
		this.childPartName = childPartName;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstalledPartId)) {
            return false;
        }
        PartLinkId other = (PartLinkId) o;
        return this.getParentPartName().equals(other.getParentPartName())
                && this.getProductSpecCode().equals(other.getProductSpecCode())
                && this.getChildPartName().equals(other.getChildPartName());
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getParentPartName().hashCode();
		hash = hash * prime + this.getProductSpecCode().hashCode();
		hash = hash * prime + this.getChildPartName().hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return  parentPartName + "," + productSpecCode
				+ "," + childPartName;
	}
}