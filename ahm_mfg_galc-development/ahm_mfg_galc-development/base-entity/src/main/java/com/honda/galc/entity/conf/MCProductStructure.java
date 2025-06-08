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
 * @author Fredrick Yessaian
 * Mar 04, 2015 :: Modified to include division id & PK alteration
 * 
 * @author Fredrick Yessaian
 * Apr 01, 2016 :: Modified to extend abstract class
 * 
 */
@Entity
@Table(name="MC_PRODUCT_STRUCTURE_TBX")
public class MCProductStructure extends BaseMCProductStructure {
	
	private static final long serialVersionUID = 1L;
	
	private static final String mode = "DIVISION_MODE";

	@EmbeddedId
	private MCProductStructureId id;

	@Column(name="STRUCTURE_REV", nullable=false)
	private long structureRevision;

    public MCProductStructure() {}

	public void setId(MCProductStructureId id) {
		this.id = id;
	}

	public long getStructureRevision() {
		return this.structureRevision;
	}

	public void setStructureRevision(long structureRevision) {
		this.structureRevision = structureRevision;
	}
	
	public String getMode() {
		return mode;
	}

	public MCProductStructureId getId() {
		return this.id;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.getId().getProductSpecCode());
	}
	
	@Override
	public String toString(){
		return toString(getId().getProductId(), getId().getDivisionId(), getId().getProductSpecCode(), getStructureRevision());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof MCProductStructure))
			return false;
		MCProductStructure other = (MCProductStructure) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (structureRevision != other.structureRevision)
			return false;
		return true;
	}

	
}