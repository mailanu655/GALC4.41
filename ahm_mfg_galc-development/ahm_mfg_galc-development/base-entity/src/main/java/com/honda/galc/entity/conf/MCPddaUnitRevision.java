package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Apr 7, 2014
 */
 @Entity
 @Table(name="MC_PDDA_UNIT_REV_TBX")
public class MCPddaUnitRevision extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;
		 
	@EmbeddedId
	private MCPddaUnitRevisionId id;
	
	@Column(name="APVD_PROC_MAINT_ID", nullable=false)   
	private int approvedProcessMaintenanceId;
	
	@Column(name="APVD_UNIT_MAINT_ID", nullable=false)   
	private int approvedUnitMaintenanceId;
	
	public MCPddaUnitRevision() {}

	public MCPddaUnitRevisionId getId() {
		return this.id;
	}

	public void setId(MCPddaUnitRevisionId id) {
		this.id = id;
	}
	
	public int getApprovedProcessMaintenanceId() {
		return approvedProcessMaintenanceId;
	}

	public void setApprovedProcessMaintenanceId(int approvedProcessMaintenanceId) {
		this.approvedProcessMaintenanceId = approvedProcessMaintenanceId;
	}

	public int getApprovedUnitMaintenanceId() {
		return approvedUnitMaintenanceId;
	}

	public void setApprovedUnitMaintenanceId(int approvedUnitMaintenanceId) {
		this.approvedUnitMaintenanceId = approvedUnitMaintenanceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + approvedProcessMaintenanceId;
		result = prime * result + approvedUnitMaintenanceId;
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
		MCPddaUnitRevision other = (MCPddaUnitRevision) obj;
		if (approvedProcessMaintenanceId != other.approvedProcessMaintenanceId)
			return false;
		if (approvedUnitMaintenanceId != other.approvedUnitMaintenanceId)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getOperationName(), getId().getOperationRevision(), getId().getPddaPlatformId(), 
				getId().getUnitNo(), getId().getPddaReport(), getApprovedProcessMaintenanceId(), getApprovedUnitMaintenanceId());
	}
}
