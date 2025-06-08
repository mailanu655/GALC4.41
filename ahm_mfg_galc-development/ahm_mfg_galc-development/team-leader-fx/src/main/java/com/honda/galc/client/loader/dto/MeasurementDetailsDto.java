package com.honda.galc.client.loader.dto;


public class MeasurementDetailsDto {

	private int measurementSeqNo;
	private double min;
	private double max;
	private String pset;
	private String tool;
	
	public MeasurementDetailsDto(){}
	
	public MeasurementDetailsDto(int measurementSeqNo, double min, double max, String pset, String tool) {
		this.measurementSeqNo = measurementSeqNo;
		this.min = min;
		this.max = max;
		this.pset = pset;
		this.tool = tool;
	}

	public int getMeasurementSeqNo() {
		return measurementSeqNo;
	}

	public void setMeasurementSeqNo(int measurementSeqNo) {
		this.measurementSeqNo = measurementSeqNo;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public String getPset() {
		return pset;
	}

	public void setPset(String pset) {
		this.pset = pset;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	
}
