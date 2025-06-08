package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="PVUMT1", schema="VIOS")
public class UnitModelType extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UnitModelTypeId id;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="MAINTENANCE_ID", nullable=false, insertable=false, updatable=false)
	private Unit unit;

    public UnitModelType() {}

	public UnitModelTypeId getId() {
		return this.id;
	}

	public void setId(UnitModelTypeId id) {
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

	public String getProdAsmLineNo() {
		return StringUtils.trim(this.prodAsmLineNo);
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public BigDecimal getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
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
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
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
		UnitModelType other = (UnitModelType) obj;
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
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null)
				return false;
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo))
			return false;
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getMaintenanceId(), getId().getModelYearDate(), getId().getMtcModel(), getId().getMtcType(), getId().getMtcOption(), 
				getDeptCode(), getPlantLocCode(), getProdAsmLineNo(), getProdSchQty(), getVehicleModelCode());
	}
}