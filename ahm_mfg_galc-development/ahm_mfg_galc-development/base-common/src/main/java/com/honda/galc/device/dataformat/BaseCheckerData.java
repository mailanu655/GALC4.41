package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class BaseCheckerData extends ProductId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String currentProcessPoint;
	
	public BaseCheckerData() {
		super();
	}

	public BaseCheckerData(String data, String currentProcessPoint) {
		super(data);
		this.currentProcessPoint = StringUtils.trim(currentProcessPoint);		
	}
	
	

	public String getCurrentProcessPoint() {
		return currentProcessPoint;
	}

	public void setCurrentProcessPoint(String currentProcessPoint) {
		this.currentProcessPoint = StringUtils.trim(currentProcessPoint);
	}
}
