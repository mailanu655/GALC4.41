package com.honda.ahm.lc.model;

public class DailyDepartmentScheduleId  {
	
	private String lineNo;

	
	private String processLocation;

	
	private String plantCode;

	
	private String productionDate;

	private String shift;

	private int period;

	public String getLineNo() {
		return lineNo;
	}


	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}


	public String getProcessLocation() {
		return processLocation;
	}


	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}


	public String getPlantCode() {
		return plantCode;
	}


	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}


	public String getProductionDate() {
		return productionDate;
	}


	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}


	public int getPeriod() {
		return period;
	}


	public void setPeriod(int period) {
		this.period = period;
	}


	public String getShift() {
		return shift;
	}


	public void setShift(String shift) {
		this.shift = shift;
	}
	
	
	
}
