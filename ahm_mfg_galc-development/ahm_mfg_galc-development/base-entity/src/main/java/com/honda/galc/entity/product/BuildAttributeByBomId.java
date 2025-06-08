package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.dto.Auditable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class BuildAttributeByBomId implements Serializable {

	@Column(name="MODEL_GROUP")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String modelGroup;

	@Column(name="PART_NO")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String partNo;

	@Column(name="PART_COLOR_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
	private String partColorCode;

	@Column(name="ATTRIBUTE")
	@Auditable(isPartOfPrimaryKey= true,sequence=4)
	private String attribute;

	private static final long serialVersionUID = 1L;

	public BuildAttributeByBomId() {
		super();
	}

	public BuildAttributeByBomId(String modelGroup, String partNo, String partColorCode, String attribute) {
		this.modelGroup = modelGroup;
		this.partNo = partNo;
		this.partColorCode = partColorCode;
		this.attribute = attribute;

	}

	public String getModelGroup() {
		return StringUtils.trim(this.modelGroup);
	}

	public void setModelGroup(String modelGroup) {
		this.modelGroup = modelGroup;
	}

	public String getPartNo() {
		return StringUtils.trim(this.partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartColorCode() {
		return StringUtils.trim(this.partColorCode);
	}

	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}

	public String getAttribute() {
		return StringUtils.trim(this.attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BuildAttributeByBomId)) {
			return false;
		}
		BuildAttributeByBomId other = (BuildAttributeByBomId) o;
		return this.modelGroup.equals(other.modelGroup)
				&& this.partNo.equals(other.partNo)
				&& this.partColorCode.equals(other.partColorCode)
				&& this.attribute.equals(other.attribute);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.modelGroup.hashCode();
		hash = hash * prime + this.partNo.hashCode();
		hash = hash * prime + this.partColorCode.hashCode();
		hash = hash * prime + this.attribute.hashCode();
		return hash;
	}
	
	@Override
	public String toString() {
		return  modelGroup +", "+  partNo + ", "
				+ partColorCode + ", " + attribute ;
	}

}
