package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;


@Embeddable
public class HoldAccessTypeId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "TYPE_ID")
    private String typeId;

    @Column(name = "SECURITY_GRP")
    private String securityGroup;
    
    @Column(name = "PRODUCT_TYPE")
    private String productType;

    public HoldAccessTypeId() {
        super();
    }
    
    public HoldAccessTypeId(String typeId,String securityGroup) {
        super();
        this.typeId = typeId;
        this.securityGroup = securityGroup;
    }
    
    public HoldAccessTypeId(String typeId, String securityGroup, String productType) {
    	super();
    	this.typeId = typeId;
    	this.securityGroup = securityGroup;
    	this.productType = productType;
    }

   
    public String getTypeId() {
		return StringUtils.trim(typeId);
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSecurityGroup() {
        return StringUtils.trim(this.securityGroup);
    }

    public void setSecurityGroup(String securityGrp) {
        this.securityGroup = securityGrp;
    }
    
    public String getProductType() {
    	return StringUtils.trim(this.productType);
    }
    
    public void setProductType(String productType) {
    	this.productType = productType;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((securityGroup == null) ? 0 : securityGroup.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
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
		HoldAccessTypeId other = (HoldAccessTypeId) obj;
		if (securityGroup == null) {
			if (other.securityGroup != null)
				return false;
		} else if (!securityGroup.equals(other.securityGroup))
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HoldAccessTypeId [typeId=" + typeId + ", securityGroup=" + securityGroup + ", productType=" + productType + "]";
	}


    
    
}
