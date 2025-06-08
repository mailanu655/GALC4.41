package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;

@Embeddable
public class ConrodBuildResultId implements Serializable {
	@Column(name="CONROD_ID")
	@Tag(name="PRODUCT_ID", alt="CONROD_ID")
	private String conrodId;

	@Column(name="PART_NAME")
	private String partName;

	private static final long serialVersionUID = 1L;

	public ConrodBuildResultId() {
		super();
	}
	
	public ConrodBuildResultId(String conrodId,String partName) {
		this.conrodId = conrodId;
		this.partName = partName;
	}

	public String getConrodId() {
		return StringUtils.trim(this.conrodId);
	}

	public void setConrodId(String conrodId) {
		this.conrodId = conrodId;
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
		if ( ! (o instanceof ConrodBuildResultId)) {
			return false;
		}
		ConrodBuildResultId other = (ConrodBuildResultId) o;
		return this.conrodId.equals(other.conrodId)
			&& this.partName.equals(other.partName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.conrodId.hashCode();
		hash = hash * prime + this.partName.hashCode();
		return hash;
	}
	
	public String toString() {
		return getClass().getSimpleName() + "(" + getConrodId() + "," + getPartName() + ")";
	}


}
