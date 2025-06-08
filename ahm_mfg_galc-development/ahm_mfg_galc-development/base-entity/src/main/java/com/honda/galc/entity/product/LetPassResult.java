package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Mar 02, 2018
 */

@Entity
@Table(name="GAL725TBX")
public class LetPassResult extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LetPassResultId id;

	@Column(name="PASS_STATUS")
	private int passStatus;

	public LetPassResult() {
		super();
	}

	public LetPassResult(LetPassResultId id) {
		this.id = id;
	}
	
	public Object getId() {
		return id;
	}
	
	public int getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(int passStatus) {
		this.passStatus = passStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + passStatus;
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
		LetPassResult other = (LetPassResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (passStatus != other.passStatus)
			return false;
		return true;
	}
}

