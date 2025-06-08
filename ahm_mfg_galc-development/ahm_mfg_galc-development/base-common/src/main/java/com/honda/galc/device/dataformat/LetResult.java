package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class LetResult extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String letResult;
	
	public LetResult() {
		super();
	}

	public LetResult(String letResult) {
		super();
		this.letResult = StringUtils.trim(letResult);
	}

	// Getters & Setters
	public String getLetResult() {
		return letResult;
	}

	public void setLetResult(String letResult) {
		this.letResult = StringUtils.trim(letResult);
	}

	public void setLetResultWithoutTrim(String letResult) {
		this.letResult = letResult;
	}

	@Override
	public String toString() {
		return this.letResult;
	}
	
	
}
