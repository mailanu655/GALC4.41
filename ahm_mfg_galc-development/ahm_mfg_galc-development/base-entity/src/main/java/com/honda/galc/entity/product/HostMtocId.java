package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>HostMtocId Class description</h3>
 * <p> HostMtocId description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 1, 2010
 *
 *
 */
@Embeddable
public class HostMtocId implements Serializable {
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

	private static final long serialVersionUID = 1L;

	public HostMtocId() {
		super();
	}

	public String getPlantCodeFrame() {
		return this.plantCodeFrame;
	}

	public void setPlantCodeFrame(String plantCodeFrame) {
		this.plantCodeFrame = plantCodeFrame;
	}

	public String getModelYearCode() {
		return this.modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return this.modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return this.modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getExtColorCode() {
		return this.extColorCode;
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public String getIntColorCode() {
		return this.intColorCode;
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}
	
	public String getProductSpecCode() {
		return modelYearCode + modelCode + modelTypeCode + modelOptionCode + extColorCode + intColorCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof HostMtocId)) {
			return false;
		}
		HostMtocId other = (HostMtocId) o;
		return this.plantCodeFrame.equals(other.plantCodeFrame)
			&& this.modelYearCode.equals(other.modelYearCode)
			&& this.modelCode.equals(other.modelCode)
			&& this.modelTypeCode.equals(other.modelTypeCode)
			&& this.modelOptionCode.equals(other.modelOptionCode)
			&& this.extColorCode.equals(other.extColorCode)
			&& this.intColorCode.equals(other.intColorCode);
	}

	@Override
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
	public String toString() {
		return "HostMtocId [plantCodeFrame=" + plantCodeFrame + ", modelYearCode=" + modelYearCode + ", modelCode="
				+ modelCode + ", modelTypeCode=" + modelTypeCode + ", modelOptionCode=" + modelOptionCode
				+ ", extColorCode=" + extColorCode + ", intColorCode=" + intColorCode + "]";
	}
	
	
}
