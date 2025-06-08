package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Embeddable
public class RotationProcessId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ROTATEREC_NO", unique=true, nullable=false)
	private int rotateRecNo;

	@Column(name="PROCESS_ID", unique=true, nullable=false)
	private int processId;

	@Column(name="PLANT_LOC_CODE", unique=true, nullable=false, length=1)
	private String plantLocCode;

	@Column(name="DEPT_CODE", unique=true, nullable=false, length=2)
	private String deptCode;

	@Column(name="EXTRACT_DATE", unique=true, nullable=false)
	private Date extractDate;

    public RotationProcessId() {
    }
	public int getRotateRecNo() {
		return this.rotateRecNo;
	}
	public void setRotateRecNo(int rotaterecNo) {
		this.rotateRecNo = rotaterecNo;
	}
	public int getProcessId() {
		return this.processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
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
	public Date getExtractDate() {
		return this.extractDate;
	}
	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + processId;
		result = prime * result + rotateRecNo;
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
		if (!(obj instanceof RotationProcessId)) {
			return false;
		}
		RotationProcessId other = (RotationProcessId) obj;
		if (deptCode == null) {
			if (other.deptCode != null) {
				return false;
			}
		} else if (!deptCode.equals(other.deptCode)) {
			return false;
		}
		if (extractDate == null) {
			if (other.extractDate != null) {
				return false;
			}
		} else if (!extractDate.equals(other.extractDate)) {
			return false;
		}
		if (plantLocCode == null) {
			if (other.plantLocCode != null) {
				return false;
			}
		} else if (!plantLocCode.equals(other.plantLocCode)) {
			return false;
		}
		if (processId != other.processId) {
			return false;
		}
		if (rotateRecNo != other.rotateRecNo) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getRotateRecNo(), getProcessId(), getPlantLocCode(), getDeptCode(), getExtractDate());
	}
}