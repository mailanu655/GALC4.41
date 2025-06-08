package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "KICKOUT_LOCATION_TBX")
public class KickoutLocation extends AuditEntry {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "KICKOUT_ID")
	private Long kickoutId;

	@Column(name = "DIVISION_ID")
	private String divisionId;

	@Column (name = "LINE_ID")
	private String lineId;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;

	public Long getId() {
		return getKickoutId();
	}
	public Long getKickoutId() {
		return this.kickoutId;
	}

	public void setKickoutId(Long kickoutId) {
		this.kickoutId = kickoutId;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return StringUtils.trim(this.lineId);
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((kickoutId == null) ? 0 : kickoutId.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
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
		KickoutLocation other = (KickoutLocation) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (kickoutId == null) {
			if (other.kickoutId != null)
				return false;
		} else if (!kickoutId.equals(other.kickoutId))
			return false;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "KickoutLocation [kickoutId=" + kickoutId + ", divisionId=" + divisionId + ", lineId=" + lineId
				+ ", processPointId=" + processPointId + "]";
	}
}
