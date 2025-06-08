package com.honda.galc.entity.oif;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Embeddable
public class InvoiceSummaryId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name = "INVOICE_NO")
	private Long invoiceNo;

	public long getInvoiceNo()  {
		return invoiceNo;
	}

	public void setInvoiceNo(long newInvoiceNo)  {
		invoiceNo=newInvoiceNo;
	}
	

	@Column(name = "QUOTATION_NO")
	private String quotationNo;

	public String getQuotationNo()  {
		return quotationNo;
	}

	public void setQuotationNo(String newQuotationNo)  {
		quotationNo=newQuotationNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (invoiceNo ^ (invoiceNo >>> 32));
		result = prime * result + ((quotationNo == null) ? 0 : quotationNo.hashCode());
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
		InvoiceSummaryId other = (InvoiceSummaryId) obj;
		if (invoiceNo != other.invoiceNo)
			return false;
		if (quotationNo == null) {
			if (other.quotationNo != null)
				return false;
		} else if (!quotationNo.equals(other.quotationNo))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceSummaryId [invoiceNo=" + invoiceNo + ", quotationNo=" + quotationNo + "]";
	}


}
