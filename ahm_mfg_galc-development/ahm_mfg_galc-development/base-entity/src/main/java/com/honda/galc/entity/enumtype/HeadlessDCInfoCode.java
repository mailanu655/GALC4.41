package com.honda.galc.entity.enumtype;

public enum HeadlessDCInfoCode {
	PRODUCT_OK			("01", "Ref# $1 is OK"),
	OUTSTANDING			("02", "Ref# $1 has outstanding defect"),
	ONHOLD				("03", "Ref# $1 is onhold"),
	SCRAP				("04", "Ref# $1 is scrap"),
	NOT_EXIST			("05", "Ref# $1 does not exist"),
	UNKNOWN				("06", "Ref# $1 status is unknown"),
	INVALID_PRODUCT		("07", "Ref# $1 is invalid product id"),
	UNMATCHED			("08", "Product spec code is not in the correct format"),
	UNKNOWN_PRODUCTIONLOT("09", "ProductionLot with product spec code and plan code does not exist"),
	UNEXPECTED_OP_TYPE	("10", "Unexpected operation type"),
	SAVE_SUCCESS		("11", "Ref# $1 Saved OK"),
	SAVE_FAILED			("12", "Ref# $1 Failed to Save");
	
	
	private String infoCode;
	private String infoMsg;
	
	private HeadlessDCInfoCode(String code, String msg) {
		infoCode = code;
		infoMsg = msg;
	}

	public String getInfoCode() {
		return infoCode;
	}

	public String getInfoMsg(String productId) {
		if(infoMsg.contains("$1")){
			return infoMsg.replace("$1", productId == null ? "" : productId);
		}else{
			return infoMsg;
		}
	}
}
