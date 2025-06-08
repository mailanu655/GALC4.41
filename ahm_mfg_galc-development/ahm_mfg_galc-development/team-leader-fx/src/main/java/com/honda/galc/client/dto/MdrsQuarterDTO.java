package com.honda.galc.client.dto;

import javafx.scene.control.CheckBox;

public class MdrsQuarterDTO implements Selectable{
	private String line;
	private String dept;
	private String quarter;
	private String plantLocCode;
	private String shiftId;
	private CheckBox selected;
	
	public CheckBox getSelected() {
		return selected;
	}
	public void setSelected(CheckBox selected) {
		this.selected = selected;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public String getPlantLocCode() {
		return plantLocCode;
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
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
	
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public boolean isSelected() {
		if (getSelected().isSelected())
			return true;
		else
			return false;
	}

}
