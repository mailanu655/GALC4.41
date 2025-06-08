package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 1.0 
* @author Vivek Bettada 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL313TBX")
public class PurchaseContract extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCHASE_CONTRACT_ID") @Id
	private long purchaseContractId;
	
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

	@Column(name = "SUBS_CODE")
	private String subsCode;

	@Column(name = "SHIPPER")
	private String shipper;

	@Column(name = "INVOICE_SEQUENCE_NUMBER")
	private int invoiceSequenceNumber;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "CURRENCY_SETTLEMENT")
	private String currencySettlement;

	@Column(name = "SHIP_UNIT")
	private int shipUnit;

	@Column(name = "ORDER_UNIT")
	private int orderUnit;

	@Column(name = "DELIVERY_FORM_CODE")
	private String deliveryFormCode;

	@Column(name = "PRODUCT_GROUP_CODE")
	private String productGroupCode;

	@Column(name = "RECEIVE_DATE")
	private Date receiveDate;

	@Column(name = "SHIP_DATE")
	private Date shipDate;
	
	@Column(name = "ORDER_DUE_DATE")
	private Date orderDueDate;
	
	public PurchaseContract() {
		
	}

	/**
	 * @return the purchaseContractId
	 */
	public Long getId() {
		return purchaseContractId;
	}

	/**
	 * @return the purchaseContractId
	 */
	public long getPurchaseContractId() {
		return purchaseContractId;
	}

	/**
	 * @param purchaseContractId the purchaseContractId to set
	 */
	public void setPurchaseContractId(long purchaseContractId) {
		this.purchaseContractId = purchaseContractId;
	}

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

	public String getSubsCode()  {
			return StringUtils.trim(subsCode);
	}

	public void setSubsCode(String newSubsCode)  {
		subsCode=newSubsCode;
	}

	public String getShipper()  {
		return StringUtils.trim(shipper);
	}

	public void setShipper(String newShipper)  {
		shipper=newShipper;
	}

	public int getInvoiceSequenceNumber()  {
		return invoiceSequenceNumber;
	}

	public void setInvoiceSequenceNumber(int newInvoiceSequenceNumber)  {
		invoiceSequenceNumber=newInvoiceSequenceNumber;
	}


	public String getCurrency()  {
		return StringUtils.trim(currency);
	}

	public void setCurrency(String newCurrency)  {
		currency=newCurrency;
	}

	public String getCurrencySettlement()  {
		return StringUtils.trim(currencySettlement);
	}

	public void setCurrencySettlement(String newCurrencySettlement)  {
		currencySettlement=newCurrencySettlement;
	}

	public int getOrderUnit()  {
		return orderUnit;
	}

	public void setOrderUnit(int newOrderUnit)  {
		orderUnit=newOrderUnit;
	}

	public int getShipUnit()  {
		return shipUnit;
	}

	public void setShipUnit(int newShipUnit)  {
		shipUnit=newShipUnit;
	}

	public String getProductGroupCode()  {
		return StringUtils.trim(productGroupCode);
	}

	public void setProductGroupCode(String newProductGroupCode)  {
		productGroupCode=newProductGroupCode;
	}

	public String getDeliveryFormCode()  {
		return StringUtils.trim(deliveryFormCode);
	}

	public void setDeliveryFormCode(String newDeliveryFormCode)  {
		deliveryFormCode=newDeliveryFormCode;
	}

	public Date getReceiveDate()  {
		return receiveDate;
	}

	public void setReceiveDate(Date newReceiveDate)  {
		receiveDate=newReceiveDate;
	}

	public Date getShipDate()  {
		return shipDate;
	}

	public void setShipDate(Date newShipDate)  {
		shipDate=newShipDate;
	}
	
	public Date getOrderDueDate() {
		return orderDueDate;
	}

	public void setOrderDueDate(Date orderDueDate) {
		this.orderDueDate = orderDueDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((currencySettlement == null) ? 0 : currencySettlement.hashCode());
		result = prime * result + ((deliveryFormCode == null) ? 0 : deliveryFormCode.hashCode());
		result = prime * result + invoiceSequenceNumber;
		result = prime * result + orderUnit;
		result = prime * result + ((productGroupCode == null) ? 0 : productGroupCode.hashCode());
		result = prime * result + (int) (purchaseContractId ^ (purchaseContractId >>> 32));
		result = prime * result + ((purchaseContractNumber == null) ? 0 : purchaseContractNumber.hashCode());
		result = prime * result + ((receiveDate == null) ? 0 : receiveDate.hashCode());
		result = prime * result + ((salesExtColorCode == null) ? 0 : salesExtColorCode.hashCode());
		result = prime * result + ((salesIntColorCode == null) ? 0 : salesIntColorCode.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((shipDate == null) ? 0 : shipDate.hashCode());
		result = prime * result + shipUnit;
		result = prime * result + ((shipper == null) ? 0 : shipper.hashCode());
		result = prime * result + ((subsCode == null) ? 0 : subsCode.hashCode());
		result = prime * result + ((orderDueDate == null) ? 0 : orderDueDate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseContract other = (PurchaseContract) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (currencySettlement == null) {
			if (other.currencySettlement != null)
				return false;
		} else if (!currencySettlement.equals(other.currencySettlement))
			return false;
		if (deliveryFormCode == null) {
			if (other.deliveryFormCode != null)
				return false;
		} else if (!deliveryFormCode.equals(other.deliveryFormCode))
			return false;
		if (invoiceSequenceNumber != other.invoiceSequenceNumber)
			return false;
		if (orderUnit != other.orderUnit)
			return false;
		if (productGroupCode == null) {
			if (other.productGroupCode != null)
				return false;
		} else if (!productGroupCode.equals(other.productGroupCode))
			return false;
		if (purchaseContractId != other.purchaseContractId)
			return false;
		if (purchaseContractNumber == null) {
			if (other.purchaseContractNumber != null)
				return false;
		} else if (!purchaseContractNumber.equals(other.purchaseContractNumber))
			return false;
		if (receiveDate == null) {
			if (other.receiveDate != null)
				return false;
		} else if (!receiveDate.equals(other.receiveDate))
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
		if (shipDate == null) {
			if (other.shipDate != null)
				return false;
		} else if (!shipDate.equals(other.shipDate))
			return false;
		if (shipUnit != other.shipUnit)
			return false;
		if (shipper == null) {
			if (other.shipper != null)
				return false;
		} else if (!shipper.equals(other.shipper))
			return false;
		if (subsCode == null) {
			if (other.subsCode != null)
				return false;
		} else if (!subsCode.equals(other.subsCode))
			return false;
		if (orderDueDate == null) {
			if (other.orderDueDate != null)
				return false;
		} else if (!orderDueDate.equals(other.orderDueDate))
			return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseContract [purchaseContractId=" + purchaseContractId + ", purchaseContractNumber="
				+ purchaseContractNumber + ", salesModelCode=" + salesModelCode + ", salesModelTypeCode="
				+ salesModelTypeCode + ", salesModelOptionCode=" + salesModelOptionCode + ", salesExtColorCode="
				+ salesExtColorCode + ", salesIntColorCode=" + salesIntColorCode + ", subsCode=" + subsCode
				+ ", shipper=" + shipper + ", invoiceSequenceNumber=" + invoiceSequenceNumber + ", currency="
				+ currency + ", currencySettlement=" + currencySettlement + ", shipUnit=" + shipUnit + ", orderUnit="
				+ orderUnit + ", deliveryFormCode=" + deliveryFormCode + ", productGroupCode=" + productGroupCode
				+ ", receiveDate=" + receiveDate + ", shipDate=" + shipDate + ", orderDueDate=" + orderDueDate +"]";
	}

}
