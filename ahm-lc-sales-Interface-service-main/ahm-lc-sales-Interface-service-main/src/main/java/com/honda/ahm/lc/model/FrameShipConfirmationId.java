package com.honda.ahm.lc.model;

public class FrameShipConfirmationId {

	private static final long serialVersionUID = 1L;

	public FrameShipConfirmationId(){}
	
	private String processPointId;
	
	
	private String engineId;
	
	
	private String productId;

	public String getProcessPointId() {
		return processPointId;
	}


	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}


	public String getEngineId() {
		return engineId;
	}


	public void setEngineId(String engineId) {
		this.engineId = engineId;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
