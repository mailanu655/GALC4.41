package com.honda.galc.client.dto;

import java.math.BigDecimal;

public class ChangeFormDTO {
	private int changeFormId;
	private BigDecimal prodSchQty;
	private BigDecimal modelYearDate;
	private String plantLocCode;
	private String deptCode;
	private String changeFormType;
	private String prodAsmLineNo;
	private String vehicleModelCode;
	private String asmProcNumbers;
	private String asmProcNames;
	private int controlNo;
	
	
	public int getControlNo() {
		return controlNo;
	}
	public void setControlNo(int controlNo) {
		this.controlNo = controlNo;
	}
	public int getChangeFormId() {
		return changeFormId;
	}
	public void setChangeFormId(int changeFormId) {
		this.changeFormId = changeFormId;
	}
	public BigDecimal getProdSchQty() {
		return prodSchQty;
	}
	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}
	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}
	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}
	public String getPlantLocCode() {
		return plantLocCode;
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getChangeFormType() {
		return changeFormType;
	}
	public void setChangeFormType(String changeFormType) {
		this.changeFormType = changeFormType;
	}
	public String getProdAsmLineNo() {
		return prodAsmLineNo;
	}
	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}
	public String getVehicleModelCode() {
		return vehicleModelCode;
	}
	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	public String getAsmProcNumbers() {
		return asmProcNumbers;
	}
	public void setAsmProcNumbers(String asmProcNumbers) {
		this.asmProcNumbers = asmProcNumbers;
	}
	public String getAsmProcNames() {
		return asmProcNames;
	}
	public void setAsmProcNames(String asmProcNames) {
		this.asmProcNames = asmProcNames;
	}
	
	@Override
	public String toString() {
		return "ChangeFormDTO [changeFormId=" + changeFormId + ", prodSchQty="
				+ prodSchQty + ", modelYearDate=" + modelYearDate
				+ ", plantLocCode=" + plantLocCode + ", deptCode=" + deptCode
				+ ", changeFormType=" + changeFormType + ", prodAsmLineNo="
				+ prodAsmLineNo + ", vehicleModelCode=" + vehicleModelCode
				+ ", asmProcNumbers=" + asmProcNumbers + ", asmProcNames="
				+ asmProcNames + "]";
	}
}
