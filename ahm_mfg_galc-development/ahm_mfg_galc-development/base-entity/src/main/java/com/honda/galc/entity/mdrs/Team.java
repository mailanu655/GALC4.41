package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the PCLTM1 database table.
 * 
 */
@Entity
@Table(name="PCLTM1", schema="VIOS")
public class Team extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TEAM_ID", unique=true, nullable=false)
	private int id;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="EFF_BEG_DATE", nullable=false)
	private Date effBegDate;

	@Column(name="EFF_END_DATE", nullable=false)
	private Date effEndDate;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Timestamp extractDate;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_RATE_ID", nullable=false)
	private int prodRateId;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="TEAM_NO", nullable=false, length=4)
	private String teamNo;

	@Column(name="TEAM_SEQ_NO", nullable=false)
	private short teamSeqNo;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

	public Team() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Date getEffBegDate() {
		return this.effBegDate;
	}

	public void setEffBegDate(Date effBegDate) {
		this.effBegDate = effBegDate;
	}

	public Date getEffEndDate() {
		return this.effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public Timestamp getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Timestamp extractDate) {
		this.extractDate = extractDate;
	}

	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
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

	public int getProdRateId() {
		return this.prodRateId;
	}

	public void setProdRateId(int prodRateId) {
		this.prodRateId = prodRateId;
	}

	public BigDecimal getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getTeamNo() {
		return StringUtils.trim(this.teamNo);
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public short getTeamSeqNo() {
		return this.teamSeqNo;
	}

	public void setTeamSeqNo(short teamSeqNo) {
		this.teamSeqNo = teamSeqNo;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((effBegDate == null) ? 0 : effBegDate.hashCode());
		result = prime * result
				+ ((effEndDate== null) ? 0 : effEndDate.hashCode());
		result = prime * result
				+ ((extractDate== null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((modelYearDate== null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((plantLocCode== null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((prodAsmLineNo== null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result + prodRateId;
		result = prime * result
				+ ((prodSchQty== null) ? 0 : prodSchQty.hashCode());
		result = prime * result
				+ ((teamNo== null) ? 0 : teamNo.hashCode());
		result = prime * result + ((int)teamSeqNo);
		result = prime * result
				+ ((vehicleModelCode== null) ? 0 : vehicleModelCode.hashCode());
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
		Team other = (Team) obj;
		if (id != other.id)
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (effBegDate == null) {
			if (other.effBegDate != null)
				return false;
		} else if (!effBegDate.equals(other.effBegDate))
			return false;
		if (effEndDate == null) {
			if (other.effEndDate != null)
				return false;
		} else if (!effEndDate.equals(other.effEndDate))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (modelYearDate == null) {
			if (other.modelYearDate != null)
				return false;
		} else if (!modelYearDate.equals(other.modelYearDate))
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
		if (prodRateId != other.prodRateId)
			return false;
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (teamNo == null) {
			if (other.teamNo != null)
				return false;
		} else if (!teamNo.equals(other.teamNo))
			return false;
		if (teamSeqNo != other.teamSeqNo)
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
		return toString(getId(), getDeptCode(), getEffBegDate(), getEffEndDate(), getExtractDate(), getModelYearDate()
				, getPlantLocCode(), getProdAsmLineNo(), getProdRateId(), getProdSchQty(), getTeamNo(), getTeamSeqNo()
				, getVehicleModelCode());
	}
}