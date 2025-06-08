package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_OP_PART_TBX")
public class MCOperationPart extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCOperationPartId id;

	@Column(name="PART_REV", nullable=false)
	private int partRevision;

	public MCOperationPart() {}

	public MCOperationPartId getId() {
		return this.id;
	}

	public void setId(MCOperationPartId id) {
		this.id = id;
	}
	
	public int getPartRevision() {
		return this.partRevision;
	}

	public void setPartRev(int partRevision) {
		this.partRevision = partRevision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + partRevision;
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
		MCOperationPart other = (MCOperationPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (partRevision != other.partRevision)
			return false;
		return true;
	}

	@Override
	public String toString(){
		return toString(getId().getOperationName(), getId().getPartId(), getPartRevision());
	}
}