package com.honda.galc.dto.lcvinbom;

import com.honda.galc.dto.IDto;

public class ErrorMessageDto implements IDto {
	private static final long serialVersionUID = 1L;

	private String code;
	
	private String message;

	public ErrorMessageDto(String errorCode,String errorMessage) {
		this.code = errorCode;
		this.message = errorMessage;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
