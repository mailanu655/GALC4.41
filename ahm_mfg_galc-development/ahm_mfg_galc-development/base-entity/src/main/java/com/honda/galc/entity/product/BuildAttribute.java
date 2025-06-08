package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.UserAuditEntry;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.UserAuditEntry;

@Entity
@Table(name="GAL259TBX")
public class BuildAttribute extends UserAuditEntry {
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private BuildAttributeId id;

	@Column(name="ATTRIBUTE_VALUE")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
	private String attributeValue;

	@Column(name="ATTRIBUTE_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private String attributeDescription;
	
	@Column(name="PRODUCT_TYPE")
	@Auditable(isPartOfPrimaryKey= false,sequence=4)
	private String productType;
	
	private static final long serialVersionUID = 1L;

	public BuildAttribute() {
		super();
	}
	
	public BuildAttribute(String productSpecCode,String attribute,String attributeValue) {
		this(productSpecCode,attribute,"",attributeValue,null);
	}
	
	public BuildAttribute(String productSpecCode,String attribute,String attributeValue,String attributeDescription) {
		this(productSpecCode,attribute,"",attributeValue,attributeDescription);
	}

	public BuildAttribute(String productSpecCode, String attribute, String subId, String attributeValue, String attributeDescription) {
		this.id = new BuildAttributeId();
		id.setProductSpecCode(productSpecCode);
		id.setAttribute(attribute);
		id.setSubId(subId);
		this.attributeValue = attributeValue;
		this.attributeDescription = attributeDescription;
	}

	public BuildAttributeId getId() {
		return this.id;
	}

	public void setId(BuildAttributeId id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return StringUtils.trim(this.attributeValue);
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAttributeDescription() {
		return  StringUtils.trim(attributeDescription);
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}
	
	public String getAttribute() {
		return getId().getAttribute();
	}
	
	public String getProductSpecCode() {
		
		return getId().getProductSpecCode();
		
	}
	
	public String getModelYearCode() {
		
		return ProductSpec.extractModelYearCode(getProductSpecCode());
		
	}
	
	public String getModelCode() {
		
		return ProductSpec.extractModelCode(getProductSpecCode());
		
	}
	
	public String getModelTypeCode() {
		
		return ProductSpec.extractModelTypeCode(getProductSpecCode());
		
	}

	public String getModelOptionCode() {
		
		return ProductSpec.extractModelOptionCode(getProductSpecCode());
		
	}
	
	public String getExtColorCode() {
		
		return ProductSpec.extractExtColorCode(getProductSpecCode());
		
	}
	
	public String getIntColorCode() {
		
		return ProductSpec.extractIntColorCode(getProductSpecCode());
		
	}
	
		
	public String getProductType() {
		return StringUtils.trim(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getTag(){
		if(StringUtils.isEmpty(getId().getSubId()))
			return getId().getAttribute();
		else
			return getId().getAttribute() + Delimiter.DOT + getId().getSubId();
	}

	public String toString() {
		return toString(
				getId().getProductSpecCode(),
				getId().getAttribute(),
				getId().getSubId(),
				getAttributeValue(),
				getAttributeDescription(),
				getProductType()
				);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((attributeDescription == null) ? 0 : attributeDescription
						.hashCode());
		result = prime * result
				+ ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildAttribute other = (BuildAttribute) obj;
		if (attributeDescription == null) {
			if (other.attributeDescription != null)
				return false;
		} else if (!attributeDescription.equals(other.attributeDescription))
			return false;
		if (attributeValue == null) {
			if (other.attributeValue != null)
				return false;
		} else if (!attributeValue.equals(other.attributeValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		return true;
	}
	
}
