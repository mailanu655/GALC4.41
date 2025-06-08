package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@Embeddable
public class LetProgramValueHistoryId implements Serializable {


	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TEST_SEQ")
	private int testSeq;
	
	@Column(name="HISTORY_SEQ")
	private int historySeq;

	@Column(name="INSPECTION_PGM_ID")
	private int inspectionPgmId;

	@Column(name="INSPECTION_PARAM_ID")
	private int inspectionParamId;

	@Column(name="INSPECTION_PARAM_TYPE")
	private String inspectionParamType;
	
	private static final long serialVersionUID = 1L;

	public LetProgramValueHistoryId() {
		super();
	}

	public LetProgramValueHistoryId(Timestamp endTimestamp, String productId,
			int testSeq, int historySeq, int inspectionPgmId,
			int inspectionParamId, String inspectionParamType) {
		super();
		this.productId = productId;
		this.testSeq = testSeq;
		this.historySeq = historySeq;
		this.inspectionPgmId = inspectionPgmId;
		this.inspectionParamId = inspectionParamId;
		this.inspectionParamType = inspectionParamType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + historySeq;
		result = prime * result + inspectionParamId;
		result = prime
				* result
				+ ((inspectionParamType == null) ? 0 : inspectionParamType
						.hashCode());
		result = prime * result + inspectionPgmId;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + testSeq;
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
		LetProgramValueHistoryId other = (LetProgramValueHistoryId) obj;
		if (historySeq != other.historySeq)
			return false;
		if (inspectionParamId != other.inspectionParamId)
			return false;
		if (inspectionParamType == null) {
			if (other.inspectionParamType != null)
				return false;
		} else if (!inspectionParamType.equals(other.inspectionParamType))
			return false;
		if (inspectionPgmId != other.inspectionPgmId)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (testSeq != other.testSeq)
			return false;
		return true;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getTestSeq() {
		return testSeq;
	}

	public void setTestSeq(int testSeq) {
		this.testSeq = testSeq;
	}

	public int getHistorySeq() {
		return historySeq;
	}

	public void setHistorySeq(int historySeq) {
		this.historySeq = historySeq;
	}

	public int getInspectionPgmId() {
		return inspectionPgmId;
	}

	public void setInspectionPgmId(int inspectionPgmId) {
		this.inspectionPgmId = inspectionPgmId;
	}

	public int getInspectionParamId() {
		return inspectionParamId;
	}

	public void setInspectionParamId(int inspectionParamId) {
		this.inspectionParamId = inspectionParamId;
	}

	public String getInspectionParamType() {
		return StringUtils.trimToEmpty(inspectionParamType);
	}

	public void setInspectionParamType(String inspectionParamType) {
		this.inspectionParamType = inspectionParamType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
