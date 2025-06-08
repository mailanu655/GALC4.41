package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.dto.Auditable;

@Entity
@Table(name="BUILD_ATTRIBUTE_BY_BOM_TBX")
public class BuildAttributeByBom extends AuditEntry {
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private BuildAttributeByBomId id;

	@Column(name="ATTRIBUTE_VALUE")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
	private String attributeValue;

	@Column(name="ATTRIBUTE_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private String attributeDescription;

	private static final long serialVersionUID = 1L;

	public BuildAttributeByBom() {
		super();
	}

	public BuildAttributeByBom(String modelGroup, String partNo, String partColorCode, String attribute, String attributeValue, String attributeDescription) {
		this.id = new BuildAttributeByBomId(modelGroup, partNo, partColorCode, attribute);
		this.attributeValue = attributeValue;
		this.attributeDescription = attributeDescription;
	}

	public BuildAttributeByBomId getId() {
		return this.id;
	}

	public void setId(BuildAttributeByBomId id) {
		this.id = id;
	}

	public String getModelGroup() {
		return this.id.getModelGroup();
	}

	public void setModelGroup(String modelGroup) {
		this.id.setModelGroup(modelGroup);
	}

	public String getPartNo() {
		return this.id.getPartNo();
	}

	public void setPartNo(String partNo) {
		this.id.setPartNo(partNo);
	}

	public String getPartColorCode() {
		return this.id.getPartColorCode();
	}

	public void setPartColorCode(String partColorCode) {
		this.id.setPartColorCode(partColorCode);
	}

	public String getAttribute() {
		return this.id.getAttribute();
	}

	public void setAttribute(String attribute) {
		this.id.setAttribute(attribute);
	}

	public String getAttributeValue() {
		return StringUtils.trim(this.attributeValue);
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAttributeDescription() {
		return StringUtils.trim(this.attributeDescription);
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	public String toString() {
		return toString(
				this.id.getModelGroup(),
				this.id.getPartNo(),
				this.id.getPartColorCode(),
				this.id.getAttribute(),
				this.attributeValue,
				this.attributeDescription
				);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BuildAttributeByBom other = (BuildAttributeByBom) obj;
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
		return true;
	}
}
