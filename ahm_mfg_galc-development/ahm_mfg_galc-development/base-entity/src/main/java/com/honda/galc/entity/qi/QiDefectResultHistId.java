package com.honda.galc.entity.qi;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QiDefectResultHistId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name = "CHANGE_TIMESTAMP")
	private Date changeTimestamp;

	public QiDefectResultHistId() {
		super();
	}
	
	public QiDefectResultHistId(long defectResultId) {
		this.defectResultId = defectResultId;
		this.changeTimestamp = new Date();
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
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
		result = prime * result + (int)defectResultId;
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
		
		QiDefectResultHistId other = (QiDefectResultHistId) obj;
		if (changeTimestamp == null) {
			if (other.changeTimestamp != null)
				return false;
		} else if (!changeTimestamp.equals(other.changeTimestamp))
			return false;
		if (defectResultId != other.defectResultId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiDefectResultHistId [defectResultId=" + defectResultId + ", changeTimestamp=" + changeTimestamp + "]";
	}
}
