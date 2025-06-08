package com.honda.galc.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class OperationCheckerDto extends AbstractCheckerDto{

	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName="OPERATION_NAME")
	private String operationName;
	
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
	
	@DtoTag(name="APPROVED")
	private Date approved;
	
	@DtoTag(name="DEPRECATED")
	private Date deprecated;
	private String status;
	private boolean isSelected;
	
	public OperationCheckerDto() {
		super();
	}

	public OperationCheckerDto(String operationName, int operationRevision, String checkPoint, int checkSeq,
			String checkName, String checker, String reactionType, Date approved, Date deprecated, String status,
			boolean isSelected) {
		super();
		this.operationName = operationName;
		this.operationRevision = operationRevision;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
		this.checkName = checkName;
		this.checker = checker;
		this.reactionType = reactionType;
		this.approved = approved;
		this.deprecated = deprecated;
		this.status = status;
		this.isSelected = isSelected;
	}
	
	public String getOperationName() {
		return StringUtils.trimToEmpty(operationName);
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
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
	public String getCheckSeqAsString() {
		return checkSeq == 0 ? "" : StringUtils.trimToEmpty(String.valueOf(checkSeq));
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
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getUnitNumber() {
		return StringUtils.trimToEmpty(operationName.substring(0, 5));
	}
	
	@Override
	public String getKey() {
		return getOperationName().substring(0, 6);
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
		OperationCheckerDto other = (OperationCheckerDto) obj;
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
		return true;
	}
}
