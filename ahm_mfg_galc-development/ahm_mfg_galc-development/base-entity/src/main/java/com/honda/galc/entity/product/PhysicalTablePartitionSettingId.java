package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class PhysicalTablePartitionSettingId implements Serializable {
	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="PHYSICAL_TABLE_OFFSET")
	private int physicalTableOffset;

	private static final long serialVersionUID = 1L;

	public PhysicalTablePartitionSettingId() {
		super();
	}

	public String getTableName() {
		return StringUtils.trim(this.tableName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getPhysicalTableOffset() {
		return this.physicalTableOffset;
	}

	public void setPhysicalTableOffset(int physicalTableOffset) {
		this.physicalTableOffset = physicalTableOffset;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof PhysicalTablePartitionSettingId)) {
			return false;
		}
		PhysicalTablePartitionSettingId other = (PhysicalTablePartitionSettingId) o;
		return this.tableName.equals(other.tableName)
			&& (this.physicalTableOffset == other.physicalTableOffset);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tableName.hashCode();
		hash = hash * prime + this.physicalTableOffset;
		return hash;
	}

}
