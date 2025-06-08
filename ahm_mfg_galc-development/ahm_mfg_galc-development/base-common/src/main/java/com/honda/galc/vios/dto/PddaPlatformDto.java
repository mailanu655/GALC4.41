package com.honda.galc.vios.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

public class PddaPlatformDto implements IDto{

	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PLANT_LOC_CODE")
	private String plantLocCode;
	
	@DtoTag(outputName = "DEPT_CODE")
	private String deptCode;
	
	@DtoTag(outputName = "MODEL_YEAR_DATE")
	private float modelYearDate;
	
	@DtoTag(outputName = "PROD_SCH_QTY")
	private float prodSchQty;
	
	@DtoTag(outputName = "PROD_ASM_LINE_NO")
	private String prodAsmLineNo;
	
	@DtoTag(outputName = "VEHICLE_MODEL_CODE")
	private String vehicleModelCode;
	
	public String getPlantLocCode() {
		return StringUtils.trimToEmpty(plantLocCode);
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDeptCode() {
		return StringUtils.trimToEmpty(deptCode);
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public float getModelYearDate() {
		return modelYearDate;
	}
	public void setModelYearDate(float modelYearDate) {
		this.modelYearDate = modelYearDate;
	}
	public float getProdSchQty() {
		return prodSchQty;
	}
	public void setProdSchQty(float prodSchQty) {
		this.prodSchQty = prodSchQty;
	}
	public String getProdAsmLineNo() {
		return StringUtils.trimToEmpty(prodAsmLineNo);
	}
	public void setProdAsmLineNo(String asmLineNo) {
		this.prodAsmLineNo = asmLineNo;
	}
	public String getVehicleModelCode() {
		return StringUtils.trimToEmpty(vehicleModelCode);
	}
	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + Float.floatToIntBits(modelYearDate);
		result = prime * result + ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result + Float.floatToIntBits(prodSchQty);
		result = prime * result + ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PddaPlatformDto other = (PddaPlatformDto) obj;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (Float.floatToIntBits(modelYearDate) != Float.floatToIntBits(other.modelYearDate))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null)
				return false;
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo))
			return false;
		if (Float.floatToIntBits(prodSchQty) != Float.floatToIntBits(other.prodSchQty))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}
	
}
