package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

@Embeddable
public class LetProgramResultValueId implements Serializable {
	
	@Column(name="END_TIMESTAMP")
	private Timestamp endTimestamp;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TEST_SEQ")
	private int testSeq;

	@Column(name="INSPECTION_PGM_ID")
	private int inspectionPgmId;

	@Column(name="INSPECTION_PARAM_ID")
	private int inspectionParamId;

	@Column(name="INSPECTION_PARAM_TYPE")
	private String inspectionParamType;

	private static final long serialVersionUID = 1L;

	public LetProgramResultValueId() {
		super();
	}

	public Timestamp getEndTimestamp() {
		return this.endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getTestSeq() {
		return this.testSeq;
	}

	public void setTestSeq(int testSeq) {
		this.testSeq = testSeq;
	}

	public int getInspectionPgmId() {
		return this.inspectionPgmId;
	}

	public void setInspectionPgmId(int inspectionPgmId) {
		this.inspectionPgmId = inspectionPgmId;
	}

	public int getInspectionParamId() {
		return this.inspectionParamId;
	}

	public void setInspectionParamId(int inspectionParamId) {
		this.inspectionParamId = inspectionParamId;
	}

	public String getInspectionParamType() {
		return StringUtils.trim(this.inspectionParamType);
	}

	public void setInspectionParamType(String inspectionParamType) {
		this.inspectionParamType = inspectionParamType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof LetProgramResultValueId)) {
			return false;
		}
		LetProgramResultValueId other = (LetProgramResultValueId) o;
		return this.endTimestamp.equals(other.endTimestamp)
			&& this.productId.equals(other.productId)
			&& (this.testSeq == other.testSeq)
			&& (this.inspectionPgmId == other.inspectionPgmId)
			&& (this.inspectionParamId == other.inspectionParamId)
			&& this.inspectionParamType.equals(other.inspectionParamType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.endTimestamp.hashCode();
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.testSeq;
		hash = hash * prime + this.inspectionPgmId;
		hash = hash * prime + this.inspectionParamId;
		hash = hash * prime + this.inspectionParamType.hashCode();
		return hash;
	}

	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getEndTimestamp(), getProductId(), getTestSeq(), getInspectionPgmId(), 
				getInspectionParamId(), getInspectionParamType());
	}
}
