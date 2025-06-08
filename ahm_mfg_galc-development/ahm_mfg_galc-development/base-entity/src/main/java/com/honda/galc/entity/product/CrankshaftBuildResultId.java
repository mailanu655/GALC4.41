package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;

@Embeddable
public class CrankshaftBuildResultId implements Serializable {
	@Column(name="CRANKSHAFT_ID")
	@Tag(name="PRODUCT_ID", alt="CRANKSHAFT_ID")
	private String crankshaftId;

	@Column(name="PART_NAME")
	private String partName;

	private static final long serialVersionUID = 1L;

	public CrankshaftBuildResultId() {
		super();
	}
	
	public CrankshaftBuildResultId(String crankshaftId,String partName) {
		this.crankshaftId = crankshaftId;
		this.partName = partName;
	}

	public String getCrankshaftId() {
		return StringUtils.trim(this.crankshaftId);
	}

	public void setCrankshaftId(String crankshaftId) {
		this.crankshaftId = crankshaftId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof CrankshaftBuildResultId)) {
			return false;
		}
		CrankshaftBuildResultId other = (CrankshaftBuildResultId) o;
		return this.crankshaftId.equals(other.crankshaftId)
			&& this.partName.equals(other.partName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.crankshaftId.hashCode();
		hash = hash * prime + this.partName.hashCode();
		return hash;
	}
	
	public String toString() {
		return getClass().getSimpleName() + "(" + getCrankshaftId() + "," + getPartName() + ")";
	}


}
