package com.honda.galc.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;

public class PartCheckerDto extends AbstractCheckerDto{
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "OPERATION_NAME")
	private String operationName;
	
	@DtoTag(name="PART_ID")
	private String partId;
	
	@DtoTag(name="OP_REV")
	private int operationRevision;

	@DtoTag(name="CHECK_POINT")
	private String checkPoint;

	@DtoTag(name="CHECK_SEQ")
	private int checkSeq;
	
	@DtoTag(name="CHECK_NAME")
	private String checkName;
	
	@DtoTag(name="CHECKER")
	private String checker;
	
	@DtoTag(name="REACTION_TYPE")
	private String reactionType;
	
	@DtoTag(name="PART_NO")
	private String partNo;
	
	@DtoTag(name="PART_ITEM_NO")
	private String partItemNo;
	
	@DtoTag(name="PART_SECTION_CODE")
	private String partSectionCode;
	
	@DtoTag(name="PART_TYPE")
	private String partType;
	
	@DtoTag(name="APPROVED")
	private Date approved;
	
	@DtoTag(name="DEPRECATED")
	private Date deprecated;
	
	private String status;
	
	private boolean isSelected;
	
	public PartCheckerDto() {
		super();
	}
	public PartCheckerDto(String operationName, String partId, int operationRevision, String checkPoint, int checkSeq,
			String checkName, String checker, String reactionType, String partNo, String partItemNo,
			String partSectionCode, String partType, Date approved, Date deprecated, String status,
			boolean isSelected) {
		super();
		this.operationName = operationName;
		this.partId = partId;
		this.operationRevision = operationRevision;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
		this.checkName = checkName;
		this.checker = checker;
		this.reactionType = reactionType;
		this.partNo = partNo;
		this.partItemNo = partItemNo;
		this.partSectionCode = partSectionCode;
		this.partType = partType;
		this.approved = approved;
		this.deprecated = deprecated;
		this.status = status;
		this.isSelected = isSelected;
	}
	public String getOperationName() {
		return StringUtils.trimToEmpty(this.operationName);
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getPartId() {
		return StringUtils.trimToEmpty(this.partId);
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public int getOperationRevision() {
		return operationRevision;
	}
	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}
	public String getCheckPoint() {
		return StringUtils.trimToEmpty(this.checkPoint);
	}
	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}
	public int getCheckSeq() {
		return checkSeq;
	}
	public void setCheckSeq(int checkSeq) {
		this.checkSeq = checkSeq;
	}
	public String getCheckName() {
		return StringUtils.trimToEmpty(this.checkName);
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getChecker() {
		return StringUtils.trimToEmpty(this.checker);
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getReactionType() {
		return StringUtils.trimToEmpty(this.reactionType);
	}
	public void setReactionType(String reactionType) {
		this.reactionType = reactionType;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public Date getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Date deprecated) {
		this.deprecated = deprecated;
	}

	public String getStatus() {
		if(approved != null && deprecated == null)
			status = "ACTIVE";
		else
			status = "INACTIVE";
		return status;
	}

	public String getPartNo() {
		return StringUtils.trimToEmpty(this.partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartItemNo() {
		return StringUtils.trimToEmpty(this.partItemNo);
	}
	public void setPartItemNo(String partItemNo) {
		this.partItemNo = partItemNo;
	}
	public String getPartSectionCode() {
		return StringUtils.trimToEmpty(this.partSectionCode);
	}
	public void setPartSectionCode(String partSectionCode) {
		this.partSectionCode = partSectionCode;
	}
	public String getPartType() {
		return partType;
	}
	public void setPartType(String partType) {
		this.partType = partType;
	}

	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	@Override
	public String getCheckSeqAsString() {
		return checkSeq == 0 ? "" : StringUtils.trimToEmpty(String.valueOf(checkSeq));
	}
	
	@Override
	public String getKey() {
		return this.getOperationName().substring(0, 6)+
				Delimiter.HYPHEN+this.getPartNo()+
				Delimiter.HYPHEN+this.getPartItemNo()+
				Delimiter.HYPHEN+this.getPartSectionCode()+
				Delimiter.HYPHEN+this.getPartType();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checkName == null) ? 0 : checkName.hashCode());
		result = prime * result
				+ ((checkPoint == null) ? 0 : checkPoint.hashCode());
		result = prime * result + checkSeq;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
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
		PartCheckerDto other = (PartCheckerDto) obj;
		if (checkName == null) {
			if (other.checkName != null)
				return false;
		} else if (!checkName.equals(other.checkName))
			return false;
		if (checkPoint == null) {
			if (other.checkPoint != null)
				return false;
		} else if (!checkPoint.equals(other.checkPoint))
			return false;
		if (checkSeq != other.checkSeq)
			return false;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		return true;
	}
}
