package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVUWX1", schema="VIOS")
public class UnitConcernPoint extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="CONCERN_POINT", nullable=false, length=150)
	private String concernPoint;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="MAINTENANCE_ID", nullable=false, insertable=false, updatable=false)
	private Unit unit;

    public UnitConcernPoint() {}
    
    public Integer getId() {
    	return getMaintenanceId();
    }
    
	public void setId(int id) {
		setMaintenanceId(id);
	}

	public int getMaintenanceId() {
		return this.maintenanceId;
	}

	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public String getConcernPoint() {
		return StringUtils.trim(this.concernPoint);
	}

	public void setConcernPoint(String concernPoint) {
		this.concernPoint = concernPoint;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((concernPoint == null) ? 0 : concernPoint.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		UnitConcernPoint other = (UnitConcernPoint) obj;
		if (concernPoint == null) {
			if (other.concernPoint != null)
				return false;
		} else if (!concernPoint.equals(other.concernPoint))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId(), getMaintenanceId(), getConcernPoint(), getDeptCode(), getPlantLocCode());
	}
}