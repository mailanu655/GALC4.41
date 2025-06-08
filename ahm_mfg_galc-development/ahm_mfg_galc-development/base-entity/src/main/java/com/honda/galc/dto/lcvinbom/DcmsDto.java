package com.honda.galc.dto.lcvinbom;

import java.util.Date;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class DcmsDto implements IDto {
	private static final long serialVersionUID = 1L;

	private String designChangeNumber;
	private String designChangeClass;
	private String serviceInterchangeableCode;
	private String stragglerInterchangeableCode;
	private Date effectiveBeginDate;
	private Date effectiveEndDate;
	private String ymtoCode;
	private String modelCode;
	private String modelType;
	private String designChangePartNumber;
	private String letSystemName;
	private String startingProductionLot;
	
	public DcmsDto() {
	}
	
	public String getDesignChangeNumber() {
		return designChangeNumber;
	}

	public void setDesignChangeNumber(String designChangeNumber) {
		this.designChangeNumber = designChangeNumber;
	}

	public String getDesignChangeClass() {
		return designChangeClass;
	}

	public void setDesignChangeClass(String designChangeClass) {
		this.designChangeClass = designChangeClass;
	}

	public String getServiceInterchangeableCode() {
		return StringUtils.isBlank(serviceInterchangeableCode)?"-":serviceInterchangeableCode;
	}

	public void setServiceInterchangeableCode(String serviceInterchangeableCode) {
		this.serviceInterchangeableCode = serviceInterchangeableCode;
	}

	public String getStragglerInterchangeableCode() {
		return stragglerInterchangeableCode;
	}

	public void setStragglerInterchangeableCode(String stragglerInterchangeableCode) {
		this.stragglerInterchangeableCode = stragglerInterchangeableCode;
	}

	public Date getEffectiveBeginDate() {
		return effectiveBeginDate;
	}

	public void setEffectiveBeginDate(Date effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getYmtoCode() {
		return ymtoCode;
	}

	public void setYmtoCode(String ymtoCode) {
		this.ymtoCode = ymtoCode;
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

	public String getDesignChangePartNumber() {
		return StringUtils.substring(designChangePartNumber,0,15);
	}

	public void setDesignChangePartNumber(String designChangePartNumber) {
		this.designChangePartNumber = designChangePartNumber;
	}

	public String getLetSystemName() {
		return letSystemName;
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getStartingProductionLot() {
		return startingProductionLot;
	}

	public void setStartingProductionLot(String startingProductionLot) {
		this.startingProductionLot = startingProductionLot;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ymtoCode == null) ? 0 : ymtoCode.hashCode());
		result = prime * result + ((designChangeClass == null) ? 0 : designChangeClass.hashCode());
		result = prime * result + ((designChangeNumber == null) ? 0 : designChangeNumber.hashCode());
		result = prime * result + ((designChangePartNumber == null) ? 0 : designChangePartNumber.hashCode());
		result = prime * result + ((effectiveBeginDate == null) ? 0 : effectiveBeginDate.hashCode());
		result = prime * result + ((effectiveEndDate == null) ? 0 : effectiveEndDate.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((serviceInterchangeableCode == null) ? 0 : serviceInterchangeableCode.hashCode());
		result = prime * result + ((startingProductionLot == null) ? 0 : startingProductionLot.hashCode());
		result = prime * result
				+ ((stragglerInterchangeableCode == null) ? 0 : stragglerInterchangeableCode.hashCode());
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
		DcmsDto other = (DcmsDto) obj;
		if (ymtoCode == null) {
			if (other.ymtoCode != null)
				return false;
		} else if (!ymtoCode.equals(other.ymtoCode))
			return false;
		if (designChangeClass == null) {
			if (other.designChangeClass != null)
				return false;
		} else if (!designChangeClass.equals(other.designChangeClass))
			return false;
		if (designChangeNumber == null) {
			if (other.designChangeNumber != null)
				return false;
		} else if (!designChangeNumber.equals(other.designChangeNumber))
			return false;
		if (designChangePartNumber == null) {
			if (other.designChangePartNumber != null)
				return false;
		} else if (!designChangePartNumber.equals(other.designChangePartNumber))
			return false;
		if (effectiveBeginDate == null) {
			if (other.effectiveBeginDate != null)
				return false;
		} else if (!effectiveBeginDate.equals(other.effectiveBeginDate))
			return false;
		if (effectiveEndDate == null) {
			if (other.effectiveEndDate != null)
				return false;
		} else if (!effectiveEndDate.equals(other.effectiveEndDate))
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelType == null) {
			if (other.modelType != null)
				return false;
		} else if (!modelType.equals(other.modelType))
			return false;
		if (serviceInterchangeableCode == null) {
			if (other.serviceInterchangeableCode != null)
				return false;
		} else if (!serviceInterchangeableCode.equals(other.serviceInterchangeableCode))
			return false;
		if (startingProductionLot == null) {
			if (other.startingProductionLot != null)
				return false;
		} else if (!startingProductionLot.equals(other.startingProductionLot))
			return false;
		if (stragglerInterchangeableCode == null) {
			if (other.stragglerInterchangeableCode != null)
				return false;
		} else if (!stragglerInterchangeableCode.equals(other.stragglerInterchangeableCode))
			return false;
		return true;
	}

	public String toString() {
	    return StringUtil.toString(getClass().getSimpleName(), 
	          getDesignChangeNumber(), getDesignChangeClass(), 
	          getServiceInterchangeableCode(), 
	          getStragglerInterchangeableCode(), 
	          getEffectiveBeginDate(), getEffectiveEndDate(), 
	          getYmtoCode(),
	          getModelCode(), getModelType(), 
	          getDesignChangePartNumber(), getLetSystemName(), 
	          getStartingProductionLot());
	}
	
}
