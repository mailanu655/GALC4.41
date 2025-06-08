package com.honda.galc.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/** * * 
* @author Xiaomei Ma
* @since Feb 27, 2020
*/
@Embeddable
public class CrankshaftBuildResultHistoryId implements Serializable {
	@Column(name="CRANKSHAFT_ID")
	private String crankshaftId;

	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	private static final long serialVersionUID = 1L;

	public CrankshaftBuildResultHistoryId() {
		super();
	}

	public CrankshaftBuildResultHistoryId(String crankshaftId, String partName, Date actualTimestamp) {
		super();
		this.crankshaftId = crankshaftId;
		this.partName = partName;
		this.actualTimestamp = actualTimestamp;
	}

	public String getCrankshaftId() {
		return crankshaftId;
	}

	public void setCrankshaftId(String crankshaftId) {
		this.crankshaftId = crankshaftId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((crankshaftId == null) ? 0 : crankshaftId.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
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
		CrankshaftBuildResultHistoryId other = (CrankshaftBuildResultHistoryId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (crankshaftId == null) {
			if (other.crankshaftId != null)
				return false;
		} else if (!crankshaftId.equals(other.crankshaftId))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CrankshaftBuildResultHistoryId [crankshaftId=" + crankshaftId + ", partName=" + partName
				+ ", actualTimestamp=" + actualTimestamp + "]";
	}
	
}
