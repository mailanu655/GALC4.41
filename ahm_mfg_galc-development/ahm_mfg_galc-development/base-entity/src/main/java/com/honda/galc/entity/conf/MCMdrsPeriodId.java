package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MC_MDRS_PERIOD_TBX database table.
 * 
 */
@Embeddable
public class MCMdrsPeriodId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="LINE_NO", nullable=false, length=2)
	private String lineNo;

	@Column(name="PROCESS_LOCATION", nullable=false, length=2)
	private String processLocation;

	@Column(name="PLANT_CODE", nullable=false, length=4)
	private String plantCode;

	@Column(name="SHIFT", nullable=false, length=2)
	private String shift;

	@Column(name="PERIOD")
	private int period;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="SHIFT_ID")
	private short shiftId;

	@Column(name="QUARTER")
	private short quarter;

	public MCMdrsPeriodId() {
	}
	public String getLineNo() {
		return StringUtils.trim(this.lineNo);
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}
	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}
	public String getPlantCode() {
		return StringUtils.trim(this.plantCode);
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getShift() {
		return StringUtils.trim(this.shift);
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public int getPeriod() {
		return this.period;
	}
	public void setPeriod(int period) {
		this.period = period;
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
	public short getShiftId() {
		return this.shiftId;
	}
	public void setShiftId(short shiftId) {
		this.shiftId = shiftId;
	}
	public short getQuarter() {
		return this.quarter;
	}
	public void setQuarter(short quarter) {
		this.quarter = quarter;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result
				+ ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result
				+ ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + period;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((int) this.shiftId);
		result = prime * result + ((int) this.quarter);
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
		MCMdrsPeriodId other = (MCMdrsPeriodId) obj;
		if (period != other.period)
			return false;
		if (shiftId != other.shiftId)
			return false;
		if (quarter != other.quarter)
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
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
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getPeriod(), getShiftId()
				, getQuarter(), getLineNo(), getProcessLocation(), getPlantCode(), getShift()
				, getPlantLocCode(), getDeptCode());
	}
}