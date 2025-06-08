package com.honda.galc.client.dto;

public class MCOperationMatrixDTO {
	private String operationName;
	private String specCodeType;
	private String specCodeMask;
	private Integer operationRev;
	private Integer pddaPlatFormId;
	private String effectiveBeginDate;
	private String effectiveEndDate;
	
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getSpecCodeType() {
		return specCodeType;
	}
	public void setSpecCodeType(String specCodeType) {
		this.specCodeType = specCodeType;
	}
	public String getSpecCodeMask() {
		return specCodeMask;
	}
	public void setSpecCodeMask(String specCodeMask) {
		this.specCodeMask = specCodeMask;
	}
	public Integer getOperationRev() {
		return operationRev;
	}
	public void setOperationRev(Integer operationRev) {
		this.operationRev = operationRev;
	}
	public Integer getPddaPlatFormId() {
		return pddaPlatFormId;
	}
	public void setPddaPlatFormId(Integer pddaPlatFormId) {
		this.pddaPlatFormId = pddaPlatFormId;
	}
	public String getEffectiveBeginDate() {
		return effectiveBeginDate;
	}
	public void setEffectiveBeginDate(String effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
}
