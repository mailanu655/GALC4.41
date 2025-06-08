package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MC_MDRS_MANPOWER_ASSIGNMENT_TBX database table.
 * 
 */
@Embeddable
public class MCMdrsManpowerAssignmentId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="MANPOWER_ASSIGNMENT_ID")
	private long manpowerAssignmentId;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="LAST_UPDATED_DATE", nullable=false)
	private Timestamp lastUpdatedDate;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Timestamp extractDate;

	public MCMdrsManpowerAssignmentId() {
	}
	public long getManpowerAssignmentId() {
		return this.manpowerAssignmentId;
	}
	public void setManpowerAssignmentId(long manpowerAssignmentId) {
		this.manpowerAssignmentId = manpowerAssignmentId;
	}
	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Timestamp getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}
	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public Timestamp getExtractDate() {
		return this.extractDate;
	}
	public void setExtractDate(Timestamp extractDate) {
		this.extractDate = extractDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCMdrsManpowerAssignmentId other = (MCMdrsManpowerAssignmentId) obj;
		if (manpowerAssignmentId != other.manpowerAssignmentId)
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (lastUpdatedDate == null) {
			if (other.lastUpdatedDate != null)
				return false;
		} else if (!lastUpdatedDate.equals(other.lastUpdatedDate))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((int) (manpowerAssignmentId ^ (manpowerAssignmentId >>> 32)));
		result = prime * result
				+ ((lastUpdatedDate == null) ? 0 : lastUpdatedDate.hashCode());
		result = prime * result + ((extractDate == null) ? 0 : extractDate.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getPlantLocCode(), getDeptCode()
				, getManpowerAssignmentId(), getLastUpdatedDate(), getExtractDate());
	}
}