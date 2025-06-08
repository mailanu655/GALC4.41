package com.honda.galc.entity.product;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ModelTypeHoldId implements Serializable{

	@Column(name = "PLAN_CODE", nullable=false)
	private String planCode;
	
	@Column(name = "MODEL_YEAR_CODE", nullable=false)
	private String modelYearCode;
	
	@Column(name = "MODEL_CODE", nullable=false)
	private String modelCode;
	
	@Column(name = "MODEL_TYPE_CODE", nullable=false)
	private String modelTypeCode;

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getModelYearCode() {
		return StringUtils.trim(modelYearCode);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return StringUtils.trim(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return StringUtils.trim(modelTypeCode);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
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
		ModelTypeHoldId other = (ModelTypeHoldId) obj;
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
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ModelTypeHoldId [planCode=" + planCode + ", modelYearCode=" + modelYearCode + ", modelCode=" + modelCode
				+ ", modelTypeCode=" + modelTypeCode + "]";
	}
	
}
