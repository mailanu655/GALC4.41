package com.honda.galc.entity.mdrs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date May 27, 2014
 */
@Embeddable
public class MdrsIndependentProcessId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="PROCESS_ID", nullable=false)
	private int processId;
	
	@Column(name="TEAM_ID", nullable=false)
	private int teamId;
	
	public MdrsIndependentProcessId() {}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + processId;
		result = prime * result + teamId;
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
		MdrsIndependentProcessId other = (MdrsIndependentProcessId) obj;
		if (processId != other.processId)
			return false;
		if (teamId != other.teamId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getProcessId(), getTeamId());
	}
}
