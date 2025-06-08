package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MC_PROCESS_ASSIGNMENT_TBX database table.
 * 
 */
@Embeddable
public class MCProcessAssignmentId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;

	@Column(name="PDDA_PLATFORM_ID")
	private int pddaPlatformId;

	@Column(name="LINE_NO", nullable=false, length=2)
	private String lineNo;

	@Column(name="PROCESS_LOCATION", nullable=false, length=2)
	private String processLocation;

	@Column(name="PLANT_CODE", nullable=false, length=4)
	private String plantCode;

	@Column(name="PRODUCTION_DATE", nullable=false)
	private Timestamp productionDate;

	@Column(name="SHIFT", nullable=false, length=2)
	private String shift;

	@Column(name="PERIOD")
	private int period;
	
	@Column(name="ASSOCIATE_NO")
	private String associateNo;
	

	public String getAssociateNo() {
		return associateNo;
	}
	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	public MCProcessAssignmentId() {
	}
	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public int getPddaPlatformId() {
		return this.pddaPlatformId;
	}
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
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
	public Timestamp getProductionDate() {
		return this.productionDate;
	}
	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCProcessAssignmentId other = (MCProcessAssignmentId) obj;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
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
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (period != other.period)
			return false;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result
				+ ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result
				+ ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result
				+ ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result
				+ ((shift == null) ? 0 : shift.hashCode());
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + period;
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getProcessPointId()
				, getPddaPlatformId(), getLineNo(), getProcessLocation(), getPlantCode()
				, getProductionDate(), getShift(), getAssociateNo());
	}
}