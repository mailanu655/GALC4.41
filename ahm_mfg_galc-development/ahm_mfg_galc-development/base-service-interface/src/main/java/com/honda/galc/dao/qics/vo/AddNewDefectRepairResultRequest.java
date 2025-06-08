package com.honda.galc.dao.qics.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.honda.galc.entity.qics.DefectRepairResult;

public class AddNewDefectRepairResultRequest {
	String productId;
	String processPointId; 
	Integer defectResultId;
	DefectRepairResult newDefectRepairResult;
	Integer defectStatusId;
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
	public void setDefectStatusId(Integer defectStatusId) {
		this.defectStatusId = defectStatusId;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
