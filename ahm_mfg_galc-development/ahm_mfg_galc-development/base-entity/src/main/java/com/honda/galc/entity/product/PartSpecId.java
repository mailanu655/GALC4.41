package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.dto.Auditable;
import org.apache.commons.lang.StringUtils;

@Embeddable
public class PartSpecId implements Serializable {
	@Column(name="PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String partName;

	@Column(name="PART_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String partId;

	private static final long serialVersionUID = 1L;

	public PartSpecId() {
		super();
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof PartSpecId)) {
			return false;
		}
		PartSpecId other = (PartSpecId) o;
		return this.getPartName().equals(other.getPartName())
			&& this.getPartId().equals(other.getPartId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.getPartName().hashCode();
		hash = hash * prime + this.getPartId().hashCode();
		return hash;
	}
	
	@Override
	public String toString() {
		return  partName + "," + partId;
	}

}
