package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * The persistent class for the LET_PARTIAL_CHECK database table.
 * 
 */
@Entity
@Table(name="LET_PARTIAL_CHECK", schema="LCVINBOM")
public class LetPartialCheck extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetPartialCheckId id;

	public LetPartialCheck() {
	}

	public LetPartialCheckId getId() {
		return this.id;
	}

	public void setId(LetPartialCheckId id) {
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
		LetPartialCheck other = (LetPartialCheck) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getCategoryCodeId(), getId().getLetInspectionName());
	}
	
	

}