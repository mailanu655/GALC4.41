package com.honda.galc.oif.dto;

import java.sql.Date;

import com.honda.galc.util.GPCSData;

//Additional fields not in DailyDepartmentSchedule
public class CapacityCalendarDTO implements IPlanCodeDTO {
	@GPCSData("CAPACITY")
	private int capacity;
	@GPCSData("DEPARTMENT_CODE")
	private String departmentCode;
	@GPCSData("ON_OFF_FLAG")
	private String onOffFlag;
	@GPCSData("PLAN_CODE")
	private String planCode;
	@GPCSData("PLANT_CODE")
	private String plantCode;
	@GPCSData("WEEK_NO")
	private String weekNo;
	@GPCSData("IS_WORK")
	private String isWork;
	@GPCSData("LINE_NO")
	private String lineNo;
	@GPCSData("PROCESS_LOCATION")
	private String processLocation;
	@GPCSData("PRODUCTION_DATE")
	private Date productionDate;
	@GPCSData("SHIFT")
	private String shift;
	@GPCSData("DAY_OF_WEEK")
	private String dayOfWeek;

	public CapacityCalendarDTO() {
	}

// Copy Constructor
	public CapacityCalendarDTO(CapacityCalendarDTO objCapacity) {
		this.capacity = objCapacity.getCapacity();
		this.dayOfWeek = objCapacity.getDayOfWeek();
		this.departmentCode = objCapacity.getDepartmentCode();
		this.isWork = objCapacity.getIsWork();
		this.lineNo = objCapacity.getLineNo();
		this.onOffFlag = objCapacity.getOnOffFlag();
		this.planCode = objCapacity.getPlanCode();
		this.plantCode = objCapacity.getPlantCode();
		this.processLocation = objCapacity.getProcessLocation();
		this.productionDate = objCapacity.getProductionDate();
		this.shift = objCapacity.getShift();
		this.weekNo = objCapacity.getWeekNo();
	}

	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getOnOffFlag() {
		return onOffFlag;
	}
	public void setOnOffFlag(String onOffFlag) {
		this.onOffFlag = onOffFlag;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}
	public String getIsWork() {
		return isWork;
	}
	public void setIsWork(String isWork) {
		this.isWork = isWork;
	}
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
	public Date getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getPlantCode() {
		return plantCode;
	}
}
