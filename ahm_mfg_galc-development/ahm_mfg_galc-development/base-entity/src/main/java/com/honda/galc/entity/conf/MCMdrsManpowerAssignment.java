package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the MC_MDRS_MANPOWER_ASSIGNMENT_TBX database table.
 * 
 */
@Entity
@Table(name="MC_MDRS_MANPOWER_ASSIGNMENT_TBX")
public class MCMdrsManpowerAssignment extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCMdrsManpowerAssignmentId id;

	@Column(name="ASSOCIATE_ID", length=7)
	private String associateId;

	@Column(name="ASSOCIATE_NO", length=11)
	private String associateNo;

	@Column(name="LINE_NO", length=2)
	private String lineNo;

	@Column(name="PDDA_PLATFORM_ID")
	private int pddaPlatformId;

	@Column(name="PERIOD")
	private int period;

	@Column(name="PLANT_CODE", length=4)
	private String plantCode;

	@Column(name="PROCESS_LOCATION", length=2)
	private String processLocation;

	@Column(name="PROCESS_POINT_ID", length=16)
	private String processPointId;

	@Column(name="PRODUCTION_DATE")
	private Timestamp productionDate;

	@Column(name="SHIFT", length=2)
	private String shift;

	public MCMdrsManpowerAssignment() {
	}

	public MCMdrsManpowerAssignmentId getId() {
		return this.id;
	}

	public void setId(MCMdrsManpowerAssignmentId id) {
		this.id = id;
	}

	public String getAssociateId() {
		return StringUtils.trim(this.associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getLineNo() {
		return StringUtils.trim(this.lineNo);
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public int getPddaPlatformId() {
		return this.pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getPlantCode() {
		return StringUtils.trim(this.plantCode);
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateId == null) ? 0 : associateId.hashCode());
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result
				+ ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + pddaPlatformId;
		result = prime * result + period;
		result = prime * result
				+ ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result
				+ ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result
				+ ((shift == null) ? 0 : shift.hashCode());
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
		MCMdrsManpowerAssignment other = (MCMdrsManpowerAssignment) obj;
		if (associateId == null) {
			if (other.associateId != null)
				return false;
		} else if (!associateId.equals(other.associateId))
			return false;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (period != other.period)
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
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
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getManpowerAssignmentId(), getId().getPlantLocCode(), getId().getDeptCode()
				, getId().getExtractDate(), getId().getLastUpdatedDate(), getAssociateId(), getAssociateNo()
				, getLineNo(), getPddaPlatformId(), getPeriod(), getPlantCode(), getProcessLocation()
				, getProcessPointId(), getProductionDate(), getShift());
	}

}