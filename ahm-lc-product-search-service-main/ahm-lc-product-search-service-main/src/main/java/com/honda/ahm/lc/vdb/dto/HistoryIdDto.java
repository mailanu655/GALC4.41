package com.honda.ahm.lc.vdb.dto;

public class HistoryIdDto {
	private String productId;
	private String productSpecCode;
	private String engineSerialNo;
	
	public HistoryIdDto() {
		super();
	}

	public HistoryIdDto(String productId, String productSpecCode,String engineSerialNo) {
		super();
		this.productId = productId;
		this.productSpecCode = productSpecCode;
		this.engineSerialNo = engineSerialNo;

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

	
	
	
}
