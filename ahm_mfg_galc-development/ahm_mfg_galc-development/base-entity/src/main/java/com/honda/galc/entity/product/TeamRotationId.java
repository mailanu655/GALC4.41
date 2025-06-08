package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * The primary key class for the TEAM_ROTATION_TBX database table.
 * 
 */
@Embeddable
public class TeamRotationId implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Column(name="LINE_NO")
	private String lineNo;

	@Column(name="PROCESS_LOCATION")
	private String processLocation;

	@Column(name="PLANT_CODE")
	private String plantCode;

	@Temporal(TemporalType.DATE)
	@Column(name="PRODUCTION_DATE")
	private java.util.Date productionDate;

	private String shift;

	private String team;

	public TeamRotationId() {
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
	public java.util.Date getProductionDate() {
		return this.productionDate;
	}
	public void setProductionDate(java.util.Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getShift() {
		return StringUtils.trim(this.shift);
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getTeam() {
		return StringUtils.trim(this.team);
	}
	public void setTeam(String team) {
		this.team = team;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TeamRotationId)) {
			return false;
		}
		TeamRotationId castOther = (TeamRotationId)other;
		return 
			this.lineNo.equals(castOther.lineNo)
			&& this.processLocation.equals(castOther.processLocation)
			&& this.plantCode.equals(castOther.plantCode)
			&& this.productionDate.equals(castOther.productionDate)
			&& this.shift.equals(castOther.shift)
			&& this.team.equals(castOther.team);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.lineNo.hashCode();
		hash = hash * prime + this.processLocation.hashCode();
		hash = hash * prime + this.plantCode.hashCode();
		hash = hash * prime + this.productionDate.hashCode();
		hash = hash * prime + this.shift.hashCode();
		hash = hash * prime + this.team.hashCode();
		
		return hash;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}