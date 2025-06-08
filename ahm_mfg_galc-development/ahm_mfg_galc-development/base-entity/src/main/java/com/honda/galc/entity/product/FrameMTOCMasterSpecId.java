package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the GAL158TBX database table.
 * 
 */
@Embeddable
public class FrameMTOCMasterSpecId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PLANT_CODE_FRAME")
	private String plantCodeFrame;

	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MODEL_TYPE_CODE")
	private String modelTypeCode;

	@Column(name="MODEL_OPTION_CODE")
	private String modelOptionCode;

	@Column(name="EXT_COLOR_CODE")
	private String extColorCode;

	@Column(name="INT_COLOR_CODE")
	private String intColorCode;

	public FrameMTOCMasterSpecId() {
	}
	public String getPlantCodeFrame() {
		return StringUtils.trim(this.plantCodeFrame);
	}
	public void setPlantCodeFrame(String plantCodeFrame) {
		this.plantCodeFrame = plantCodeFrame;
	}
	public String getModelYearCode() {
		return StringUtils.trim(this.modelYearCode);
	}
	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}
	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelTypeCode() {
		return StringUtils.stripEnd(this.modelTypeCode, null);
	}
	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}
	public String getModelOptionCode() {
		return StringUtils.trim(this.modelOptionCode);
	}
	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}
	public String getExtColorCode() {
		return StringUtils.trim(this.extColorCode);
	}
	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}
	public String getIntColorCode() {
		return StringUtils.trim(this.intColorCode);
	}
	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FrameMTOCMasterSpecId)) {
			return false;
		}
		FrameMTOCMasterSpecId castOther = (FrameMTOCMasterSpecId)other;
		return 
			this.plantCodeFrame.equals(castOther.plantCodeFrame)
			&& this.modelYearCode.equals(castOther.modelYearCode)
			&& this.modelCode.equals(castOther.modelCode)
			&& this.modelTypeCode.equals(castOther.modelTypeCode)
			&& this.modelOptionCode.equals(castOther.modelOptionCode)
			&& this.extColorCode.equals(castOther.extColorCode)
			&& this.intColorCode.equals(castOther.intColorCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.plantCodeFrame.hashCode();
		hash = hash * prime + this.modelYearCode.hashCode();
		hash = hash * prime + this.modelCode.hashCode();
		hash = hash * prime + this.modelTypeCode.hashCode();
		hash = hash * prime + this.modelOptionCode.hashCode();
		hash = hash * prime + this.extColorCode.hashCode();
		hash = hash * prime + this.intColorCode.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString(){
		return StringUtil.toString(
				this.getClass().getSimpleName(), 
				getPlantCodeFrame(), 
				getModelYearCode(), 
				getModelCode(), 
				getModelTypeCode(), 
				getModelOptionCode(), 
				getExtColorCode(),
				getIntColorCode()
				);
	}
}