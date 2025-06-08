package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MODEL_PART database table.
 * 
 */
@Embeddable
public class ModelPartId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PRODUCT_SPEC_WILDCARD", unique=true, nullable=false, length=30)
	private String productSpecWildcard;

	@Column(name="LET_SYSTEM_NAME", unique=true, nullable=false, length=255)
	private String letSystemName;

	@Column(name="DC_PART_NUMBER", unique=true, nullable=false, length=18)
	private String dcPartNumber;

	public ModelPartId() {
	}
	public String getProductSpecWildcard() {
		return StringUtils.trim(this.productSpecWildcard);
	}
	public void setProductSpecWildcard(String productSpecWildcard) {
		this.productSpecWildcard = productSpecWildcard;
	}
	public String getLetSystemName() {
		return StringUtils.trim(this.letSystemName);
	}
	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}
	public String getDcPartNumber() {
		return StringUtils.trim(this.dcPartNumber);
	}
	public void setDcPartNumber(String dcPartNumber) {
		this.dcPartNumber = dcPartNumber;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ModelPartId)) {
			return false;
		}
		ModelPartId castOther = (ModelPartId)other;
		return 
			this.productSpecWildcard.equals(castOther.productSpecWildcard)
			&& this.letSystemName.equals(castOther.letSystemName)
			&& this.dcPartNumber.equals(castOther.dcPartNumber);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productSpecWildcard.hashCode();
		hash = hash * prime + this.letSystemName.hashCode();
		hash = hash * prime + this.dcPartNumber.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				getProductSpecWildcard(), getLetSystemName(), getDcPartNumber());
	}
}