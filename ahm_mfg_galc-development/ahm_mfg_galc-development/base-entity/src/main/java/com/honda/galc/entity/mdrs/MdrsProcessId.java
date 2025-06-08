package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the PCLPR1 database table.
 * 
 */
@Embeddable
public class MdrsProcessId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="APVD_PROC_MAINT_ID", unique=true, nullable=false)
	private int apvdProcMaintId;

	@Column(name="TEAM_ID", unique=true, nullable=false)
	private int teamId;

	public MdrsProcessId() {
	}
	public int getApvdProcMaintId() {
		return this.apvdProcMaintId;
	}
	public void setApvdProcMaintId(int apvdProcMaintId) {
		this.apvdProcMaintId = apvdProcMaintId;
	}
	public int getTeamId() {
		return this.teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MdrsProcessId other = (MdrsProcessId)obj;
		if (apvdProcMaintId != other.apvdProcMaintId)
			return false;
		if (teamId != other.teamId)
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.apvdProcMaintId;
		result = prime * result + this.teamId;
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getApvdProcMaintId(), getTeamId());
	}
}