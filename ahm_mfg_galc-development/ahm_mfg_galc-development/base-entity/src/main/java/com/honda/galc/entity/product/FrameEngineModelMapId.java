package com.honda.galc.entity.product;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

@Embeddable
public class FrameEngineModelMapId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="FRM_MODEL_YEAR_CODE")
	private String frmModelYearCode;
	
	@Column(name="FRM_MODEL_CODE")
	private String frmModelCode;
	
	@Column(name="FRM_MODEL_TYPE_CODE")
	private String frmModelTypeCode;
	
	@Column(name="FRM_MODEL_OPTION_CODE")
	private String frmModelOptionCode;
	
	@Column(name="ENG_MODEL_YEAR_CODE")
	private String engModelYearCode;
	
	@Column(name="ENG_MODEL_CODE")
	private String engModelCode;
	
	@Column(name="ENG_MODEL_TYPE_CODE")
	private String engModelTypeCode;
	
	@Column(name="ENG_MODEL_OPTION_CODE")
	private String engModelOptionCode;
	
	public FrameEngineModelMapId() {
		super();
	}
	
	public FrameEngineModelMapId(
			String frmModelYearCode,
			String frmModelCode,
			String frmModelTypeCode,
			String frmModelOptionCode,
			String engModelYearCode,
			String engModelCode,
			String engModelTypeCode,
			String engModelOptionCode) {
		this.setFrmModelYearCode(frmModelYearCode);
		this.setFrmModelCode(frmModelCode);
		this.setFrmModelTypeCode(frmModelTypeCode);
		this.setFrmModelOptionCode(frmModelOptionCode);
		this.setEngModelYearCode(engModelYearCode);
		this.setEngModelCode(engModelCode);
		this.setEngModelTypeCode(engModelTypeCode);
		this.setEngModelOptionCode(engModelOptionCode);
	}
	
	public void setFrmModelYearCode(String frmModelYearCode) {
		this.frmModelYearCode = frmModelYearCode.trim();
	}
	
	public String getFrmModelYearCode() {
		return StringUtils.trim(this.frmModelYearCode);
	}
	
	public void setFrmModelCode(String frmModelCode) {
		this.frmModelCode = frmModelCode;
	}
	
	public String getFrmModelCode() {
		return StringUtils.trim(StringUtils.trim(this.frmModelCode));
	}
	
	public void setFrmModelTypeCode(String frmModelTypeCode) {
		this.frmModelTypeCode = frmModelTypeCode;
	}
	
	public String getFrmModelTypeCode() {
		return StringUtils.trim(this.frmModelTypeCode);
	}
	
	public void setFrmModelOptionCode(String frmModelOptionCode) {
		this.frmModelOptionCode = frmModelOptionCode;
	}
	
	public String getFrmModelOptionCode() {
		return StringUtils.trim(this.frmModelOptionCode);
	}
	
	public void setEngModelYearCode(String engModelYearCode) {
		this.engModelYearCode = engModelYearCode;
	}
	
	public String getEngModelYearCode() {
		return StringUtils.trim(this.engModelYearCode);
	}
	
	public void setEngModelCode(String engModelCode) {
		this.engModelCode = engModelCode;
	}
	
	public String getEngModelCode() {
		return StringUtils.trim(this.engModelCode);
	}
	
	public void setEngModelTypeCode(String engModelTypeCode) {
		this.engModelTypeCode = engModelTypeCode;
	}
	
	public String getEngModelTypeCode() {
		return StringUtils.trim(this.engModelTypeCode);
	}
	
	public void setEngModelOptionCode(String engModelOptionCode) {
		this.engModelOptionCode = engModelOptionCode;
	}
	
	public String getEngModelOptionCode() {
		return StringUtils.trim(this.engModelOptionCode);
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FrameEngineModelMapId)) {
			return false;
		}
		FrameEngineModelMapId castOther = (FrameEngineModelMapId)other;
		return 
			this.getFrmModelYearCode().equals(castOther.getFrmModelYearCode())
			&& this.getFrmModelCode().equals(castOther.getFrmModelCode())
			&& this.getFrmModelTypeCode().equals(castOther.getFrmModelTypeCode())
			&& this.getFrmModelOptionCode().equals(castOther.getFrmModelOptionCode())
			&& this.getEngModelYearCode().equals(castOther.getEngModelYearCode())
			&& this.getEngModelCode().equals(castOther.getEngModelCode())
			&& this.getEngModelTypeCode().equals(castOther.getEngModelTypeCode())
			&& this.getEngModelOptionCode().equals(castOther.getEngModelOptionCode());
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
        hash = hash * prime + this.frmModelYearCode.hashCode();
        hash = hash * prime + this.frmModelCode.hashCode();
        hash = hash * prime + this.frmModelTypeCode.hashCode();
        hash = hash * prime + this.frmModelOptionCode.hashCode();
        hash = hash * prime + this.engModelYearCode.hashCode();
        hash = hash * prime + this.engModelCode.hashCode();
        hash = hash * prime + this.engModelTypeCode.hashCode();
        hash = hash * prime + this.engModelOptionCode.hashCode();
		return hash;
	}
	
	@Override
	public String toString(){
		return StringUtil.toString(
			this.getClass().getSimpleName(), 
			getFrmModelYearCode(), 
			getFrmModelCode(), 
			getFrmModelTypeCode(), 
			getFrmModelOptionCode(), 
			getEngModelYearCode(), 
			getEngModelCode(),
			getEngModelTypeCode(),
			getEngModelOptionCode()
		);
	}
}
