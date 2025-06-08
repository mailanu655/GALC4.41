package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "BUILD_ATTRIBUTE_GROUP_DEF_TBX")
public class BuildAttributeGroupDefinition extends AuditEntry {

	@Id
	@Column(name = "ATTRIBUTE_GRP")
	private String attributeGroup;

	@Column(name = "SCREEN_ID")
	private String screenId;

	private static final long serialVersionUID = 1L;

	public BuildAttributeGroupDefinition() {
		super();
	}

	public BuildAttributeGroupDefinition(String attributeGroup, String screenId) {
		super();
		this.attributeGroup = attributeGroup;
		this.screenId = screenId;
	}

	public String getAttributeGroup() {
		return StringUtils.trim(this.attributeGroup);
	}

	public void setAttributeGroup(String attributeGroup) {
		this.attributeGroup = attributeGroup;
	}

	public String getScreenId() {
		return StringUtils.trim(this.screenId);
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Override
	public String toString() {
		return super.toString(this.attributeGroup, this.screenId);
	}

	public Object getId() {
		return this.attributeGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeGroup == null) ? 0 : attributeGroup.hashCode());
		result = prime * result + ((screenId == null) ? 0 : screenId.hashCode());
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
		BuildAttributeGroupDefinition other = (BuildAttributeGroupDefinition) obj;
		if (attributeGroup == null) {
			if (other.attributeGroup != null)
				return false;
		} else if (!attributeGroup.equals(other.attributeGroup))
			return false;
		if (screenId == null) {
			if (other.screenId != null)
				return false;
		} else if (!screenId.equals(other.screenId))
			return false;
		return true;
	}
}