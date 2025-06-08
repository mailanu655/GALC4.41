package com.honda.galc.dto.qi;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * <h3>QiAppliedRepairMethodDto Class description</h3>
 * <p> QiAppliedRepairMethodDto description </p>
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
 * Dec 20, 2016
 *
 */

/**
 * @author vcc00863
 *
 */
public class QiAppliedRepairMethodDto implements IDto {

	private static final long serialVersionUID = 1L;	

	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;

	@DtoTag(outputName = "REPAIR_TIME")
	private Integer repairTime;

	@DtoTag(outputName = "IS_COMPLETELY_FIXED")
	private Integer isCompletelyFixed;

	@DtoTag(outputName = "COMMENT")
	private String comment;

	@DtoTag(outputName = "REPAIR_TIMESTAMP")
	private Date repairTimestamp;	

	@DtoTag(outputName = "CREATE_USER")
	private String createUser;	
	
	@DtoTag(outputName = "UPDATE_USER")
	private String updateUser;
	
	@DtoTag(name = "REPAIR_METHOD_SEQ")
	private Integer repairMethodSeq;

	private String entryDept;

	private boolean fixedStatus;
	
	@DtoTag(name = "REPAIR_ID")
	private Long repairId;
	
	@DtoTag(outputName = "APPLICATION_ID")
	private String applicationId;
	
	private List<Long> repairIdList; //repair ID list in same transaction for bulk

	private List<Integer> repairMethodSeqList; //repair method seq list in same transaction for bulk
	
	public String getRepairMethod() {
		return StringUtils.trimToEmpty(repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	/**
	 * @return
	 */
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

	public String getFixedStatus() {
		return StringUtils.trimToEmpty(isCompletelyFixed == 1 ? "Y" : "N");
	}

	public void setDefectStatus(boolean fixedStatus) {

		this.fixedStatus = fixedStatus;
	}

	public String getComment() {
		return StringUtils.trimToEmpty(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getRepairTimestamp() {
		return repairTimestamp;
	}

	public void setRepairTimestamp(Date repairTimestamp) {
		this.repairTimestamp = repairTimestamp;
	}	

	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}	
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getEntryDept() {
		return StringUtils.trimToEmpty(entryDept);
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public Long getRepairId() {
		return repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	public Integer getRepairMethodSeq() {
		return repairMethodSeq;
	}

	public void setRepairMethodSeq(Integer repairMethodSeq) {
		this.repairMethodSeq = repairMethodSeq;
	}
	
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public List<Long> getRepairIdList() {
		return repairIdList;
	}

	public void setRepairIdList(List<Long> repairIdList) {
		this.repairIdList = repairIdList;
	}

	public List<Integer> getRepairMethodSeqList() {
		return repairMethodSeqList;
	}

	public void setRepairMethodSeqList(List<Integer> repairMethodSeqList) {
		this.repairMethodSeqList = repairMethodSeqList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((entryDept == null) ? 0 : entryDept.hashCode());
		result = prime * result + (fixedStatus ? 1231 : 1237);
		result = prime * result + ((isCompletelyFixed == null) ? 0 : isCompletelyFixed.hashCode());
		result = prime * result + ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + ((repairTime == null) ? 0 : repairTime.hashCode());
		result = prime * result + ((repairTimestamp == null) ? 0 : repairTimestamp.hashCode());
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiAppliedRepairMethodDto other = (QiAppliedRepairMethodDto) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		if (entryDept == null) {
			if (other.entryDept != null)
				return false;
		} else if (!entryDept.equals(other.entryDept))
			return false;
		if (fixedStatus != other.fixedStatus)
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
		return true;
	}


	@Override
	public String toString() {
		return "QiAppliedRepairMethodDto [repairMethod=" + repairMethod + ", repairTime=" + repairTime
				+ ", applicationId=" + applicationId
				+ ", isCompletelyFixed=" + isCompletelyFixed + ", comment=" + comment + ", repairTimestamp="
				+ repairTimestamp + ", createUser=" + createUser + ", updateUser=" + updateUser + ", entryDept=" + entryDept
				+ ", fixedStatus=" + fixedStatus + "]";
	}

}
