package com.honda.ahm.lc.model;

public class DailyDepartmentSchedule extends AuditEntry {
	
	private DailyDepartmentScheduleId id;

	
	private String dayOfWeek;

	
	private String periodLabel;

	private String type;

	private String plan;

	
	private String startTime;

	
	private String endTime;

	
	private short nextDay;

	private int capacity;

	
	private int capacityOn;

	
	private String planCode;


	private String weekNo;

	private String iswork;

	
	private String startTimestamp;

	
	private String endTimestamp;


	public DailyDepartmentScheduleId getId() {
		return id;
	}


	public void setId(DailyDepartmentScheduleId id) {
		this.id = id;
	}


	public String getDayOfWeek() {
		return dayOfWeek;
	}


	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}


	public String getPeriodLabel() {
		return periodLabel;
	}


	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getPlan() {
		return plan;
	}


	public void setPlan(String plan) {
		this.plan = plan;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public short getNextDay() {
		return nextDay;
	}


	public void setNextDay(short nextDay) {
		this.nextDay = nextDay;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public int getCapacityOn() {
		return capacityOn;
	}


	public void setCapacityOn(int capacityOn) {
		this.capacityOn = capacityOn;
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


	public String getIswork() {
		return iswork;
	}


	public void setIswork(String iswork) {
		this.iswork = iswork;
	}


	public String getStartTimestamp() {
		return startTimestamp;
	}


	public void setStartTimestamp(String startTimestamp) {
		this.startTimestamp = startTimestamp;
	}


	public String getEndTimestamp() {
		return endTimestamp;
	}


	public void setEndTimestamp(String endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	
	
}
