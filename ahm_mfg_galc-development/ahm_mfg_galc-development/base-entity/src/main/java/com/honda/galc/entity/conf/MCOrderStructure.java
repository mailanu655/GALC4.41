package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 * 
 * 
 * @author Fredrick Yessaian
 * Mar 04 2015 :: Modified to include division id
 * 
 * @author Fredrick Yessaian
 * Apr 01, 2016 :: Modified to extend abstract class
 * 
 */
@Entity
@Table(name="MC_ORDER_STRUCTURE_TBX")
public class MCOrderStructure extends BaseMCOrderStructure {

	private static final long serialVersionUID = 1L;
	
	private static final String mode = "DIVISION_MODE";

	@EmbeddedId
	private MCOrderStructureId id;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name="STRUCTURE_REV")
	private long structureRevision;

    public MCOrderStructure() {}

    
	public MCOrderStructureId getId() {
		return this.id;
	}

	public void setId(MCOrderStructureId id) {
		this.id = id;
	}

	public String getProductSpecCode() {
		return this.productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public long getStructureRevision() {
		return this.structureRevision;
	}
	
	public String getMode() {
		return StringUtils.trim(mode);
	}

	public void setStructureRevision(long structureRevision) {
		this.structureRevision = structureRevision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		MCOrderStructure other = (MCOrderStructure) obj;
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
		return toString(getId().getOrderNo(), getId().getDivisionId(), getProductSpecCode(), getStructureRevision());
	}

}
