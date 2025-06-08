package com.honda.galc.entity.pdda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVUPM1", schema="VIOS")
public class UnitPart extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UnitPartId id;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="MAINTENANCE_ID", nullable=false, insertable=false, updatable=false)
	private Unit unit;

    public UnitPart() {}

	public UnitPartId getId() {
		return this.id;
	}

	public void setId(UnitPartId id) {
		this.id = id;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
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
		UnitPart other = (UnitPart) obj;
		
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return toString(getId().getMaintenanceId(), getId().getMtcModel(), getId().getMtcType(), 
				getId().getMtcOption(), getId().getPartItemNo(), getId().getPartNo(), 
				getId().getPartEffDate(), getId().getPartMarkingNo(), getId().getPartQty(),
				getId().getTgtEffBegDate(), getId().getDeliveryLocation(),  getId().getPartName(), 
				getId().getPartSectionCode(), getDeptCode(),  getPlantLocCode());
	}
}