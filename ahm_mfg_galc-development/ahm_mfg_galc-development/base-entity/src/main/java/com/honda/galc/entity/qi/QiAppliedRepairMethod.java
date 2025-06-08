package com.honda.galc.entity.qi;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * <h3>QiAppliedRepairMethod Class description</h3>
 * <p>
 * QiAppliedRepairMethod description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         Dec 20, 2016
 *
 *
 */

@Entity
@Table(name = "QI_APPLIED_REPAIR_METHOD_TBX")
public class QiAppliedRepairMethod extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private QiAppliedRepairMethodId id;

	@Column(name = "REPAIR_METHOD")
	private String repairMethod;

	@Column(name = "REPAIR_TIME")
	private Integer repairTime;

	@Column(name = "IS_COMPLETELY_FIXED")
	private Integer isCompletelyFixed;
	
	@Column(name = "APPLICATION_ID")
	private String applicationId;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "REPAIR_TIMESTAMP")
	private Timestamp repairTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	@Column(name = "PRODUCTION_DATE")
	private Date productionDate;
	
	@Column(name = "SHIFT")
	private String shift;
	
	@Column(name = "TEAM")
	private String team;
	
	public QiAppliedRepairMethodId getId() {
		return id;
	}

	public void setId(QiAppliedRepairMethodId id) {
		this.id = id;
	}

	public String getRepairMethod() {
		return StringUtils.trimToEmpty(repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public Integer getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Integer repairTime) {
		this.repairTime = repairTime;
	}

	public Integer getIsCompletelyFixed() {
		return isCompletelyFixed;
	}

	public void setIsCompletelyFixed(Integer selectedRadioValue) {
		this.isCompletelyFixed = selectedRadioValue;
	}

	public String getComment() {
		return StringUtils.trimToEmpty(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getRepairTimestamp() {
		return repairTimestamp;
	}

	public void setRepairTimestamp(Timestamp repairTimestamp) {
		this.repairTimestamp = repairTimestamp;
	}

	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getShift() {
		return StringUtils.trimToEmpty(shift);
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getTeam() {
		return StringUtils.trimToEmpty(team);
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isCompletelyFixed == null) ? 0 : isCompletelyFixed.hashCode());
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + ((repairTime == null) ? 0 : repairTime.hashCode());
		result = prime * result + ((repairTimestamp == null) ? 0 : repairTimestamp.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
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
		QiAppliedRepairMethod other = (QiAppliedRepairMethod) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isCompletelyFixed == null) {
			if (other.isCompletelyFixed != null)
				return false;
		} else if (!isCompletelyFixed.equals(other.isCompletelyFixed))
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (repairTime == null) {
			if (other.repairTime != null)
				return false;
		} else if (!repairTime.equals(other.repairTime))
			return false;
		if (repairTimestamp == null) {
			if (other.repairTimestamp != null)
				return false;
		} else if (!repairTimestamp.equals(other.repairTimestamp))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiAppliedRepairMethod [id=" + id + ", repairMethod=" + repairMethod + ", applicationId=" + applicationId
				+ ", repairTime=" + repairTime
				+ ", isCompletelyFixed=" + isCompletelyFixed + ", comment=" + comment + ", repairTimestamp="
				+ repairTimestamp + ",updateUser=" + updateUser 
				+ ",productionDate=" + productionDate + ",shift=" + shift + ",team=" + team + "]";
	}
}
