package com.honda.galc.qics.mobile.shared.entity;


public class AddNewDefectRepairResultRequest {
	String productId;
	String processPointId; 
	Integer defectResultId;
	DefectRepairResult newDefectRepairResult;
	private Integer defectStatusId;
	Integer repairTimePlan;
	
	
	
	public Integer getRepairTimePlan() {
		return repairTimePlan;
	}
	public void setRepairTimePlan(Integer repairTimePlan) {
		this.repairTimePlan = repairTimePlan;
	}	
	public Integer getDefectStatusId() {
		return defectStatusId;
	}
	public void setDefectStatusId(Integer defectStatus) {
		this.defectStatusId = defectStatus;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public Integer getDefectResultId() {
		return defectResultId;
	}
	public void setDefectResultId(Integer defectResultId) {
		this.defectResultId = defectResultId;
	}
	public DefectRepairResult getNewDefectRepairResult() {
		return newDefectRepairResult;
	}
	public void setNewDefectRepairResult(DefectRepairResult newDefectRepairResult) {
		this.newDefectRepairResult = newDefectRepairResult;
	}


}