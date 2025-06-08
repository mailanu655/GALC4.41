package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the ROTATION database table.
 * 
 */
@Entity
@Table(name="ROTATION", schema="VIOS")
public class Rotation extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RotationId id;

	@Column(name="CREATED_BY", nullable=false, length=7)
	private String createdBy;

	@Column(name="CREATED_DATE", nullable=false)
	private Timestamp createdDate;

	@Column(name="IS_FINALIZED", length=18)
	private String isFinalized;

	@Column(name="LAST_UPDATED_BY", nullable=false, length=7)
	private String lastUpdatedBy;

	@Column(name="LINE_NUM")
	private Short lineNum;

	@Column(name="PC_AREA_ID")
	private Integer pcAreaId;

	@Column(name="PRODUCTION_DATE", nullable=false)
	private Timestamp productionDate;

	@Column(name="SHIFT_ID", nullable=false)
	private short shiftId;

	@Column(name="TEAM_GROUP_ID")
	private Integer teamGroupId;

	@Column(name="TEAM_ID")
	private Integer teamId;

	public Rotation() {
	}

	public RotationId getId() {
		return this.id;
	}

	public void setId(RotationId id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return StringUtils.trim(this.createdBy);
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsFinalized() {
		return StringUtils.trim(this.isFinalized);
	}

	public void setIsFinalized(String isFinalized) {
		this.isFinalized = isFinalized;
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

	public Integer getPcAreaId() {
		return this.pcAreaId;
	}

	public void setPcAreaId(Integer pcAreaId) {
		this.pcAreaId = pcAreaId;
	}

	public Timestamp getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
	}

	public short getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(short shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getTeamGroupId() {
		return this.teamGroupId;
	}

	public void setTeamGroupId(Integer teamGroupId) {
		this.teamGroupId = teamGroupId;
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
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((isFinalized == null) ? 0 : isFinalized.hashCode());
		result = prime * result
				+ ((lastUpdatedBy == null) ? 0 : lastUpdatedBy.hashCode());
		result = prime * result + ((lineNum == null) ? 0 : ((int)lineNum));
		result = prime * result + ((pcAreaId == null) ? 0 : pcAreaId);
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + (int)shiftId;
		result = prime * result + ((teamGroupId == null) ? 0 : teamGroupId);
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
		Rotation other = (Rotation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (isFinalized == null) {
			if (other.isFinalized != null)
				return false;
		} else if (!isFinalized.equals(other.isFinalized))
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
		if (pcAreaId == null) {
			if (other.pcAreaId != null)
				return false;
		} else if (!pcAreaId.equals(other.pcAreaId))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shiftId != other.shiftId)
			return false;
		if (teamGroupId == null) {
			if (other.teamGroupId != null)
				return false;
		} else if (!teamGroupId.equals(other.teamGroupId))
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
		return toString(getId().getRotationId(), getId().getPlantLocCode(), getId().getDeptCode()
				, getId().getExtractDate(), getId().getLastUpdatedDate(), getCreatedBy(), getCreatedDate() 
				, getIsFinalized(), getLastUpdatedBy(), getLineNum(),  getPcAreaId(), getProductionDate()
				, getShiftId(), getTeamGroupId(), getTeamId());
	}
}