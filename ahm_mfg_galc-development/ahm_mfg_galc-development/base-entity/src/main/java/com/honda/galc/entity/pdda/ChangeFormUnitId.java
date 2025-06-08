package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class ChangeFormUnitId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="APVD_PROC_MAINT_ID", nullable=false)
	private int approvedProcMaintId;

	@Column(name="APVD_UNIT_MAINT_ID", nullable=false)
	private int approvedUnitMaintId;

	@Column(name="CHANGE_FORM_ID", nullable=false)
	private int changeFormId;

    public ChangeFormUnitId() {}
    
	public int getApprovedProcMaintId() {
		return this.approvedProcMaintId;
	}
	
	public void setApprovedProcMaintId(int approvedProcMaintId) {
		this.approvedProcMaintId = approvedProcMaintId;
	}
	
	public int getApprovedUnitMaintId() {
		return this.approvedUnitMaintId;
	}
	
	public void setApprovedUnitMaintId(int approvedUnitMaintId) {
		this.approvedUnitMaintId = approvedUnitMaintId;
	}
	
	public int getChangeFormId() {
		return this.changeFormId;
	}
	
	public void setChangeFormId(int changeFormId) {
		this.changeFormId = changeFormId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + approvedProcMaintId;
		result = prime * result + approvedUnitMaintId;
		result = prime * result + changeFormId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeFormUnitId other = (ChangeFormUnitId) obj;
		if (approvedProcMaintId != other.approvedProcMaintId)
			return false;
		if (approvedUnitMaintId != other.approvedUnitMaintId)
			return false;
		if (changeFormId != other.changeFormId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getApprovedProcMaintId(), getApprovedUnitMaintId(), 
				getChangeFormId());
	}
}