package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.util.StringUtil;

public class MeasurementValue extends MeasurementInputData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected double measurementValue;
	
	public MeasurementValue(){
		super();
	}
	
	public MeasurementValue(double measurementValue){
		super();
		this.measurementValue = measurementValue;
	}

	public double getMeasurementValue() {
		return measurementValue;
	}

	public void setMeasurementValue(double measurementValue) {
		this.measurementValue = measurementValue;
	}

	@Override
	public String toString(){
		return StringUtil.toString(String.valueOf(this.measurementValue), getMeasurementIndex());
	}
}
