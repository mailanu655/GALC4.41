package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the LOT_PART database table.
 * 
 */
@Embeddable
public class LotPartId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PRODUCTION_LOT", unique=true, nullable=false, length=20)
	private String productionLot;

	@Column(name="LET_SYSTEM_NAME", unique=true, nullable=false, length=255)
	private String letSystemName;

	@Column(name="DC_PART_NUMBER", unique=true, nullable=false, length=18)
	private String dcPartNumber;

	public LotPartId() {
	}
	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
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
		if (!(other instanceof LotPartId)) {
			return false;
		}
		LotPartId castOther = (LotPartId)other;
		return 
			this.productionLot.equals(castOther.productionLot)
			&& this.letSystemName.equals(castOther.letSystemName)
			&& this.dcPartNumber.equals(castOther.dcPartNumber);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productionLot.hashCode();
		hash = hash * prime + this.letSystemName.hashCode();
		hash = hash * prime + this.dcPartNumber.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				getProductionLot(), getLetSystemName(), getDcPartNumber());
	}
}