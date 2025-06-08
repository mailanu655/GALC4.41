package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 25, 2013
 */
@Embeddable
public class LetPassCriteriaId implements Serializable {
	
	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode;
	
	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MODEL_TYPE_CODE")
	private String modelTypeCode;
	
	@Column(name="MODEL_OPTION_CODE")
	private String modelOptionCode;
	
	@Column(name="EFFECTIVE_TIME")
	private Timestamp effectiveTime;
			
	@Column(name="CRITERIA_PGM_ID")
	private Integer criteriaPgmId;

	private static final long serialVersionUID = 1L;

	public LetPassCriteriaId() {
		super();
	}

	
	public LetPassCriteriaId(String modelYearCode, String modelCode,
			String modelTypeCode, String modelOptionCode,
			Timestamp effectiveTime, Integer criteriaPgmId) {
		super();
		this.modelYearCode = modelYearCode;
		this.modelCode = modelCode;
		this.modelTypeCode = modelTypeCode;
		this.modelOptionCode = modelOptionCode;
		this.effectiveTime = effectiveTime;
		this.criteriaPgmId = criteriaPgmId;
	}


	public String getModelOptionCode() {
		return StringUtils.trim(modelOptionCode);
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
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
		return StringUtils.stripEnd(modelTypeCode, null);
	}


	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}


	public Timestamp getEffectiveTime() {
		return effectiveTime;
	}


	public void setEffectiveTime(Timestamp effectiveTime) {
		this.effectiveTime = effectiveTime;
	}


	public Integer getCriteriaPgmId() {
		return criteriaPgmId;
	}


	public void setCriteriaPgmId(Integer criteriaPgmId) {
		this.criteriaPgmId = criteriaPgmId;
	}


	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((criteriaPgmId == null) ? 0 : criteriaPgmId.hashCode());
		result = prime * result
				+ ((effectiveTime == null) ? 0 : effectiveTime.hashCode());
		result = prime * result
				+ ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result
				+ ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result
				+ ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result
				+ ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
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
		LetPassCriteriaId other = (LetPassCriteriaId) obj;
		if (criteriaPgmId == null) {
			if (other.criteriaPgmId != null)
				return false;
		} else if (!criteriaPgmId.equals(other.criteriaPgmId))
			return false;
		if (effectiveTime == null) {
			if (other.effectiveTime != null)
				return false;
		} else if (!effectiveTime.equals(other.effectiveTime))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
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
		return true;
	}
	

}
