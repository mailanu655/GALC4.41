package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;


public class ErrorDataIdentifier extends ErrorData implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;

	public static final String ON = "1";
	public static final String OFF = "0";

	private String errorBit;
	private String errorCode;
	private String errorMessage;
	private String identifier;

	public ErrorDataIdentifier() {
		super();
	}

	public ErrorDataIdentifier(String errorBit, String errorCode, String errorMessage, String identifier) {
		super(errorBit, errorCode, errorMessage);
		this.identifier = StringUtils.trim(identifier);
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = StringUtils.trim(identifier);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean isErrorBit(){
		return ON.equals(getErrorBit());
	}

	@Override
	public String toString() {
		return super.toString()+","+this.identifier;
	}

}
