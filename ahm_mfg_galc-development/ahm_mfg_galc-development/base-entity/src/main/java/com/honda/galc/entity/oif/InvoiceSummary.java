package com.honda.galc.entity.oif;


import java.io.Serializable;
import java.sql.Date;

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

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.product.InstalledPartId;

@Entity
@Table(name = "INVOICE_SUMMARY_TBX")
public class InvoiceSummary extends AuditEntry{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SUM_PRICE")
	private double sumPrice;

	@Column(name = "SUM_TOTAL_QUANTITY")
	private int sumTotalQuantity;

	@Column(name = "SUM_SHIPMENT_NO")
	private String sumShipmentNo;

	@Column(name = "SUM_INVOICE_DATE")
	private Date sumInvoiceDate;

	@Column(name = "SHIP_DATE")
	private Date shipDate;
	
    @EmbeddedId
    private InvoiceSummaryId id;

	@ManyToOne
	@JoinColumn(name="INVOICE_NO" ,updatable = false,insertable=false, nullable=false)
    private Invoice invoice;

	public InvoiceSummaryId getId() {
		return id;
	}

	public void setId(InvoiceSummaryId id) {
		this.id = id;
	}
	
	public double getSumPrice()  {
		return sumPrice;
	}

	public void setSumPrice(double newPrice)  {
		sumPrice=newPrice;
	}

	public int getSumTotalQuantity()  {
		return sumTotalQuantity;
	}

	public void setSumTotalQuantity(int newTotalQuantity)  {
		sumTotalQuantity=newTotalQuantity;
	}

	public String getSumShipmentNo()  {
		return sumShipmentNo;
	}

	public void setSumShipmentNo(String newShipmentNo)  {
		sumShipmentNo=newShipmentNo;
	}

	public Date getSumInvoiceDate()  {
		return sumInvoiceDate;
	}
	public void setSumInvoiceDate(Date newInvoiceDate)  {
		sumInvoiceDate=newInvoiceDate;
	}


	public Date getShipDate()  {
		return shipDate;
	}

	public void setShipDate(Date newShipDate)  {
		shipDate=newShipDate;
	}
	
	public Invoice getInvoice() {
		return invoice;
	}

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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((invoice == null) ? 0 : invoice.hashCode());
		result = prime * result + ((shipDate == null) ? 0 : shipDate.hashCode());
		result = prime * result + ((sumInvoiceDate == null) ? 0 : sumInvoiceDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(sumPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sumShipmentNo == null) ? 0 : sumShipmentNo.hashCode());
		result = prime * result + sumTotalQuantity;
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
		InvoiceSummary other = (InvoiceSummary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (invoice == null) {
			if (other.invoice != null)
				return false;
		} else if (!invoice.equals(other.invoice))
			return false;
		if (shipDate == null) {
			if (other.shipDate != null)
				return false;
		} else if (!shipDate.equals(other.shipDate))
			return false;
		if (sumInvoiceDate == null) {
			if (other.sumInvoiceDate != null)
				return false;
		} else if (!sumInvoiceDate.equals(other.sumInvoiceDate))
			return false;
		if (Double.doubleToLongBits(sumPrice) != Double.doubleToLongBits(other.sumPrice))
			return false;
		if (sumShipmentNo == null) {
			if (other.sumShipmentNo != null)
				return false;
		} else if (!sumShipmentNo.equals(other.sumShipmentNo))
			return false;
		if (sumTotalQuantity != other.sumTotalQuantity)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceSummary [sumPrice=" + sumPrice + ", sumTotalQuantity=" + sumTotalQuantity + ", sumShipmentNo="
				+ sumShipmentNo + ", sumInvoiceDate=" + sumInvoiceDate + ", shipDate=" + shipDate + ", id=" + id
				+ ", invoice=" + invoice + "]";
	}

}
