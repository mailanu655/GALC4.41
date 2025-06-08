package com.honda.galc.dto;

import java.math.BigDecimal;

public class PddaDetailDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PLANT_LOC_CODE")
	private String plantLocCode;
	
	@DtoTag(outputName = "DEPT_CODE")
	private String deptCode;
	
	@DtoTag(outputName = "MODEL_YEAR_DATE")
	private BigDecimal modelYearDate;
	
	@DtoTag(outputName = "PROD_SCH_QTY")
	private BigDecimal prodSchQty;
	
	@DtoTag(outputName = "VEHICLE_MODEL_CODE")
	private String vehicleModelCode;
	
	@DtoTag(outputName = "PROD_ASM_LINE_NO")
	private String prodAsmLineNo;

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

	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public BigDecimal getProdSchQty() {
		return prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getVehicleModelCode() {
		return vehicleModelCode;
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getProdAsmLineNo() {
		return prodAsmLineNo;
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}
	
	
}
