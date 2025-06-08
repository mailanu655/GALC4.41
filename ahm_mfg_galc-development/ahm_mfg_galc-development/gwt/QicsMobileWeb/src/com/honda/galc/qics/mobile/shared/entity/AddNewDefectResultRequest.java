package com.honda.galc.qics.mobile.shared.entity;


public class AddNewDefectResultRequest {
	private String productId;
	private String processPointId; 
	private DefectResult newDefectResult;
	
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