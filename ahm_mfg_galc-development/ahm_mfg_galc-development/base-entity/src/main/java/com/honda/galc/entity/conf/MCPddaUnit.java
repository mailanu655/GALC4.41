package com.honda.galc.entity.conf;

import java.io.Serializable;
import com.honda.galc.entity.AuditEntry;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The persistent class for the MC_PDDA_UNIT_TBX database table.
 * 
 */
@Entity
@Table(name="MC_PDDA_UNIT_TBX")
public class MCPddaUnit extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCPddaUnitId id;

	@Column(name="APPROVED")
	private Timestamp approved;

	@Column(name="APPROVER_NO", length=11)
	private String approverNo;

	@Column(name="DEPRECATED")
	private Timestamp deprecated;

	@Column(name="DEPRECATED_REV_ID")
	private long deprecatedRevId;

	@Column(name="DEPRECATER_NO", length=11)
	private String deprecaterNo;

    public MCPddaUnit() {
    }

	public MCPddaUnitId getId() {
		return this.id;
	}

	public void setId(MCPddaUnitId id) {
		this.id = id;
	}
	
	public Timestamp getApproved() {
		return this.approved;
	}

	public void setApproved(Timestamp approved) {
		this.approved = approved;
	}

	public String getApproverNo() {
		return this.approverNo;
	}

	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}

	public Timestamp getDeprecated() {
		return this.deprecated;
	}

	public void setDeprecated(Timestamp deprecated) {
		this.deprecated = deprecated;
	}

	public long getDeprecatedRevId() {
		return this.deprecatedRevId;
	}

	public void setDeprecatedRevId(long deprecatedRevId) {
		this.deprecatedRevId = deprecatedRevId;
	}

	public String getDeprecaterNo() {
		return this.deprecaterNo;
	}

	public void setDeprecaterNo(String deprecaterNo) {
		this.deprecaterNo = deprecaterNo;
	}

	@Override
	public String toString() {
		return toString(getId().getOperationName(), getId().getUnitNo(), getApproverNo(), 
				getApproved(), getDeprecaterNo(), getDeprecated());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approved == null) ? 0 : approved.hashCode());
		result = prime * result
				+ ((approverNo == null) ? 0 : approverNo.hashCode());
		result = prime * result
				+ ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result
				+ (int) (deprecatedRevId ^ (deprecatedRevId >>> 32));
		result = prime * result
				+ ((deprecaterNo == null) ? 0 : deprecaterNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MCPddaUnit other = (MCPddaUnit) obj;
		if (approved == null) {
			if (other.approved != null) {
				return false;
			}
		} else if (!approved.equals(other.approved)) {
			return false;
		}
		if (approverNo == null) {
			if (other.approverNo != null) {
				return false;
			}
		} else if (!approverNo.equals(other.approverNo)) {
			return false;
		}
		if (deprecated == null) {
			if (other.deprecated != null) {
				return false;
			}
		} else if (!deprecated.equals(other.deprecated)) {
			return false;
		}
		if (deprecatedRevId != other.deprecatedRevId) {
			return false;
		}
		if (deprecaterNo == null) {
			if (other.deprecaterNo != null) {
				return false;
			}
		} else if (!deprecaterNo.equals(other.deprecaterNo)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	
}