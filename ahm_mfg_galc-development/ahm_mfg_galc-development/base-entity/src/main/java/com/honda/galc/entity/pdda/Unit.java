package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVUMX1", schema="VIOS")
public class Unit extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="ACTION", nullable=false, length=1)
	private String action;

	@Column(name="AUX_MSDS_NO", length=20)
	private String auxMsdsNo;

	@Column(name="AUX_MTRL_DESC_TEXT", length=50)
	private String auxMtrlDescText;

	@Column(name="BASE_PART_NO", nullable=false, length=5)
	private String basePartNo;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="IMPACT_PT_DESC", length=540)
	private String impactPtDesc;

	@Column(name="JOINT_TYPE", nullable=false, length=1)
	private String jointType;

	@Column(name="LINE_SPEED", nullable=false, precision=5, scale=1)
	private BigDecimal lineSpeed;

	@Column(name="MAINT_DATE", nullable=false)
	private Date maintDate;

	@Column(name="MAINT_LOGON_ID", nullable=false, length=8)
	private String maintLogonId;

	@Column(name="MAX_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal maxTorqueValQty;

	@Column(name="MIN_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal minTorqueValQty;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="QLTY_PT_DESC_TEXT", nullable=false, length=500)
	private String qltyPtDescText;

	@Column(name="SAFETY_PT_DESC", length=500)
	private String safetyPtDesc;

	@Column(name="TOOL", nullable=false, length=50)
	private String tool;

	@Column(name="TORQUE_CHAR_VALUE", nullable=false, length=3)
	private String torqueCharValue;

	@Column(name="UNIT_CREATE_DATE", nullable=false)
	private Date unitCreateDate;

	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;

	@Column(name="UNIT_OP_DESC_TEXT", nullable=false, length=254)
	private String unitOpDescText;

	@Column(name="UNIT_RANK", length=20)
	private String unitRank;

	@Column(name="UNIT_SEQ_NO", nullable=false)
	private int unitSeqNo;
	
	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

	@Column(name="WORK_PT_DESC_TEXT", nullable=false, length=500)
	private String workPtDescText;
	
	@Column(name="SAFETY_ERGO_PT", nullable=false, length=500)
	private String safetyErgoPt;
	
	@Column(name="SAFETY_ERGO_INST", nullable=false, length=500)
	private String safetyErgoInst;
	
	@Column(name="UNIT_TOT_TIME", nullable=false)
	private double unitTotTime;

	@OneToMany(targetEntity = UnitConcernPoint.class, fetch=FetchType.EAGER)
	@ElementJoinColumn(name="MAINTENANCE_ID")
    @OrderBy
	private List<UnitConcernPoint> unitConcernPoints;
	
	@OneToMany(targetEntity = UnitControlMethod.class, fetch=FetchType.EAGER)
	@ElementJoinColumn(name="MAINTENANCE_ID")
    @OrderBy
	private List<UnitControlMethod> unitControlMethods;
	
	public Unit() {}
    
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

	public String getAction() {
		return StringUtils.trim(this.action);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAuxMsdsNo() {
		return StringUtils.trim(this.auxMsdsNo);
	}

	public void setAuxMsdsNo(String auxMsdsNo) {
		this.auxMsdsNo = auxMsdsNo;
	}

	public String getAuxMtrlDescText() {
		return StringUtils.trim(this.auxMtrlDescText);
	}

	public void setAuxMtrlDescText(String auxMtrlDescText) {
		this.auxMtrlDescText = auxMtrlDescText;
	}

	public String getBasePartNo() {
		return StringUtils.trim(this.basePartNo);
	}

	public void setBasePartNo(String basePartNo) {
		this.basePartNo = basePartNo;
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

	public String getImpactPtDesc() {
		return StringUtils.trim(this.impactPtDesc);
	}

	public void setImpactPtDesc(String impactPtDesc) {
		this.impactPtDesc = impactPtDesc;
	}

	public String getJointType() {
		return StringUtils.trim(this.jointType);
	}

	public void setJointType(String jointType) {
		this.jointType = jointType;
	}

	public BigDecimal getLineSpeed() {
		return this.lineSpeed;
	}

	public void setLineSpeed(BigDecimal lineSpeed) {
		this.lineSpeed = lineSpeed;
	}

	public Date getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(Date maintDate) {
		this.maintDate = maintDate;
	}

	public String getMaintLogonId() {
		return StringUtils.trim(this.maintLogonId);
	}

	public void setMaintLogonId(String maintLogonId) {
		this.maintLogonId = maintLogonId;
	}

	public BigDecimal getMaxTorqueValQty() {
		return this.maxTorqueValQty;
	}

	public void setMaxTorqueValQty(BigDecimal maxTorqueValQty) {
		this.maxTorqueValQty = maxTorqueValQty;
	}

	public BigDecimal getMinTorqueValQty() {
		return this.minTorqueValQty;
	}

	public void setMinTorqueValQty(BigDecimal minTorqueValQty) {
		this.minTorqueValQty = minTorqueValQty;
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

	public BigDecimal getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getQltyPtDescText() {
		return StringUtils.trim(this.qltyPtDescText);
	}

	public void setQltyPtDescText(String qltyPtDescText) {
		this.qltyPtDescText = qltyPtDescText;
	}

	public String getSafetyPtDesc() {
		return StringUtils.trim(this.safetyPtDesc);
	}

	public void setSafetyPtDesc(String safetyPtDesc) {
		this.safetyPtDesc = safetyPtDesc;
	}

	public String getTool() {
		return StringUtils.trim(this.tool);
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public String getTorqueCharValue() {
		return StringUtils.trim(this.torqueCharValue);
	}

	public void setTorqueCharValue(String torqueCharValue) {
		this.torqueCharValue = torqueCharValue;
	}

	public Date getUnitCreateDate() {
		return this.unitCreateDate;
	}

	public void setUnitCreateDate(Date unitCreateDate) {
		this.unitCreateDate = unitCreateDate;
	}

	public String getUnitNo() {
		return StringUtils.trim(this.unitNo);
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitOpDescText() {
		return StringUtils.trim(this.unitOpDescText);
	}

	public void setUnitOpDescText(String unitOpDescText) {
		this.unitOpDescText = unitOpDescText;
	}

	public String getUnitRank() {
		return StringUtils.trim(this.unitRank);
	}

	public void setUnitRank(String unitRank) {
		this.unitRank = unitRank;
	}

	public int getUnitSeqNo() {
		return this.unitSeqNo;
	}

	public void setUnitSeqNo(int unitSeqNo) {
		this.unitSeqNo = unitSeqNo;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getWorkPtDescText() {
		return StringUtils.trim(this.workPtDescText);
	}

	public void setWorkPtDescText(String workPtDescText) {
		this.workPtDescText = workPtDescText;
	}

	public void setSafetyErgoPt(String safetyErgoPt) {
		this.safetyErgoPt = safetyErgoPt;
	}

	public String getSafetyErgoPt() {
		return safetyErgoPt;
	}

	public void setSafetyErgoInst(String safetyErgoInst) {
		this.safetyErgoInst = safetyErgoInst;
	}

	public String getSafetyErgoInst() {
		return safetyErgoInst;
	}

	public double getUnitTotTime() {
		return unitTotTime;
	}

	public void setUnitTotTime(double unitTotTime) {
		this.unitTotTime = unitTotTime;
	}
	
	public List<UnitConcernPoint> getUnitConcernPoints() {
		return this.unitConcernPoints;
	}

	public void setUnitConcernPoints(List<UnitConcernPoint> unitConcernPoints) {
		this.unitConcernPoints = unitConcernPoints;
	}
	
	public List<UnitControlMethod> getUnitControlMethods() {
		return unitControlMethods;
	}

	public void setUnitControlMethods(List<UnitControlMethod> unitControlMethods) {
		this.unitControlMethods = unitControlMethods;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((auxMsdsNo == null) ? 0 : auxMsdsNo.hashCode());
		result = prime * result
				+ ((auxMtrlDescText == null) ? 0 : auxMtrlDescText.hashCode());
		result = prime * result
				+ ((basePartNo == null) ? 0 : basePartNo.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((impactPtDesc == null) ? 0 : impactPtDesc.hashCode());
		result = prime * result
				+ ((jointType == null) ? 0 : jointType.hashCode());
		result = prime * result
				+ ((lineSpeed == null) ? 0 : lineSpeed.hashCode());
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maintLogonId == null) ? 0 : maintLogonId.hashCode());
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((maxTorqueValQty == null) ? 0 : maxTorqueValQty.hashCode());
		result = prime * result
				+ ((minTorqueValQty == null) ? 0 : minTorqueValQty.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result
				+ ((qltyPtDescText == null) ? 0 : qltyPtDescText.hashCode());
		result = prime * result
				+ ((safetyErgoInst == null) ? 0 : safetyErgoInst.hashCode());
		result = prime * result
				+ ((safetyErgoPt == null) ? 0 : safetyErgoPt.hashCode());
		result = prime * result
				+ ((safetyPtDesc == null) ? 0 : safetyPtDesc.hashCode());
		result = prime * result + ((tool == null) ? 0 : tool.hashCode());
		result = prime * result
				+ ((torqueCharValue == null) ? 0 : torqueCharValue.hashCode());
		result = prime * result
				+ ((unitCreateDate == null) ? 0 : unitCreateDate.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result
				+ ((unitOpDescText == null) ? 0 : unitOpDescText.hashCode());
		result = prime * result
				+ ((unitRank == null) ? 0 : unitRank.hashCode());
		result = prime * result + unitSeqNo;
		long temp;
		temp = Double.doubleToLongBits(unitTotTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		result = prime * result
				+ ((workPtDescText == null) ? 0 : workPtDescText.hashCode());
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
		Unit other = (Unit) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (auxMsdsNo == null) {
			if (other.auxMsdsNo != null)
				return false;
		} else if (!auxMsdsNo.equals(other.auxMsdsNo))
			return false;
		if (auxMtrlDescText == null) {
			if (other.auxMtrlDescText != null)
				return false;
		} else if (!auxMtrlDescText.equals(other.auxMtrlDescText))
			return false;
		if (basePartNo == null) {
			if (other.basePartNo != null)
				return false;
		} else if (!basePartNo.equals(other.basePartNo))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (impactPtDesc == null) {
			if (other.impactPtDesc != null)
				return false;
		} else if (!impactPtDesc.equals(other.impactPtDesc))
			return false;
		if (jointType == null) {
			if (other.jointType != null)
				return false;
		} else if (!jointType.equals(other.jointType))
			return false;
		if (lineSpeed == null) {
			if (other.lineSpeed != null)
				return false;
		} else if (!lineSpeed.equals(other.lineSpeed))
			return false;
		if (maintDate == null) {
			if (other.maintDate != null)
				return false;
		} else if (!maintDate.equals(other.maintDate))
			return false;
		if (maintLogonId == null) {
			if (other.maintLogonId != null)
				return false;
		} else if (!maintLogonId.equals(other.maintLogonId))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (maxTorqueValQty == null) {
			if (other.maxTorqueValQty != null)
				return false;
		} else if (!maxTorqueValQty.equals(other.maxTorqueValQty))
			return false;
		if (minTorqueValQty == null) {
			if (other.minTorqueValQty != null)
				return false;
		} else if (!minTorqueValQty.equals(other.minTorqueValQty))
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
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (qltyPtDescText == null) {
			if (other.qltyPtDescText != null)
				return false;
		} else if (!qltyPtDescText.equals(other.qltyPtDescText))
			return false;
		if (safetyErgoInst == null) {
			if (other.safetyErgoInst != null)
				return false;
		} else if (!safetyErgoInst.equals(other.safetyErgoInst))
			return false;
		if (safetyErgoPt == null) {
			if (other.safetyErgoPt != null)
				return false;
		} else if (!safetyErgoPt.equals(other.safetyErgoPt))
			return false;
		if (safetyPtDesc == null) {
			if (other.safetyPtDesc != null)
				return false;
		} else if (!safetyPtDesc.equals(other.safetyPtDesc))
			return false;
		if (tool == null) {
			if (other.tool != null)
				return false;
		} else if (!tool.equals(other.tool))
			return false;
		if (torqueCharValue == null) {
			if (other.torqueCharValue != null)
				return false;
		} else if (!torqueCharValue.equals(other.torqueCharValue))
			return false;
		if (unitCreateDate == null) {
			if (other.unitCreateDate != null)
				return false;
		} else if (!unitCreateDate.equals(other.unitCreateDate))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (unitOpDescText == null) {
			if (other.unitOpDescText != null)
				return false;
		} else if (!unitOpDescText.equals(other.unitOpDescText))
			return false;
		if (unitRank == null) {
			if (other.unitRank != null)
				return false;
		} else if (!unitRank.equals(other.unitRank))
			return false;
		if (unitSeqNo != other.unitSeqNo)
			return false;
		if (Double.doubleToLongBits(unitTotTime) != Double
				.doubleToLongBits(other.unitTotTime))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		if (workPtDescText == null) {
			if (other.workPtDescText != null)
				return false;
		} else if (!workPtDescText.equals(other.workPtDescText))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId(), getMaintenanceId(), getAction(), getAuxMsdsNo(), getAuxMtrlDescText(), 
				getBasePartNo(), getDeptCode(), getExtractDate(), getImpactPtDesc(), getJointType(), 
				getLineSpeed(), getMaintDate(), getMaintLogonId(), getMaxTorqueValQty(), 
				getMinTorqueValQty(), getModelYearDate(), getPlantLocCode(), getProdAsmLineNo(), 
				getProdSchQty(), getQltyPtDescText(), getSafetyPtDesc(), getTool(), 
				getTorqueCharValue(), getUnitCreateDate(), getUnitNo(), getUnitOpDescText(), getUnitRank(), 
				getUnitSeqNo(), getVehicleModelCode(), getWorkPtDescText(), getSafetyErgoPt(), 
				getSafetyErgoInst(), getUnitTotTime());
	}
}