package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the ROTATION database table.
 * 
 */
@Embeddable
public class RotationId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ROTATION_ID", unique=true, nullable=false)
	private int rotationId;

	@Column(name="PLANT_LOC_CODE", unique=true, nullable=false, length=1)
	private String plantLocCode;

	@Column(name="DEPT_CODE", unique=true, nullable=false, length=2)
	private String deptCode;

	@Column(name="LAST_UPDATED_DATE", unique=true, nullable=false)
	private Timestamp lastUpdatedDate;

	@Column(name="EXTRACT_DATE", unique=true, nullable=false)
	private Timestamp extractDate;

	public RotationId() {
	}
	public int getRotationId() {
		return this.rotationId;
	}
	public void setRotationId(int rotationId) {
		this.rotationId = rotationId;
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
		RotationId other = (RotationId) obj;
		if (rotationId != other.rotationId)
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
				+ rotationId;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((lastUpdatedDate == null) ? 0 : lastUpdatedDate.hashCode());
		result = prime * result + ((extractDate == null) ? 0 : extractDate.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getRotationId()
				, getPlantLocCode(), getDeptCode(), getLastUpdatedDate(), getExtractDate());
	}
}