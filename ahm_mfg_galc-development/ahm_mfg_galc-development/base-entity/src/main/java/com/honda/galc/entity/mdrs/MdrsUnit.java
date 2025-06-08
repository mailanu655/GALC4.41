package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the PCLUN1 database table.
 * 
 */
@Entity
@Table(name="PCLUN1", schema="VIOS")
public class MdrsUnit extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UNIT_REC_ID", unique=true, nullable=false)
	private int id;

	@Column(name="ACCEPTED_DATE")
	private Timestamp acceptedDate;

	@Column(name="ACCEPTED_LOGON_ID", length=8)
	private String acceptedLogonId;

	@Column(name="ADDED_DATE", nullable=false)
	private Timestamp addedDate;

	@Column(name="APVD_PROC_MAINT_ID", nullable=false)
	private int apvdProcMaintId;

	@Column(name="APVD_UNIT_MAINT_ID", nullable=false)
	private int apvdUnitMaintId;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Timestamp extractDate;

	@Column(name="JOINT_TYPE", length=1)
	private String jointType;

	@Column(name="MAINT_DATE", nullable=false)
	private Timestamp maintDate;

	@Column(name="MAX_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal maxTorqueValQty;

	@Column(name="MIN_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal minTorqueValQty;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROD_UNIT_ID_NO", length=20)
	private String prodUnitIdNo;

	@Column(name="QLTY_PT_DESC_TEXT", nullable=false, length=500)
	private String qltyPtDescText;

	@Column(nullable=false, length=8)
	private String status;

	@Column(name="TEAM_ID", nullable=false)
	private int teamId;

	@Column(name="TORQUE_CHAR_VALUE", nullable=false, length=3)
	private String torqueCharValue;

	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;

	@Column(name="UNIT_OP_DESC_TEXT", nullable=false, length=254)
	private String unitOpDescText;

	@Column(name="UNIT_SEQ_NO", nullable=false)
	private short unitSeqNo;

	@Column(name="WORK_PT_DESC_TEXT", nullable=false, length=500)
	private String workPtDescText;

	public MdrsUnit() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getAcceptedDate() {
		return this.acceptedDate;
	}

	public void setAcceptedDate(Timestamp acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getAcceptedLogonId() {
		return StringUtils.trim(this.acceptedLogonId);
	}

	public void setAcceptedLogonId(String acceptedLogonId) {
		this.acceptedLogonId = acceptedLogonId;
	}

	public Timestamp getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public int getApvdProcMaintId() {
		return this.apvdProcMaintId;
	}

	public void setApvdProcMaintId(int apvdProcMaintId) {
		this.apvdProcMaintId = apvdProcMaintId;
	}

	public int getApvdUnitMaintId() {
		return this.apvdUnitMaintId;
	}

	public void setApvdUnitMaintId(int apvdUnitMaintId) {
		this.apvdUnitMaintId = apvdUnitMaintId;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Timestamp getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Timestamp extractDate) {
		this.extractDate = extractDate;
	}

	public String getJointType() {
		return StringUtils.trim(this.jointType);
	}

	public void setJointType(String jointType) {
		this.jointType = jointType;
	}

	public Timestamp getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(Timestamp maintDate) {
		this.maintDate = maintDate;
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

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getProdUnitIdNo() {
		return StringUtils.trim(this.prodUnitIdNo);
	}

	public void setProdUnitIdNo(String prodUnitIdNo) {
		this.prodUnitIdNo = prodUnitIdNo;
	}

	public String getQltyPtDescText() {
		return StringUtils.trim(this.qltyPtDescText);
	}

	public void setQltyPtDescText(String qltyPtDescText) {
		this.qltyPtDescText = qltyPtDescText;
	}

	public String getStatus() {
		return StringUtils.trim(this.status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTorqueCharValue() {
		return StringUtils.trim(this.torqueCharValue);
	}

	public void setTorqueCharValue(String torqueCharValue) {
		this.torqueCharValue = torqueCharValue;
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

	public short getUnitSeqNo() {
		return this.unitSeqNo;
	}

	public void setUnitSeqNo(short unitSeqNo) {
		this.unitSeqNo = unitSeqNo;
	}

	public String getWorkPtDescText() {
		return StringUtils.trim(this.workPtDescText);
	}

	public void setWorkPtDescText(String workPtDescText) {
		this.workPtDescText = workPtDescText;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((acceptedDate == null) ? 0 : acceptedDate.hashCode());
		result = prime * result
				+ ((acceptedLogonId == null) ? 0 : acceptedLogonId.hashCode());
		result = prime * result
				+ ((addedDate == null) ? 0 : addedDate.hashCode());
		result = prime * result + apvdProcMaintId;
		result = prime * result + apvdUnitMaintId;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((jointType == null) ? 0 : jointType.hashCode());
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maxTorqueValQty == null) ? 0 : maxTorqueValQty.hashCode());
		result = prime * result
				+ ((minTorqueValQty == null) ? 0 : minTorqueValQty.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((prodUnitIdNo == null) ? 0 : prodUnitIdNo.hashCode());
		result = prime * result
				+ ((qltyPtDescText == null) ? 0 : qltyPtDescText.hashCode());
		result = prime * result
				+ ((status == null) ? 0 : status.hashCode());
		result = prime * result + teamId;
		result = prime * result
				+ ((torqueCharValue == null) ? 0 : torqueCharValue.hashCode());
		result = prime * result
				+ ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result
				+ ((unitOpDescText == null) ? 0 : unitOpDescText.hashCode());
		result = prime * result + unitSeqNo;
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
		MdrsUnit other = (MdrsUnit) obj;
		if (id != other.id)
			return false;
		if (acceptedDate == null) {
			if (other.acceptedDate != null)
				return false;
		} else if (!acceptedDate.equals(other.acceptedDate))
			return false;
		if (id != other.id)
			return false;
		if (acceptedLogonId == null) {
			if (other.acceptedLogonId != null)
				return false;
		} else if (!acceptedLogonId.equals(other.acceptedLogonId))
			return false;
		if (id != other.id)
			return false;
		if (addedDate == null) {
			if (other.addedDate != null)
				return false;
		} else if (!addedDate.equals(other.addedDate))
			return false;
		if (apvdProcMaintId != other.apvdProcMaintId)
			return false;
		if (apvdUnitMaintId != other.apvdUnitMaintId)
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
		if (jointType == null) {
			if (other.jointType != null)
				return false;
		} else if (!jointType.equals(other.jointType))
			return false;
		if (maintDate == null) {
			if (other.maintDate != null)
				return false;
		} else if (!maintDate.equals(other.maintDate))
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
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (prodUnitIdNo == null) {
			if (other.prodUnitIdNo != null)
				return false;
		} else if (!prodUnitIdNo.equals(other.prodUnitIdNo))
			return false;
		if (qltyPtDescText == null) {
			if (other.qltyPtDescText != null)
				return false;
		} else if (!qltyPtDescText.equals(other.qltyPtDescText))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (teamId != other.teamId)
			return false;
		if (torqueCharValue == null) {
			if (other.torqueCharValue != null)
				return false;
		} else if (!torqueCharValue.equals(other.torqueCharValue))
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
		if (unitSeqNo != other.unitSeqNo)
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
		return toString(getId(), getAcceptedDate(), getAcceptedLogonId(), getAddedDate(), getApvdProcMaintId()
				, getApvdUnitMaintId(), getDeptCode(), getExtractDate(), getJointType(), getMaintDate()
				, getMaxTorqueValQty(), getMinTorqueValQty(), getPlantLocCode(), getProdUnitIdNo()
				, getQltyPtDescText(), getStatus(), getTeamId(), getTorqueCharValue(), getUnitNo()
				, getUnitOpDescText(), getUnitSeqNo(), getWorkPtDescText());
	}

}