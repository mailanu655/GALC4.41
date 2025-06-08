package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.dto.Auditable;

@Entity
@Table(name = "BUILD_ATTRIBUTE_DEF_TBX")
public class BuildAttributeDefinition extends AuditEntry {

	@Id
	@Column(name = "ATTRIBUTE")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String attribute;

	@Column(name = "ATTRIBUTE_LABEL")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
	private String attributeLabel;

	@Column(name = "ATTRIBUTE_GRP")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private String attributeGroup;

	@Column(name = "AUTO_UPDATE")
	@Auditable(isPartOfPrimaryKey= false,sequence=4)
	private String autoUpdate;

	private static final long serialVersionUID = 1L;

	public BuildAttributeDefinition() {
		super();
	}

	public BuildAttributeDefinition(String attribute, String attributeLabel, String attributeGroup, String autoUpdate) {
		super();
		this.attribute = attribute;
		this.attributeLabel = attributeLabel;
		this.attributeGroup = attributeGroup;
		this.autoUpdate = autoUpdate;
	}

	public String getAttribute() {
		return StringUtils.trim(this.attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeLabel() {
		return StringUtils.trim(this.attributeLabel);
	}

	public void setAttributeLabel(String attributeLabel) {
		this.attributeLabel = attributeLabel;
	}

	public String getAttributeGroup() {
		return StringUtils.trim(this.attributeGroup);
	}

	public void setAttributeGroup(String attributeGroup) {
		this.attributeGroup = attributeGroup;
	}

	public String getAutoUpdate() {
		return StringUtils.trim(this.autoUpdate);
	}

	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public String toString() {
		return super.toString(this.attribute, this.attributeLabel, this.attributeGroup, this.autoUpdate);
	}

	public Object getId() {
		return this.attribute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((attributeGroup == null) ? 0 : attributeGroup.hashCode());
		result = prime * result + ((attributeLabel == null) ? 0 : attributeLabel.hashCode());
		result = prime * result + ((autoUpdate == null) ? 0 : autoUpdate.hashCode());
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
		BuildAttributeDefinition other = (BuildAttributeDefinition) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (attributeGroup == null) {
			if (other.attributeGroup != null)
				return false;
		} else if (!attributeGroup.equals(other.attributeGroup))
			return false;
		if (attributeLabel == null) {
			if (other.attributeLabel != null)
				return false;
		} else if (!attributeLabel.equals(other.attributeLabel))
			return false;
		if (autoUpdate == null) {
			if (other.autoUpdate != null)
				return false;
		} else if (!autoUpdate.equals(other.autoUpdate))
			return false;
		return true;
	}
}