package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.honda.galc.entity.oif.Invoice;
import com.honda.galc.util.OutputData;

//Additional fields not in DailyDepartmentSchedule
public class InvoiceHeaderDTO   implements IOutputFormat { 


	@OutputData("HDR_RECORD_ID")
	private String hdrRecordId;

	@OutputData("HDR_TRANS_CODE")
	private String hdrTransCode;

	@OutputData("HDR_BATCH_DATE")
	private String hdrBatchDate;

	@OutputData("HDR_BATCH_TIME")
	private String hdrBatchTime;

	@OutputData("SPACE")
	private String space;

	@OutputData("HDR_BATCH_SEQUENCE")
	private String hdrBatchSequence;

	@OutputData("SPACE1")
	private String space1;

	private Timestamp invoiceDate = null;
	private Timestamp batchTime = null;
	
	public String getHdrRecordId()  {
		return hdrRecordId;
	}

	public void setHdrRecordId(String newHdrRecordId)  {
		hdrRecordId=newHdrRecordId;
	}

	public String getHdrTransCode()  {
		return hdrTransCode;
	}

	public void setHdrTransCode(String newHdrTransCode)  {
		hdrTransCode=newHdrTransCode;
	}

	public String getHdrBatchDate()  {
		return hdrBatchDate;
	}

	public void setHdrBatchDate(String newHdrBatchDate)  {
		hdrBatchDate=newHdrBatchDate;
	}

	public String getHdrBatchTime()  {
		return hdrBatchTime;
	}

	public void setHdrBatchTime(String newHdrBatchTime)  {
		hdrBatchTime=newHdrBatchTime;
	}

	public String getSpace()  {
		return space;
	}

	public void setSpace(String newSpace)  {
		space=newSpace;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Timestamp getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(Timestamp batchTime) {
		this.batchTime = batchTime;
		setHdrBatchDate(InvoiceDTO.dateFormat.format(batchTime));
		setHdrBatchTime(InvoiceDTO.timeFormat.format(batchTime));
	}

	public String getHdrBatchSequence()  {
		return hdrBatchSequence;
	}

	public void setHdrBatchSequence(String newHdrBatchSequence)  {
		hdrBatchSequence=newHdrBatchSequence;
	}

	public String getSpace1()  {
		return space1;
	}

	public void setSpace1(String newSpace1)  {
		space1=newSpace1;
	}

	
	public void initialize(Map<String,String>  inputValues)  {
		if(inputValues == null || inputValues.isEmpty())  return;
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields)  {
			OutputData a1 = f.getAnnotation(OutputData.class);
			if(!inputValues.containsKey(a1.value()))  continue;
			if(f.getType().isAssignableFrom(String.class))  {
				try {
					f.set(this, inputValues.get(a1.value()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Invoice getInvoice()  {
		Invoice inv = new Invoice();
		inv.setInvoiceTimestamp(getInvoiceDate());
		inv.setBatchTimestamp(getBatchTime());
		inv.setBatchSequence(Integer.valueOf(getHdrBatchSequence().trim()));
		if(invoiceDate != null)  {
			inv.setProcDate(new Date(getInvoiceDate().getTime()));
		}
		inv.setNumDtlRec(0);
		inv.setNumSummaryRec(0);
		return inv;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceHeaderDTO [hdrRecordId=" + hdrRecordId + ", hdrTransCode=" + hdrTransCode + ", hdrBatchDate="
				+ hdrBatchDate + ", hdrBatchTime=" + hdrBatchTime + ", hdrBatchSequence=" + hdrBatchSequence + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hdrBatchDate == null) ? 0 : hdrBatchDate.hashCode());
		result = prime * result + ((hdrBatchSequence == null) ? 0 : hdrBatchSequence.hashCode());
		result = prime * result + ((hdrBatchTime == null) ? 0 : hdrBatchTime.hashCode());
		result = prime * result + ((hdrRecordId == null) ? 0 : hdrRecordId.hashCode());
		result = prime * result + ((hdrTransCode == null) ? 0 : hdrTransCode.hashCode());
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
		InvoiceHeaderDTO other = (InvoiceHeaderDTO) obj;
		if (hdrBatchDate == null) {
			if (other.hdrBatchDate != null)
				return false;
		} else if (!hdrBatchDate.equals(other.hdrBatchDate))
			return false;
		if (hdrBatchSequence == null) {
			if (other.hdrBatchSequence != null)
				return false;
		} else if (!hdrBatchSequence.equals(other.hdrBatchSequence))
			return false;
		if (hdrBatchTime == null) {
			if (other.hdrBatchTime != null)
				return false;
		} else if (!hdrBatchTime.equals(other.hdrBatchTime))
			return false;
		if (hdrRecordId == null) {
			if (other.hdrRecordId != null)
				return false;
		} else if (!hdrRecordId.equals(other.hdrRecordId))
			return false;
		if (hdrTransCode == null) {
			if (other.hdrTransCode != null)
				return false;
		} else if (!hdrTransCode.equals(other.hdrTransCode))
			return false;
		return true;
	}
	
	
}
