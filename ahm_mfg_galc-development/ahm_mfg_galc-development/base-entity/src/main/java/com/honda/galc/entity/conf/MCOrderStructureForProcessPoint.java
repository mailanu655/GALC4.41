package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * @author Fredrick Yessaian
 * Apr 01, 2016 : New Entity
 * 
 * 
 */
@Entity
@Table(name="MC_ORDER_STRU_FOR_PROCESS_POINT_TBX")
public class MCOrderStructureForProcessPoint extends BaseMCOrderStructure {

	private static final long serialVersionUID = 1L;
	
	private static final String mode = "PROCESS_POINT_MODE";

	@EmbeddedId
	private MCOrderStructureForProcessPointId id;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name="STRUCTURE_REV")
	private long structureRevision;
	
	@Column(name="DIVISION_ID", nullable=false, length=16)
	private String divisionId;

    public MCOrderStructureForProcessPoint() {}

	public String getMode() {
		return mode;
	}
    
	public MCOrderStructureForProcessPointId getId() {
		return this.id;
	}

	public void setId(MCOrderStructureForProcessPointId id) {
		this.id = id;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public long getStructureRevision() {
		return this.structureRevision;
	}

	public void setStructureRevision(long structureRevision) {
		this.structureRevision = structureRevision;
	}
	
	public String getDivisionId() {
		return StringUtils.trim(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result
				+ (int) (structureRevision ^ (structureRevision >>> 32));
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
		MCOrderStructureForProcessPoint other = (MCOrderStructureForProcessPoint) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (structureRevision != other.structureRevision)
			return false;
		return true;
	}


	@Override
	public String toString(){
		return toString(getId().getOrderNo(), getId().getProcessPointId(), getProductSpecCode(), getStructureRevision(), getDivisionId());
	}	
}
