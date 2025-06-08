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
public class LetResultHistoryId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TEST_SEQ")
	private int testSeq;
	
	@Column(name="HISTORY_SEQ")
	private int historySeq;

	private static final long serialVersionUID = 1L;

	public LetResultHistoryId() {
		super();
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

	
	public int getHistorySeq() {
		return historySeq;
	}

	public void setHistorySeq(int historySeq) {
		this.historySeq = historySeq;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + historySeq;
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
		LetResultHistoryId other = (LetResultHistoryId) obj;
		if (historySeq != other.historySeq)
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
