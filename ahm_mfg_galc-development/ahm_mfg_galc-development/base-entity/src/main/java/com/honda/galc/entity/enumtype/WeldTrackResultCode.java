package com.honda.galc.entity.enumtype;

/**
*
* @author Jiamei Li <br>
* @date   Dec 05, 2014
*/
public enum WeldTrackResultCode {

	ERROR("0", "Problem with GALC processing request."), 
	REQUEST_PROCESSED_OK("1", "Request Processed OK (ACK)"), 
	DUPLICATE_REQUEST("2", "Duplicate Request");

	private String infoCode;
	private String infoMsg;

	private WeldTrackResultCode(String code, String msg) {
		this.infoCode = code;
		this.infoMsg = msg;
	}

	public String getInfoCode() {
		return infoCode;
	}

	public String getInfoMsg() {
		return infoMsg;
	}
}
