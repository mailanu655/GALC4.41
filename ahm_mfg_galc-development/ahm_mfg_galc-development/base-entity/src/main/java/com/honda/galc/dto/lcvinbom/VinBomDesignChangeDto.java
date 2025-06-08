package com.honda.galc.dto.lcvinbom;

import java.util.Date;

import com.honda.galc.dto.IDto;

public class VinBomDesignChangeDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	
	private String designChangeNumber;
	private String designChangePartNumber;
	private Date effectiveBeginDate;
	private String ymtoCode;
	private String modelCode;
	private String modelType;
	
	
	public String getDesignChangeNumber() {
		return designChangeNumber;
	}
	public void setDesignChangeNumber(String designChangeNumber) {
		this.designChangeNumber = designChangeNumber;
	}
	public String getDesignChangePartNumber() {
		return designChangePartNumber;
	}
	public void setDesignChangePartNumber(String designChangePartNumber) {
		this.designChangePartNumber = designChangePartNumber;
	}
	public Date getEffectiveBeginDate() {
		return effectiveBeginDate;
	}
	public void setEffectiveBeginDate(Date effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	
	@Override
	public String toString() {
		return "VinBomDesignChangeDto [dcNumber=" + designChangeNumber + ", dcPartNumber=" + designChangePartNumber
				+ ", effectiveBeginDate=" + effectiveBeginDate + ", modelCode=" + modelCode + ", modelTypeCode="
				+ modelType + "]";
	}
	public String getYmtoCode() {
		return ymtoCode;
	}
	public void setYmtoCode(String ymtoCode) {
		this.ymtoCode = ymtoCode;
	}
	
	
	
}
