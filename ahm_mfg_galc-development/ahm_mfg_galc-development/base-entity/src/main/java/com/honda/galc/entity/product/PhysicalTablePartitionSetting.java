package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL723TBX")
public class PhysicalTablePartitionSetting extends AuditEntry implements Serializable{
	@EmbeddedId
	private PhysicalTablePartitionSettingId id;

	@Column(name="PHYSICAL_TABLE_NAME")
	private String physicalTableName;

	private static final long serialVersionUID = 1L;

	public PhysicalTablePartitionSetting() {
		super();
	}

	public PhysicalTablePartitionSettingId getId() {
		return id;
	}

	public void setId(PhysicalTablePartitionSettingId id) {
		this.id = id;
	}

	public String getPhysicalTableName() {
		return StringUtils.trim(this.physicalTableName);
	}

	public void setPhysicalTableName(String physicalTableName) {
		this.physicalTableName = physicalTableName;
	}

}
