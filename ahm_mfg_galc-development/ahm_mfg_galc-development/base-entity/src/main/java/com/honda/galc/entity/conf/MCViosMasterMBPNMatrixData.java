package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * Entity implementation class for Entity: MCViosMasterMBPNMatrixData
 *
 */
@Entity
@Table(name="MC_VIOS_MASTER_MBPN_MATRIX_TBX")
public class MCViosMasterMBPNMatrixData extends AuditEntry {

	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCViosMasterMBPNMatrixDataId id;
	
	@Column(name = "USER_ID")
	private String userId;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterMBPNMatrixData() {
	}

	public MCViosMasterMBPNMatrixDataId getId() {
		return id;
	}

	public void setId(MCViosMasterMBPNMatrixDataId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		MCViosMasterMBPNMatrixData other = (MCViosMasterMBPNMatrixData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	
}
