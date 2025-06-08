
package com.honda.galc.entity.oif;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.product.EquipUnitGroup;

@Entity
@Table(name = "INVOICE_TBX")
public class Invoice extends AuditEntry{

	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INVOICE_NO")
	private Long invoiceNo = null;

	@Column(name = "INVOICE_TIMESTAMP")
	private Timestamp invoiceTimestamp;

	@Column(name = "BATCH_TIMESTAMP")
	private Timestamp batchTimeStamp;

	@Column(name = "BATCH_SEQUENCE")
	private int batchSequence;

	@Column(name = "PROC_DATE")
	private Date procDate;

	@Column(name = "NUM_SUMMARY_REC")
	private int numSummaryRec;

	@Column(name = "NUM_DTL_REC")
	private int numDtlRec;

	@Column(name = "TRANCE_CODE")
	private String tranceCode;

	public Long getInvoiceNo()  {
		return invoiceNo;
	}

	public void setInvoiceNo(long newInvoiceNo)  {
		invoiceNo=newInvoiceNo;
	}

    public Long getId() {
    	return getInvoiceNo();
    }

	public Timestamp getInvoiceTimestamp()  {
		return invoiceTimestamp;
	}

	public void setInvoiceTimestamp(Timestamp newInvoiceDate)  {
		invoiceTimestamp=newInvoiceDate;
	}

	public Timestamp getBatchTimestamp()  {
		return batchTimeStamp;
	}

	public void setBatchTimestamp(Timestamp newBatchTime)  {
		batchTimeStamp=newBatchTime;
	}

	public int getBatchSequence()  {
		return batchSequence;
	}

	public void setBatchSequence(int newBatchSequence)  {
		batchSequence=newBatchSequence;
	}

	public Date getProcDate()  {
		return procDate;
	}
	public void setProcDate(Date newProcDate)  {
		procDate=newProcDate;
	}

	public int getNumSummaryRec()  {
		return numSummaryRec;
	}

	public void setNumSummaryRec(int newNumSummaries)  {
		numSummaryRec=newNumSummaries;
	}

	public int getNumDtlRec()  {
		return numDtlRec;
	}

	public void setNumDtlRec(int newNumDtl)  {
		numDtlRec=newNumDtl;
	}

	public String getTranceCode()  {
		return tranceCode;
	}

	public void setTranceCode(String newTranceCode)  {
		tranceCode=newTranceCode;
	}

   @OneToMany(targetEntity = InvoiceDetail.class, mappedBy = "invoice", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OrderBy("dtlRowNo ASC")
    private List<InvoiceDetail> invoiceDetails;
    
	/**
	 * @return the invoiceDetails
	 */
	public List<InvoiceDetail> getInvoiceDetails() {
		return invoiceDetails;
	}

	/**
	 * @param invoiceDetails the invoiceDetails to set
	 */
	public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}


    @OneToMany(targetEntity = InvoiceSummary.class, mappedBy = "invoice", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<InvoiceSummary> invoiceSummaries;

	/**
	 * @return the invoiceSummaries
	 */
	public List<InvoiceSummary> getInvoiceSummaries() {
		return invoiceSummaries;
	}

	/**
	 * @param invoiceSummaries the invoiceSummaries to set
	 */
	public void setInvoiceSummaries(List<InvoiceSummary> invoiceSummaries) {
		this.invoiceSummaries = invoiceSummaries;
	}

	public void addInvoiceDetail(InvoiceDetail invD)  {
		
		if(invoiceDetails == null)  {
			invoiceDetails = new ArrayList<InvoiceDetail>();
		}
		invoiceDetails.add(invD);
	}
	
	public void addInvoiceSummary(InvoiceSummary invS)  {
		
		if(invoiceSummaries == null)  {
			invoiceSummaries = new ArrayList<InvoiceSummary>();
		}
		invoiceSummaries.add(invS);
	}
	
	public Invoice() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batchSequence;
		result = prime * result + ((batchTimeStamp == null) ? 0 : batchTimeStamp.hashCode());
		result = prime * result + ((invoiceDetails == null) ? 0 : invoiceDetails.hashCode());
		result = prime * result + ((invoiceNo == null) ? 0 : invoiceNo.hashCode());
		result = prime * result + ((invoiceSummaries == null) ? 0 : invoiceSummaries.hashCode());
		result = prime * result + ((invoiceTimestamp == null) ? 0 : invoiceTimestamp.hashCode());
		result = prime * result + numDtlRec;
		result = prime * result + numSummaryRec;
		result = prime * result + ((procDate == null) ? 0 : procDate.hashCode());
		result = prime * result + ((tranceCode == null) ? 0 : tranceCode.hashCode());
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
		Invoice other = (Invoice) obj;
		if (batchSequence != other.batchSequence)
			return false;
		if (batchTimeStamp == null) {
			if (other.batchTimeStamp != null)
				return false;
		} else if (!batchTimeStamp.equals(other.batchTimeStamp))
			return false;
		if (invoiceDetails == null) {
			if (other.invoiceDetails != null)
				return false;
		} else if (!invoiceDetails.equals(other.invoiceDetails))
			return false;
		if (invoiceNo == null) {
			if (other.invoiceNo != null)
				return false;
		} else if (!invoiceNo.equals(other.invoiceNo))
			return false;
		if (invoiceSummaries == null) {
			if (other.invoiceSummaries != null)
				return false;
		} else if (!invoiceSummaries.equals(other.invoiceSummaries))
			return false;
		if (invoiceTimestamp == null) {
			if (other.invoiceTimestamp != null)
				return false;
		} else if (!invoiceTimestamp.equals(other.invoiceTimestamp))
			return false;
		if (numDtlRec != other.numDtlRec)
			return false;
		if (numSummaryRec != other.numSummaryRec)
			return false;
		if (procDate == null) {
			if (other.procDate != null)
				return false;
		} else if (!procDate.equals(other.procDate))
			return false;
		if (tranceCode == null) {
			if (other.tranceCode != null)
				return false;
		} else if (!tranceCode.equals(other.tranceCode))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Invoice [invoiceNo=" + invoiceNo + ", invoiceTimestamp=" + invoiceTimestamp + ", batchTimeStamp="
				+ batchTimeStamp + ", batchSequence=" + batchSequence + ", procDate=" + procDate + ", numSummaryRec="
				+ numSummaryRec + ", numDtlRec=" + numDtlRec + ", tranceCode=" + tranceCode + ", invoiceDetails="
				+ invoiceDetails + ", invoiceSummaries=" + invoiceSummaries + "]";
	}
	
}
