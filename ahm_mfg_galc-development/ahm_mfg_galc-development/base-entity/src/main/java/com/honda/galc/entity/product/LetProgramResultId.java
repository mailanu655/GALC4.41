package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

@Embeddable
public class LetProgramResultId implements Serializable {
	@Column(name="END_TIMESTAMP")
	private Timestamp endTimestamp;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TEST_SEQ")
	private int testSeq;

	@Column(name="INSPECTION_PGM_ID")
	private int inspectionPgmId;
	
	private static final long serialVersionUID = 1L;

	public LetProgramResultId() {
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof LetProgramResultId)) {
			return false;
		}
		LetProgramResultId other = (LetProgramResultId) o;
		return this.endTimestamp.equals(other.endTimestamp)
			&& this.productId.equals(other.productId)
			&& (this.testSeq == other.testSeq)
			&& (this.inspectionPgmId == other.inspectionPgmId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.endTimestamp.hashCode();
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.testSeq;
		hash = hash * prime + this.inspectionPgmId;
		return hash;
	}
	
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getEndTimestamp(),
				getProductId(), getTestSeq(), getInspectionPgmId());
	}
}
