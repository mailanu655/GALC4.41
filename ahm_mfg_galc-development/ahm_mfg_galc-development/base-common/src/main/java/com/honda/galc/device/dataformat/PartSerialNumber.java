package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class PartSerialNumber extends InputData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String partSn;
	
	public PartSerialNumber() {
		super();
	}

	public PartSerialNumber(String partSerialNumber) {
		super();
		this.partSn = StringUtils.trim(partSerialNumber);
	}

	//Getters & Setters
	public String getPartSn() {
		return partSn;
	}

	public void setPartSn(String partSerialNumber) {
		this.partSn = StringUtils.trim(partSerialNumber);
	}

	@Override
	public String toString() {
		return this.partSn;
	}
}
