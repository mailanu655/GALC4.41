package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_OP_MATRIX_TBX")
public class MCOperationMatrix extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCOperationMatrixId id;
	
    public MCOperationMatrix() {}

	public MCOperationMatrixId getId() {
		return this.id;
	}

	public void setId(MCOperationMatrixId id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MCOperationMatrix other = (MCOperationMatrix) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return toString(getId().getOperationName(), getId().getOperationRevision(), getId().getSpecCodeType(), getId().getSpecCodeMask());
	}
}