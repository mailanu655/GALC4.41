package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

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
 * @author Vivek Bettada<br>
 * Feb 25, 2015
 *
 *
 */
@Embeddable
public class PurchaseContractId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "PURCHASE_CONTRACT_NUMBER")
	private String purchaseContractNumber;

	@Column(name = "SALES_MODEL_CODE")
	private String salesModelCode;

	@Column(name = "SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@Column(name = "SALES_MODEL_OPTION_CODE")
	private String salesModelOptionCode;

	@Column(name = "SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;

	@Column(name = "SALES_INT_COLOR_CODE")
	private String salesIntColorCode;

	public String getPurchaseContractNumber()  {
		return StringUtils.trim(purchaseContractNumber);
	}

	public void setPurchaseContractNumber(String newPurchaseContractNumber)  {
		purchaseContractNumber=newPurchaseContractNumber;
	}

	/**
	 * @return the salesModelCode
	 */
	public String getSalesModelCode() {
		return StringUtils.trim(salesModelCode);
	}

	/**
	 * @param salesModelCode the salesModelCode to set
	 */
	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	/**
	 * @return the salesModelTypeCode
	 */
	public String getSalesModelTypeCode() {
		return StringUtils.trim(salesModelTypeCode);
	}

	/**
	 * @param salesModelTypeCode the salesModelTypeCode to set
	 */
	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	/**
	 * @return the salesModelOptionCode
	 */
	public String getSalesModelOptionCode() {
		return StringUtils.trim(salesModelOptionCode);
	}

	/**
	 * @param salesModelOptionCode the salesModelOptionCode to set
	 */
	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	/**
	 * @return the salesExtColorCode
	 */
	public String getSalesExtColorCode() {
		return StringUtils.trim(salesExtColorCode);
	}

	/**
	 * @param salesExtColorCode the salesExtColorCode to set
	 */
	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	/**
	 * @return the salesIntColorCode
	 */
	public String getSalesIntColorCode() {
		return StringUtils.trim(salesIntColorCode);
	}

	/**
	 * @param salesIntColorCode the salesIntColorCode to set
	 */
	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((purchaseContractNumber == null) ? 0 : purchaseContractNumber.hashCode());
		result = prime * result + ((salesExtColorCode == null) ? 0 : salesExtColorCode.hashCode());
		result = prime * result + ((salesIntColorCode == null) ? 0 : salesIntColorCode.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseContractId other = (PurchaseContractId) obj;
		if (purchaseContractNumber == null) {
			if (other.purchaseContractNumber != null)
				return false;
		} else if (!purchaseContractNumber.equals(other.purchaseContractNumber))
			return false;
		if (salesExtColorCode == null) {
			if (other.salesExtColorCode != null)
				return false;
		} else if (!salesExtColorCode.equals(other.salesExtColorCode))
			return false;
		if (salesIntColorCode == null) {
			if (other.salesIntColorCode != null)
				return false;
		} else if (!salesIntColorCode.equals(other.salesIntColorCode))
			return false;
		if (salesModelCode == null) {
			if (other.salesModelCode != null)
				return false;
		} else if (!salesModelCode.equals(other.salesModelCode))
			return false;
		if (salesModelOptionCode == null) {
			if (other.salesModelOptionCode != null)
				return false;
		} else if (!salesModelOptionCode.equals(other.salesModelOptionCode))
			return false;
		if (salesModelTypeCode == null) {
			if (other.salesModelTypeCode != null)
				return false;
		} else if (!salesModelTypeCode.equals(other.salesModelTypeCode))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseContractId [purchaseContractNumber=" + purchaseContractNumber + ", salesModelCode="
				+ salesModelCode + ", salesModelTypeCode=" + salesModelTypeCode + ", salesModelOptionCode="
				+ salesModelOptionCode + ", salesExtColorCode=" + salesExtColorCode + ", salesIntColorCode="
				+ salesIntColorCode + "]";
	}

	
}
