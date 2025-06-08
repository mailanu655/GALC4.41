package com.honda.galc.entity.mdrs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the TRAINING_PROCESS database table.
 * 
 */
@Entity
@Table(name="TRAINING_PROCESS", schema="VIOS")
public class TrainingProcess extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TrainingProcessId id;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="LAST_UPDATED_BY", nullable=false, length=7)
	private String lastUpdatedBy;

	@Column(name="LINE_NUM")
	private Short lineNum;

	@Column(name="PROCESS_DESC", nullable=false, length=35)
	private String processDesc;

	@Column(name="PROCESS_NO", nullable=false, length=5)
	private String processNo;

	@Column(name="PROCESSID")
	private Integer processid;

	@Column(name="TEAM_ID")
	private Integer teamId;

	public TrainingProcess() {
	}

	public TrainingProcessId getId() {
		return this.id;
	}

	public void setId(TrainingProcessId id) {
		this.id = id;
	}

	public String getBodyLocNo() {
		return StringUtils.trim(this.bodyLocNo);
	}

	public void setBodyLocNo(String bodyLocNo) {
		this.bodyLocNo = bodyLocNo;
	}

	public String getLastUpdatedBy() {
		return StringUtils.trim(this.lastUpdatedBy);
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Short getLineNum() {
		return this.lineNum;
	}

	public void setLineNum(Short lineNum) {
		this.lineNum = lineNum;
	}

	public String getProcessDesc() {
		return StringUtils.trim(this.processDesc);
	}

	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}

	public String getProcessNo() {
		return StringUtils.trim(this.processNo);
	}

	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	public Integer getProcessid() {
		return this.processid;
	}

	public void setProcessid(Integer processid) {
		this.processid = processid;
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
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((lastUpdatedBy == null) ? 0 : lastUpdatedBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lineNum == null) ? 0 : ((int)lineNum));
		result = prime * result
				+ ((processDesc == null) ? 0 : processDesc.hashCode());
		result = prime * result
				+ ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result + ((processid == null) ? 0 : processid);
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
		TrainingProcess other = (TrainingProcess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null)
				return false;
		} else if (!bodyLocNo.equals(other.bodyLocNo))
			return false;
		if (lastUpdatedBy == null) {
			if (other.lastUpdatedBy != null)
				return false;
		} else if (!lastUpdatedBy.equals(other.lastUpdatedBy))
			return false;
		if (lineNum == null) {
			if (other.lineNum != null)
				return false;
		} else if (!lineNum.equals(other.lineNum))
			return false;
		if (processDesc == null) {
			if (other.processDesc != null)
				return false;
		} else if (!processDesc.equals(other.processDesc))
			return false;
		if (processNo == null) {
			if (other.processNo != null)
				return false;
		} else if (!processNo.equals(other.processNo))
			return false;
		if (processid == null) {
			if (other.processid != null)
				return false;
		} else if (!processid.equals(other.processid))
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
		return toString(getId().getProcessId(), getId().getPlantLocCode(), getId().getDeptCode()
				, getId().getExtractDate(), getId().getLastUpdatedDate(), getBodyLocNo()
				, getLastUpdatedBy(), getLineNum(), getProcessDesc(), getProcessNo()
				, getProcessid(), getTeamId());
	}

}