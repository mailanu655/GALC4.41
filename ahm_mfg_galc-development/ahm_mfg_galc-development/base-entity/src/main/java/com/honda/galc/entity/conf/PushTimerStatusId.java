package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Subu Kathiresan
 * @date Jul 31, 2015
 */
@Embeddable
public class PushTimerStatusId implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Column(name="PROCESS_POINT_ID", nullable = false, length = 16)
	private String processPointId;
	
	@Column(name="TERMINAL_ID", nullable = false)
	private String terminalId;
	
	public PushTimerStatusId() {}
	
	public PushTimerStatusId(String processPointId, String terminalId) {
		this.processPointId = processPointId;
		this.terminalId = terminalId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((terminalId == null) ? 0 : terminalId.hashCode());
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
		PushTimerStatusId other = (PushTimerStatusId) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (terminalId == null) {
			if (other.terminalId != null)
				return false;
		} else if (!terminalId.equals(other.terminalId))
			return false;
		return true;
	}
}
