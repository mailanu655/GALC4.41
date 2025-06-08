package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;


public class ErrorData implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;

	public static final String ON = "1";
	public static final String OFF = "0";

	private String errorBit;
	private String errorCode;
	private String errorMessage;

	public ErrorData() {
		super();
	}

	public ErrorData(String errorBit, String errorCode, String errorMessage) {
		super();
		this.errorBit = StringUtils.trim(errorBit);
		this.errorCode = StringUtils.trim(errorCode);
		this.errorMessage = StringUtils.trim(errorMessage);
	}

	public String getErrorBit() {
		return this.errorBit;
	}

	public void setErrorBit(String errorBit) {
		this.errorBit = StringUtils.trim(errorBit);
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = StringUtils.trim(errorCode);
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringUtils.trim(errorMessage);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isErrorBit(){
		return ON.equals(getErrorBit());
	}

	@Override
	public String toString() {
		return this.errorBit+","+this.errorCode+","+this.errorMessage;
	}

}
