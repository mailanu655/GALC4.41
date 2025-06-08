package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@Embeddable
public class LetLogId implements Serializable {

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TEST_SEQ")
	private int testSeq;
	
	@Column(name="ECU_NAME")
	private String ecuName;
	
	private static final long serialVersionUID = 1L;

	public LetLogId() {
		super();
	}


	public LetLogId(String productId, int testSeq, int historySeq, String ecuName) {
		super();
		this.productId = productId;
		this.testSeq = testSeq;
		this.ecuName = ecuName;
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



	public String getEcuName() {
		return StringUtils.trimToEmpty(ecuName);
	}


	public void setEcuName(String ecuName) {
		this.ecuName = ecuName;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ecuName == null) ? 0 : ecuName.hashCode());
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
		LetLogId other = (LetLogId) obj;
		if (ecuName == null) {
			if (other.ecuName != null)
				return false;
		} else if (!ecuName.equals(other.ecuName))
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



}
