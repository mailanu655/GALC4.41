package com.honda.galc.entity.mdrs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the MANPOWER_ASSIGNMENT database table.
 * 
 */
@Entity
@Table(name="MANPOWER_ASSIGNMENT", schema="VIOS")
public class ManpowerAssignment extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ManpowerAssignmentId id;

	@Column(name="ADDITIONAL_ASSOCIATE_ID", length=7)
	private String additionalAssociateId;

	@Column(name="ASSOCIATE_ID", length=7)
	private String associateId;

	@Column(name="DAYS_FLAG", nullable=false, length=1)
	private String daysFlag;

	@Column(name="ERGO_TYPE", nullable=false, length=1)
	private String ergoType;

	@Column(name="LAST_UPDATED_BY", nullable=false, length=7)
	private String lastUpdatedBy;

	@Column(name="MANPOWER_TAG_ID", nullable=false)
	private int manpowerTagId;

	@Column(name="PAIR_TYPE", nullable=false)
	private short pairType;

	@Column(name="PC_AREA_ID")
	private Integer pcAreaId;

	@Column(name="PROCESS_GROUP_ID")
	private Integer processGroupId;

	@Column(name="QUARTER", nullable=false)
	private short quarter;

	@Column(name="REASON_CODE", nullable=false)
	private short reasonCode;

	@Column(name="ROTATION_ID", nullable=false)
	private int rotationId;

	@Column(name="TEAM_ID")
	private Integer teamId;

	public ManpowerAssignment() {
	}

	public ManpowerAssignmentId getId() {
		return this.id;
	}

	public void setId(ManpowerAssignmentId id) {
		this.id = id;
	}

	public String getAdditionalAssociateId() {
		return StringUtils.trim(this.additionalAssociateId);
	}

	public void setAdditionalAssociateId(String additionalAssociateId) {
		this.additionalAssociateId = additionalAssociateId;
	}

	public String getAssociateId() {
		return StringUtils.trim(this.associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getDaysFlag() {
		return StringUtils.trim(this.daysFlag);
	}

	public void setDaysFlag(String daysFlag) {
		this.daysFlag = daysFlag;
	}

	public String getErgoType() {
		return StringUtils.trim(this.ergoType);
	}

	public void setErgoType(String ergoType) {
		this.ergoType = ergoType;
	}

	public String getLastUpdatedBy() {
		return StringUtils.trim(this.lastUpdatedBy);
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public int getManpowerTagId() {
		return this.manpowerTagId;
	}

	public void setManpowerTagId(int manpowerTagId) {
		this.manpowerTagId = manpowerTagId;
	}

	public short getPairType() {
		return this.pairType;
	}

	public void setPairType(short pairType) {
		this.pairType = pairType;
	}

	public Integer getPcAreaId() {
		return this.pcAreaId;
	}

	public void setPcAreaId(Integer pcAreaId) {
		this.pcAreaId = pcAreaId;
	}

	public Integer getProcessGroupId() {
		return this.processGroupId;
	}

	public void setProcessGroupId(Integer processGroupId) {
		this.processGroupId = processGroupId;
	}

	public short getQuarter() {
		return this.quarter;
	}

	public void setQuarter(short quarter) {
		this.quarter = quarter;
	}

	public short getReasonCode() {
		return this.reasonCode;
	}

	public void setReasonCode(short reasonCode) {
		this.reasonCode = reasonCode;
	}

	public int getRotationId() {
		return this.rotationId;
	}

	public void setRotationId(int rotationId) {
		this.rotationId = rotationId;
	}

	public Integer getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalAssociateId == null) ? 0 : additionalAssociateId.hashCode());
		result = prime * result
				+ ((associateId == null) ? 0 : associateId.hashCode());
		result = prime * result
				+ ((daysFlag == null) ? 0 : daysFlag.hashCode());
		result = prime * result
				+ ((ergoType == null) ? 0 : ergoType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastUpdatedBy == null) ? 0 : lastUpdatedBy.hashCode());
		result = prime * result + manpowerTagId;
		result = prime * result + (int)pairType;
		result = prime * result + ((pcAreaId == null) ? 0 : pcAreaId);
		result = prime * result + ((pcAreaId == null) ? 0 : processGroupId);
		result = prime * result + (int)quarter;
		result = prime * result + (int)reasonCode;
		result = prime * result + rotationId;
		result = prime * result + ((teamId == null) ? 0 : teamId);
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
		ManpowerAssignment other = (ManpowerAssignment) obj;
		if (additionalAssociateId == null) {
			if (other.additionalAssociateId != null)
				return false;
		} else if (!additionalAssociateId.equals(other.additionalAssociateId))
			return false;
		if (associateId == null) {
			if (other.associateId != null)
				return false;
		} else if (!associateId.equals(other.associateId))
			return false;
		if (daysFlag == null) {
			if (other.daysFlag != null)
				return false;
		} else if (!daysFlag.equals(other.daysFlag))
			return false;
		if (ergoType == null) {
			if (other.ergoType != null)
				return false;
		} else if (!ergoType.equals(other.ergoType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastUpdatedBy == null) {
			if (other.lastUpdatedBy != null)
				return false;
		} else if (!lastUpdatedBy.equals(other.lastUpdatedBy))
			return false;
		if (manpowerTagId != other.manpowerTagId)
			return false;
		if (pairType != other.pairType)
			return false;
		if (pcAreaId == null) {
			if (other.pcAreaId != null)
				return false;
		} else if (!pcAreaId.equals(other.pcAreaId))
			return false;
		if (processGroupId == null) {
			if (other.processGroupId != null)
				return false;
		} else if (!processGroupId.equals(other.processGroupId))
			return false;
		if (quarter != other.quarter)
			return false;
		if (reasonCode != other.reasonCode)
			return false;
		if (rotationId != other.rotationId)
			return false;
		if (teamId == null) {
			if (other.teamId != null)
				return false;
		} else if (!teamId.equals(other.teamId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getManpowerAssignmentId(), getId().getPlantLocCode(), getId().getDeptCode()
				, getId().getExtractDate(), getId().getLastUpdatedDate(), getAssociateId(), getAdditionalAssociateId() 
				, getDaysFlag(), getErgoType(), getLastUpdatedBy(), getManpowerTagId(), getPairType()
				, getPcAreaId(), getProcessGroupId(), getQuarter(), getReasonCode(), getRotationId(), getTeamId());
	}
}