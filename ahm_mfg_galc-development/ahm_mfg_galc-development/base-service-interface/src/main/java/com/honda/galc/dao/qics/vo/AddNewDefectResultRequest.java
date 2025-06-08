package com.honda.galc.dao.qics.vo;

import com.honda.galc.entity.qics.DefectResult;

public class AddNewDefectResultRequest {
	String productId;
	String processPointId; 
	DefectResult newDefectResult;
	
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
	public DefectResult getNewDefectResult() {
		return newDefectResult;
	}
	public void setNewDefectResult(DefectResult newDefectResult) {
		this.newDefectResult = newDefectResult;
	}

}
