package com.honda.galc.client.enumtype;

import com.honda.galc.common.logging.Logger;

public enum FloorStampInfoCodes {
	// Ordered least significant to most significant
	REQUEST_VIN_OK 					("01", "VIN @VIN_INFO@ - Next VIN Ready  "),
	REQUEST_VIN_SKIPPED				("04", "Skipped VIN @VIN_INFO@           "),
	REQUEST_VIN_ALREADY_PROCESSED 	("31", "Already Requested VIN @VIN_INFO@ "),
	REQUEST_VIN_RFID_NG				("20", "No RFID Data For @VIN_INFO@      "),
	REQUEST_VIN_INVALID				("03", "Invalid VIN Request @VIN_INFO@   "),
	REQUEST_VIN_NO_NEXT_VIN 		("30", "No Next VIN For @VIN_INFO@       "),
		
	RESULT_VIN_OK					("51", "Results OK VIN @VIN_INFO@        "),
	RESULT_VIN_SKIPPED				("55", "Skipped VIN @VIN_INFO@           "),
	RESULT_VIN_ALREADY_PROCESSED	("52", "Already Sent VIN @VIN_INFO@      "),
	RESULT_VIN_RFID_NG				("70", "RFID Tag Write Error @VIN_INFO@  "),
	RESULT_VIN_INVALID				("53", "Invalid VIN Result @VIN_INFO@    ");

	private final String _infoCode;
	private final String _infoMsg;

	private FloorStampInfoCodes(String code, String msg) {
		_infoCode = code;
		_infoMsg = msg;
	}

	public String getInfoCode() {
		return _infoCode;
	}

	public String getInfoMessage(String vinInfo) {
		String infoMessage = _infoMsg;
		
		if (vinInfo != null) {
			infoMessage = infoMessage.replace("@VIN_INFO@", vinInfo);
		}
		if(infoMessage.length() > 40) {
			infoMessage = infoMessage.substring(0, 40);
		}
		
		Logger.getLogger().info("Returning info message: " + infoMessage);
		return infoMessage;
	}
}
