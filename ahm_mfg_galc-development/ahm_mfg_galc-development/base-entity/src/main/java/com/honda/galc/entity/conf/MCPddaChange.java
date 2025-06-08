package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.pdda.ChangeForm;

/**
 * @author Subu Kathiresan
 * @date Apr 8, 2014
 */
@Entity
@Table(name="MC_PDDA_CHG_TBX")
public class MCPddaChange extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCPddaChangeId id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CHANGE_FORM_ID", nullable=false, insertable=false, updatable=false)
	private ChangeForm changeForm;
	
	public MCPddaChange() {}

	public MCPddaChangeId getId() {
		return this.id;
	}

	public void setId(MCPddaChangeId id) {
		this.id = id;
	}
	
	public ChangeForm getChangeForm() {
		return this.changeForm;
	}

	public void setChangeForm(ChangeForm changeForm) {
		this.changeForm = changeForm;
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
		MCPddaChange other = (MCPddaChange) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getRevisionId(), getId().getChangeFormId());
	}
}
