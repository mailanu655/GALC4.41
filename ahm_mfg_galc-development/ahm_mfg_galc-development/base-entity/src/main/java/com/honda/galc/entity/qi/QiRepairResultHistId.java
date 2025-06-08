package com.honda.galc.entity.qi;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QiRepairResultHistId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "REPAIR_ID", nullable=false)
	private long repairId;
	
	@Column(name = "CHANGE_TIMESTAMP")
	private Date changeTimestamp;

	public QiRepairResultHistId() {
		super();
	}
	
	public QiRepairResultHistId(long repairId) {
		this.repairId = repairId;
		this.changeTimestamp = new Date();
	}

	public long getRepairId() {
		return repairId;
	}

	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}

	public Date getChangeTimestamp() {
		return changeTimestamp;
	}

	public void setChangeTimestamp(Date changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)repairId;
		result = prime * result + ((changeTimestamp == null) ? 0 : changeTimestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		QiRepairResultHistId other = (QiRepairResultHistId) obj;
		if (changeTimestamp == null) {
			if (other.changeTimestamp != null)
				return false;
		} else if (!changeTimestamp.equals(other.changeTimestamp))
			return false;
		if (repairId != other.repairId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiRepairResultHistId [repairId=" + repairId + ", changeTimestamp=" + changeTimestamp + "]";
	}
}
