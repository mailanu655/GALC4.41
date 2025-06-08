package com.honda.galc.client.dto;

import javafx.scene.control.CheckBox;

public class GALCPeriodDTO implements Selectable{
	private String line;
	private String dept;
	private String shift;
	private String period;
	private String periodLabel;
	private String plantCode;
	private CheckBox selected;
	
	
	public String getPlantCode() {
		return plantCode;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getPeriodLabel() {
		return periodLabel;
	}
	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}
	
	public CheckBox getSelected() {
		return selected;
	}
	public void setSelected(CheckBox selected) {
		this.selected = selected;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public boolean isSelected() {
		if (getSelected().isSelected())
			return true;
		else
			return false;
	}
	

}
