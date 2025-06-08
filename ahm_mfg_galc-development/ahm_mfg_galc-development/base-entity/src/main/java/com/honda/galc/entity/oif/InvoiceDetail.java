package com.honda.galc.entity.oif;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.EquipmentGroup;
import com.honda.galc.entity.product.InstalledPartId;

@Entity
@Table(name = "INVOICE_DETAIL_TBX")
public class InvoiceDetail extends AuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "INSPECTION_TIMESTAMP")
	private Timestamp inspectionTimestamp;

	@Column(name = "SHIPPING_TIMESTAMP")
	private Timestamp shippingTimestamp;

	@Column(name = "PRICE")
	private double price;

	@Column(name = "QUOTATION_NO")
	private String quotationNo;

	@Column(name = "PRICE_TYPE")
	private String priceType;

	@Column(name = "AHM_INVOICE_NO")
	private String ahmInvoiceNo;

	@Column(name = "CORRECTION_FLAG")
	private String correctionFlag;

	@Column(name = "RECEIPT_TIMESTAMP")
	private String receiptTimestamp;

	@Column(name = "VALIDATION_DATE")
	private String validationDate;

	@Column(name = "FINAL_DATE")
	private String finalDate;

	@Column(name = "FINAL_RECEIPT_FLG")
	private String finalReceiptFlg;

	@Column(name = "FACTORY_SHIPMENT_NO")
	private String factoryShipmentNo;

	@Column(name = "ERROR_CODE")
	private String errorCode;

	@Column(name = "DTL_ROW_NO")
	private int dtlRowNo;

	@ManyToOne
	@JoinColumn(name="INVOICE_NO" ,updatable = false,insertable=false)
    private Invoice invoice;


    @EmbeddedId
    private InvoiceDetailId id;

	public InvoiceDetailId getId() {
		return id;
	}

	public void setId(InvoiceDetailId id) {
		this.id = id;
	}
	
	public Timestamp getInspectionTimestamp()  {
		return inspectionTimestamp;
	}

	public void setInspectionTimestamp(Timestamp newInspectionDate)  {
		inspectionTimestamp=newInspectionDate;
	}

	public Timestamp getShippingTimestamp()  {
		return shippingTimestamp;
	}

	public void setShippingTimestamp(Timestamp newShippingDate)  {
		shippingTimestamp=newShippingDate;
	}

	public String getQuotationNo()  {
		return quotationNo;
	}

	public void setQuotationNo(String newQuotationNo)  {
		quotationNo=newQuotationNo;
	}

	public String getPriceType()  {
		return priceType;
	}

	public void setPriceType(String newPriceType)  {
		priceType=newPriceType;
	}

	public String getAhmInvoiceNo()  {
		return ahmInvoiceNo;
	}

	public void setAhmInvoiceNo(String newAhmInvoiceNo)  {
		ahmInvoiceNo=newAhmInvoiceNo;
	}

	public String getCorrectionFlag()  {
		return correctionFlag;
	}

	public void setCorrectionFlag(String newCorrectionFlag)  {
		correctionFlag=newCorrectionFlag;
	}

	/**
	 * @return the receiptTimestamp
	 */
	public String getReceiptTimestamp() {
		return receiptTimestamp;
	}

	/**
	 * @param receiptTimestamp the receiptTimestamp to set
	 */
	public void setReceiptTimestamp(String receiptTimestamp) {
		this.receiptTimestamp = receiptTimestamp;
	}

	/**
	 * @return the validationDate
	 */
	public String getValidationDate() {
		return validationDate;
	}

	/**
	 * @param validationDate the validationDate to set
	 */
	public void setValidationDate(String validationDate) {
		this.validationDate = validationDate;
	}

	/**
	 * @return the finalDate
	 */
	public String getFinalDate() {
		return finalDate;
	}

	/**
	 * @param finalDate the finalDate to set
	 */
	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	/**
	 * @return the finalReceiptFlg
	 */
	public String getFinalReceiptFlg() {
		return finalReceiptFlg;
	}

	/**
	 * @param finalReceiptFlg the finalReceiptFlg to set
	 */
	public void setFinalReceiptFlg(String finalReceiptFlg) {
		this.finalReceiptFlg = finalReceiptFlg;
	}

	public String getFactoryShipmentNo()  {
		return factoryShipmentNo;
	}

	public void setFactoryShipmentNo(String newFactoryShipmentNo)  {
		factoryShipmentNo=newFactoryShipmentNo;
	}

	public String getErrorCode()  {
		return errorCode;
	}

	public void setErrorCode(String newErrorCode)  {
		errorCode=newErrorCode;
	}

	public double getPrice()  {
		return price;
	}

	public void setPrice(double newPrice)  {
		price=newPrice;
	}

	public int getDtlRowNo()  {
		return dtlRowNo;
	}

	public void setDtlRowNo(int newDtlRowNo)  {
		dtlRowNo=newDtlRowNo;
	}
	
	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ahmInvoiceNo == null) ? 0 : ahmInvoiceNo.hashCode());
		result = prime * result + ((correctionFlag == null) ? 0 : correctionFlag.hashCode());
		result = prime * result + dtlRowNo;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((factoryShipmentNo == null) ? 0 : factoryShipmentNo.hashCode());
		result = prime * result + ((finalDate == null) ? 0 : finalDate.hashCode());
		result = prime * result + ((finalReceiptFlg == null) ? 0 : finalReceiptFlg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inspectionTimestamp == null) ? 0 : inspectionTimestamp.hashCode());
		result = prime * result + ((invoice == null) ? 0 : invoice.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((priceType == null) ? 0 : priceType.hashCode());
		result = prime * result + ((quotationNo == null) ? 0 : quotationNo.hashCode());
		result = prime * result + ((receiptTimestamp == null) ? 0 : receiptTimestamp.hashCode());
		result = prime * result + ((shippingTimestamp == null) ? 0 : shippingTimestamp.hashCode());
		result = prime * result + ((validationDate == null) ? 0 : validationDate.hashCode());
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
		InvoiceDetail other = (InvoiceDetail) obj;
		if (ahmInvoiceNo == null) {
			if (other.ahmInvoiceNo != null)
				return false;
		} else if (!ahmInvoiceNo.equals(other.ahmInvoiceNo))
			return false;
		if (correctionFlag == null) {
			if (other.correctionFlag != null)
				return false;
		} else if (!correctionFlag.equals(other.correctionFlag))
			return false;
		if (dtlRowNo != other.dtlRowNo)
			return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (factoryShipmentNo == null) {
			if (other.factoryShipmentNo != null)
				return false;
		} else if (!factoryShipmentNo.equals(other.factoryShipmentNo))
			return false;
		if (finalDate == null) {
			if (other.finalDate != null)
				return false;
		} else if (!finalDate.equals(other.finalDate))
			return false;
		if (finalReceiptFlg == null) {
			if (other.finalReceiptFlg != null)
				return false;
		} else if (!finalReceiptFlg.equals(other.finalReceiptFlg))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inspectionTimestamp == null) {
			if (other.inspectionTimestamp != null)
				return false;
		} else if (!inspectionTimestamp.equals(other.inspectionTimestamp))
			return false;
		if (invoice == null) {
			if (other.invoice != null)
				return false;
		} else if (!invoice.equals(other.invoice))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (priceType == null) {
			if (other.priceType != null)
				return false;
		} else if (!priceType.equals(other.priceType))
			return false;
		if (quotationNo == null) {
			if (other.quotationNo != null)
				return false;
		} else if (!quotationNo.equals(other.quotationNo))
			return false;
		if (receiptTimestamp == null) {
			if (other.receiptTimestamp != null)
				return false;
		} else if (!receiptTimestamp.equals(other.receiptTimestamp))
			return false;
		if (shippingTimestamp == null) {
			if (other.shippingTimestamp != null)
				return false;
		} else if (!shippingTimestamp.equals(other.shippingTimestamp))
			return false;
		if (validationDate == null) {
			if (other.validationDate != null)
				return false;
		} else if (!validationDate.equals(other.validationDate))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceDetail [inspectionTimestamp=" + inspectionTimestamp + ", shippingTimestamp=" + shippingTimestamp
				+ ", price=" + price + ", quotationNo=" + quotationNo + ", priceType=" + priceType + ", ahmInvoiceNo="
				+ ahmInvoiceNo + ", correctionFlag=" + correctionFlag + ", receiptTimestamp=" + receiptTimestamp
				+ ", validationDate=" + validationDate + ", finalDate=" + finalDate + ", finalReceiptFlg="
				+ finalReceiptFlg + ", factoryShipmentNo=" + factoryShipmentNo + ", errorCode=" + errorCode
				+ ", dtlRowNo=" + dtlRowNo + ", invoice=" + invoice + ", id=" + id + "]";
	}	

}
