package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;


public class ApplicationCheckerDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(name="APPLICATION_ID")
	private String applicationId;

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
	
	@DtoTag(name = "PROCESS_POINT_NAME")
	private String processPointName;
	
	private boolean isSelected;
	
	public String getApplicationId() {
		return StringUtils.trimToEmpty(this.applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
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

	public String getProcessPointName() {
		return StringUtils.trimToEmpty(this.processPointName);
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
