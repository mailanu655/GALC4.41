package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL724TBX")
public class PhysicalTableMap extends AuditEntry {
	@EmbeddedId
	private PhysicalTableMapId id;

	@Column(name="PHYSICAL_TABLE_NAME")
	private String physicalTableName;

	private static final long serialVersionUID = 1L;

	public PhysicalTableMap() {
		super();
	}
	
	public PhysicalTableMap(PhysicalTableMapId id) {
		super();
		this.id = id;
	}

	public PhysicalTableMapId getId() {
		return this.id;
	}

	public void setId(PhysicalTableMapId id) {
		this.id = id;
	}

	public String getPhysicalTableName() {
		return StringUtils.trim(this.physicalTableName);
	}

	public void setPhysicalTableName(String physicalTableName) {
		this.physicalTableName = physicalTableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((physicalTableName == null) ? 0 : physicalTableName
						.hashCode());
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
		PhysicalTableMap other = (PhysicalTableMap) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (physicalTableName == null) {
			if (other.physicalTableName != null)
				return false;
		} else if (!physicalTableName.equals(other.physicalTableName))
			return false;
		return true;
	}
	
	public String toString() {
		return toString(getId().getProductId(), getId().getTableName(), getPhysicalTableName());
	}
}
