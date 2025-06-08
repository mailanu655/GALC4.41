package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.UserAuditEntry;

@Embeddable
public class BuildAttributeId implements Serializable {
	
	@Column(name="ATTRIBUTE")
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String attribute;

	@Column(name="PRODUCT_SPEC_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String productSpecCode;
	
	@Column(name = "SUB_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
    private String subId="";

	private static final long serialVersionUID = 1L;

	public BuildAttributeId() {
		super();
	}
	
	public BuildAttributeId(String attribute, String productSpecCode) {
		this(attribute, productSpecCode,"");
			
	}
	
	public BuildAttributeId(String attribute, String productSpecCode, String subId) {
		this.attribute = attribute;
		this.productSpecCode = productSpecCode;
		this.subId = (subId == null)? "":subId;
			
	}

	public String getAttribute() {
		return StringUtils.trim(this.attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public String getSubId() {
		return StringUtils.trim(subId);
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BuildAttributeId)) {
			return false;
		}
		BuildAttributeId other = (BuildAttributeId) o;
		return getAttribute().equals(other.getAttribute())
			&& getProductSpecCode().equals(other.getProductSpecCode())
			&& getSubId().equals(other.getSubId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + getAttribute().hashCode();
		hash = hash * prime + getProductSpecCode().hashCode();
		hash = hash * prime + getSubId().hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return  attribute + "," + productSpecCode + "," + subId;
	}

}
