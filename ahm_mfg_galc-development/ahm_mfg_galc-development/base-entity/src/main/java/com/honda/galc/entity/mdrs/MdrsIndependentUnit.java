package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVLUN1", schema="VIOS")
public class MdrsIndependentUnit extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UNIT_REC_ID", nullable=false)
	private int id;

	@Column(name="ACCEPTED_DATE", nullable=false)
	private Date acceptedDate;

	@Column(name="ACCEPTED_LOGON_ID", nullable=false, length=8)
	private String acceptedLogonId;

	@Column(name="ADDED_DATE", nullable=false)
	private Date addedDate;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="JOINT_TYPE", nullable=false, length=1)
	private String jointType;

	@Column(name="MAINT_DATE", nullable=false)
	private Date maintDate;

	@Column(name="MAX_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal maxTorqueValQty;

	@Column(name="MIN_TORQUE_VAL_QTY", nullable=false, precision=4, scale=1)
	private BigDecimal minTorqueValQty;

	@Column(name="PROCESS_ID", nullable=false)
	private int processId;

	@Column(name="QLTY_PT_DESC_TEXT", nullable=false, length=500)
	private String qltyPtDescText;

	@Column(name="STATUS", nullable=false, length=8)
	private String status;

	@Column(name="TEAM_ID", nullable=false)
	private int teamId;

	@Column(name="TORQUE_CHAR_VALUE", nullable=false, length=3)
	private String torqueCharValue;

	@Column(name="UNIT_ID", nullable=false)
	private int unitId;

	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;

	@Column(name="UNIT_OP_DESC_TEXT", nullable=false, length=254)
	private String unitOpDescText;

	@Column(name="UNIT_SEQ_NO", nullable=false)
	private short unitSeqNo;

	@Column(name="WORK_PT_DESC_TEXT", nullable=false, length=500)
	private String workPtDescText;

    public MdrsIndependentUnit() {}

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getAcceptedDate() {
		return this.acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getAcceptedLogonId() {
		return StringUtils.trim(this.acceptedLogonId);
	}

	public void setAcceptedLogonId(String acceptedLogonId) {
		this.acceptedLogonId = acceptedLogonId;
	}

	public Date getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}

	public String getJointType() {
		return StringUtils.trim(this.jointType);
	}

	public void setJointType(String jointType) {
		this.jointType = jointType;
	}

	public Date getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(Date maintDate) {
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

	public int getProcessId() {
		return this.processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
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

	public int getUnitId() {
		return this.unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
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
		result = prime * result
				+ ((acceptedDate == null) ? 0 : acceptedDate.hashCode());
		result = prime * result
				+ ((acceptedLogonId == null) ? 0 : acceptedLogonId.hashCode());
		result = prime * result
				+ ((addedDate == null) ? 0 : addedDate.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((jointType == null) ? 0 : jointType.hashCode());
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maxTorqueValQty == null) ? 0 : maxTorqueValQty.hashCode());
		result = prime * result
				+ ((minTorqueValQty == null) ? 0 : minTorqueValQty.hashCode());
		result = prime * result + processId;
		result = prime * result
				+ ((qltyPtDescText == null) ? 0 : qltyPtDescText.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + teamId;
		result = prime * result
				+ ((torqueCharValue == null) ? 0 : torqueCharValue.hashCode());
		result = prime * result + unitId;
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
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
		MdrsIndependentUnit other = (MdrsIndependentUnit) obj;
		if (acceptedDate == null) {
			if (other.acceptedDate != null)
				return false;
		} else if (!acceptedDate.equals(other.acceptedDate))
			return false;
		if (acceptedLogonId == null) {
			if (other.acceptedLogonId != null)
				return false;
		} else if (!acceptedLogonId.equals(other.acceptedLogonId))
			return false;
		if (addedDate == null) {
			if (other.addedDate != null)
				return false;
		} else if (!addedDate.equals(other.addedDate))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (id != other.id)
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
		if (processId != other.processId)
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
		if (unitId != other.unitId)
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
		return toString(getId(), getAcceptedDate(), getAcceptedLogonId(), getAddedDate(), getExtractDate(), 
				getJointType(), getMaintDate(), getMaxTorqueValQty(), getMinTorqueValQty(), getProcessId(), 
				getQltyPtDescText(), getStatus(), getTeamId(), getTorqueCharValue(), getUnitId(), getUnitNo(), 
				getUnitOpDescText(), getUnitSeqNo(), getWorkPtDescText());
	}
}