package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class VoltageMeterResult extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String voltageMeterResult;
	
	public VoltageMeterResult() {
		super();
	}

	public VoltageMeterResult(String voltageMeterResult) {
		super();
		this.voltageMeterResult = StringUtils.trim(voltageMeterResult);
	}

	// Getters & Setters
	public String getVoltageMeterResult() {
		return voltageMeterResult;
	}

	public void setVoltageMeterResult(String voltageMeterResult) {
		this.voltageMeterResult = StringUtils.trim(voltageMeterResult);
	}

	public void setVoltageMeterResultWithoutTrim(String voltageMeterResult) {
		this.voltageMeterResult = voltageMeterResult;
	}

	@Override
	public String toString() {
		return this.voltageMeterResult;
	}
	
	
}
