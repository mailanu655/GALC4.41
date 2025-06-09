package com.honda.ahm.lc.vdb.dto;

public class ProductIdDto {
	
	private String productId;
	private String productSpecCode;
	private String engineSerialNo;
	private String missionSerialNo;
	
	public ProductIdDto() {
		super();
	}

	public ProductIdDto(String productId, String productSpecCode,String engineSerialNo, String missionSerialNo) {
		super();
		this.productId = productId;
		this.productSpecCode = productSpecCode;
		this.engineSerialNo = engineSerialNo;
		this.missionSerialNo = missionSerialNo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public String getMissionSerialNo() {
		return missionSerialNo;
	}

	public void setMissionSerialNo(String missionSerialNo) {
		this.missionSerialNo = missionSerialNo;
	}
	
	

}
