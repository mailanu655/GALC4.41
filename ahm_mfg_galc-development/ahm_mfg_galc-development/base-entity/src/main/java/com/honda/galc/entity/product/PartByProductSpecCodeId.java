package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.UserAuditEntry;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class PartByProductSpecCodeId implements Ymtoc, Serializable {
	@Column(name="PART_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String partId;

	@Column(name="PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String partName;

	@Column(name="MODEL_YEAR_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
	private String modelYearCode ="";

	@Column(name="MODEL_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=4)
	private String modelCode ="";

	@Column(name="MODEL_TYPE_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=5)
	private String modelTypeCode ="";

	@Column(name="MODEL_OPTION_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=6)
	private String modelOptionCode ="";

	@Column(name="EXT_COLOR_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=7)
	private String extColorCode ="";

	@Column(name="INT_COLOR_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=8)
	private String intColorCode ="";
	
	@Column(name="PRODUCT_SPEC_CODE")
	@Auditable(isPartOfPrimaryKey= true,sequence=9)
	private String productSpecCode = "";

	private static final long serialVersionUID = 1L;

	public PartByProductSpecCodeId() {
		super();
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
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
	
	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	 public String getYMTO() {
		if(!StringUtils.isEmpty(getProductSpecCode()))
			return getProductSpecCode();
		else
	    	return getModelYearCode()+" " +
	    	       getModelCode()+ " " +
	    	       getModelTypeCode()+ " " +
	    	       getModelOptionCode()+ " " +
	    	       getExtColorCode()+ " " + 
	    	       getIntColorCode();
	}
	 
	public void setYmtoc(Ymtoc ymtoc) {
		this.modelYearCode = ymtoc.getModelYearCode();
		this.modelCode = ymtoc.getModelCode();
		this.modelTypeCode = ymtoc.getModelTypeCode();
		this.modelOptionCode = ymtoc.getModelOptionCode();
		this.extColorCode = ymtoc.getExtColorCode();
		this.intColorCode = ymtoc.getIntColorCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof PartByProductSpecCodeId)) {
			return false;
		}
		PartByProductSpecCodeId other = (PartByProductSpecCodeId) o;
		return this.partId.equals(other.partId)
			&& this.partName.equals(other.partName)
			&& this.modelYearCode.equals(other.modelYearCode)
			&& this.modelCode.equals(other.modelCode)
			&& this.modelTypeCode.equals(other.modelTypeCode)
			&& this.modelOptionCode.equals(other.modelOptionCode)
			&& this.extColorCode.equals(other.extColorCode)
			&& this.intColorCode.equals(other.intColorCode)
			&& this.productSpecCode.equals(other.productSpecCode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partId.hashCode();
		hash = hash * prime + this.partName.hashCode();
		hash = hash * prime + this.modelYearCode.hashCode();
		hash = hash * prime + this.modelCode.hashCode();
		hash = hash * prime + this.modelTypeCode.hashCode();
		hash = hash * prime + this.modelOptionCode.hashCode();
		hash = hash * prime + this.extColorCode.hashCode();
		hash = hash * prime + this.intColorCode.hashCode();
		hash = hash * prime + this.productSpecCode.hashCode();
		return hash;
	}

}
