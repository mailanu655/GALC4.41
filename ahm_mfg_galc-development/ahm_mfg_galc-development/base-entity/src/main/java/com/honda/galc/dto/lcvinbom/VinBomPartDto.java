package com.honda.galc.dto.lcvinbom;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class VinBomPartDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@DtoTag(name="LET_SYSTEM_NAME")
	private String letSystemName;
	@DtoTag(name="MODEL_YEAR_CODE")
	private String modelYearCode;
	@DtoTag(name="MODEL_CODE")
	private String modelCode;
	@DtoTag(name="MODEL_TYPE_CODE")
	private String modelTypeCode;
	@DtoTag(name="BASE_PART_NUMBER")
	private String basePartNumber;
	@DtoTag(name="DC_PART_NUMBER")
	private String dcPartNumber;
	@DtoTag(name="DESCRIPTION")
	private String description;
	@DtoTag(name="EFFECTIVE_BEGIN_DATE")
	private Date effectiveBeginDate;
	@DtoTag(name="EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	@DtoTag(name="ACTIVE")
	private String active="";
	
	public VinBomPartDto() {
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getLetSystemName() {
		return letSystemName;
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getBasePartNumber() {
		return basePartNumber;
	}

	public void setBasePartNumber(String basePartNumber) {
		this.basePartNumber = basePartNumber;
	}

	public String getDcPartNumber() {
		return dcPartNumber;
	}

	public void setDcPartNumber(String dcPartNumber) {
		this.dcPartNumber = dcPartNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basePartNumber == null) ? 0 : basePartNumber.hashCode());
		result = prime * result + ((dcPartNumber == null) ? 0 : dcPartNumber.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((effectiveBeginDate == null) ? 0 : effectiveBeginDate.hashCode());
		result = prime * result + ((effectiveEndDate == null) ? 0 : effectiveEndDate.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((active == null) ? 0 : active.hashCode());;
		
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
		VinBomPartDto other = (VinBomPartDto) obj;
		if (basePartNumber == null) {
			if (other.basePartNumber != null)
				return false;
		} else if (!basePartNumber.equals(other.basePartNumber))
			return false;
		if (dcPartNumber == null) {
			if (other.dcPartNumber != null)
				return false;
		} else if (!dcPartNumber.equals(other.dcPartNumber))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), 
				getProductSpecCode(), getLetSystemName(), 
				getBasePartNumber(), getDcPartNumber(), getDescription(),
				getEffectiveBeginDate(), getEffectiveEndDate(), getActive());
	}

	@JsonIgnore
	public String getProductSpecWildCard() {
		return StringUtils.trim(getModelYearCode())+ StringUtils.trim(getModelCode()) + StringUtils.trim(getModelTypeCode());
	}
	
}
