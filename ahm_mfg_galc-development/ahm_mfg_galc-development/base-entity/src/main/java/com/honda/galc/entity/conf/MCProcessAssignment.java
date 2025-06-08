package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the MC_PROCESS_ASSIGNMENT_TBX database table.
 * 
 */
@Entity
@Table(name="MC_PROCESS_ASSIGNMENT_TBX")
public class MCProcessAssignment extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCProcessAssignmentId id;

	@Column(name="APPROVE_METHOD", length=16)
	private String approveMethod;

	@Column(name="APPROVER_NO", length=11)
	private String approverNo;

	@Column(name="COMMENTS", length=250)
	private String comments;

	public MCProcessAssignment() {
	}

	public MCProcessAssignmentId getId() {
		return this.id;
	}

	public void setId(MCProcessAssignmentId id) {
		this.id = id;
	}

	public String getApproveMethod() {
		return StringUtils.trim(this.approveMethod);
	}

	public void setApproveMethod(String approveMethod) {
		this.approveMethod = approveMethod;
	}

	public String getApproverNo() {
		return StringUtils.trim(this.approverNo);
	}

	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}

	public String getComments() {
		return StringUtils.trim(this.comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approveMethod == null) ? 0 : approveMethod.hashCode());
		result = prime * result
				+ ((approverNo == null) ? 0 : approverNo.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		MCProcessAssignment other = (MCProcessAssignment) obj;
		if (approveMethod == null) {
			if (other.approveMethod != null)
				return false;
		} else if (!approveMethod.equals(other.approveMethod))
			return false;
		if (approverNo == null) {
			if (other.approverNo != null)
				return false;
		} else if (!approverNo.equals(other.approverNo))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
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
		return toString(getId().getPddaPlatformId(), getId().getProcessPointId(), getId().getLineNo(), getId().getPlantCode()
				, getId().getProductionDate(), getId().getProcessLocation(), getId().getPeriod(), getId().getShift()
				, getApproveMethod(), getApproverNo(), getComments());
	}

}