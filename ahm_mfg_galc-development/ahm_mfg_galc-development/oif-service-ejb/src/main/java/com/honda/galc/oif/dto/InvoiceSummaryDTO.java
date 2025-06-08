package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import com.honda.galc.entity.oif.InvoiceSummary;
import com.honda.galc.entity.oif.InvoiceSummaryId;
import com.honda.galc.util.OutputData;

//Additional fields not in DailyDepartmentSchedule
public class InvoiceSummaryDTO   implements IOutputFormat { 

	@OutputData("SUM_RECORD_ID")
	private String sumRecordId;

	@OutputData("SUM_TRANCE_CODE")
	private String sumTranceCode;

	@OutputData("SUM_INVOICE_NO")
	private String sumInvoiceNo;

	@OutputData("SUM_PRICE")
	private String sumPrice;

	@OutputData("SUM_TOTAL_QUANTITY")
	private String sumTotalQuantity;

	@OutputData("SPACE4")
	private String space4;

	@OutputData("SUM_QUOTATION_NO")
	private String sumQuotationNo;

	@OutputData("SUM_INVOICE_DATE")
	private String sumInvoiceDate;

	@OutputData("SUM_SHIPMENT_NO")
	private String sumShipmentNo;

	@OutputData("SUM_SHIPPING_DATE")
	private String sumShippingDate;

	@OutputData("SPACE5")
	private String space5;

	@OutputData("SUM_PROC_DATE")
	private String sumProcDate;

	@OutputData("SPACE6")
	private String space6;

	private Timestamp shipTs = null;
	private Timestamp procTs = null; //today
	private Timestamp invoiceTs = null;  //today + 1 day
	
	public InvoiceSummary getInvoiceSummary()  {
		InvoiceSummary invS = new InvoiceSummary();
		InvoiceSummaryId invSumId = new InvoiceSummaryId();
		invSumId.setQuotationNo(getSumQuotationNo());
		invS.setId(invSumId);
		invS.setSumPrice(Double.valueOf(getSumPrice())/100);
		invS.setSumTotalQuantity(Integer.valueOf(getSumTotalQuantity()));
		invS.setSumShipmentNo(getSumShipmentNo());
		invS.setSumInvoiceDate(new Date(invoiceTs.getTime()));
		invS.setShipDate(new Date(shipTs.getTime()));
		return invS;
	}
	
	public String getSumRecordId()  {
		return sumRecordId;
	}

	public void setSumRecordId(String newSumRecordId)  {
		sumRecordId=newSumRecordId;
	}

	public String getSumTranceCode()  {
		return sumTranceCode;
	}

	public void setSumTranceCode(String newSumTranceCode)  {
		sumTranceCode=newSumTranceCode;
	}

	public String getSumInvoiceNo()  {
		return sumInvoiceNo;
	}

	public void setSumInvoiceNo(String newSumInvoiceNo)  {
		sumInvoiceNo=newSumInvoiceNo;
	}

	public String getSumPrice()  {
		return sumPrice;
	}

	public void setSumPrice(String newSumPrice)  {
		sumPrice=newSumPrice;
	}

	public String getSumTotalQuantity()  {
		return sumTotalQuantity;
	}

	public void setSumTotalQuantity(String newSumTotalQuantity)  {
		sumTotalQuantity=newSumTotalQuantity;
	}

	public String getSpace4()  {
		return space4;
	}

	public void setSpace4(String newSpace4)  {
		space4=newSpace4;
	}

	public String getSumQuotationNo()  {
		return sumQuotationNo;
	}

	public void setSumQuotationNo(String newSumQuotationNo)  {
		sumQuotationNo=newSumQuotationNo;
	}

	public String getSumInvoiceDate()  {
		return sumInvoiceDate;
	}

	private void setSumInvoiceDate(String newSumInvoiceDate)  {
		sumInvoiceDate=newSumInvoiceDate;
	}

	public String getSumShipmentNo()  {
		return sumShipmentNo;
	}

	public void setSumShipmentNo(String newSumShipmentNo)  {
		sumShipmentNo=newSumShipmentNo;
	}

	public String getSumShippingDate()  {
		return sumShippingDate;
	}

	private void setSumShippingDate(String newSumShippingDate)  {
		sumShippingDate=newSumShippingDate;
	}

	public String getSpace5()  {
		return space5;
	}

	public void setSpace5(String newSpace5)  {
		space5=newSpace5;
	}

	public String getSumProcDate()  {
		return sumProcDate;
	}

	private void setSumProcDate(String newSumProcDate)  {
		sumProcDate=newSumProcDate;
	}

	public String getSpace6()  {
		return space6;
	}

	public void setSpace6(String newSpace6)  {
		space6=newSpace6;
	}

	
	public Timestamp getShipTs() {
		return shipTs;
	}

	public void setShipTs(Timestamp shipTs) {
		this.shipTs = shipTs;
		setSumShippingDate(InvoiceDTO.dateFormat.format(shipTs));
	}

	public Timestamp getProcTs() {
		return procTs;
	}

	public void setProcTs(Timestamp procTs) {
		this.procTs = procTs;
		setSumProcDate(InvoiceDTO.dateFormat.format(procTs));
	}

	public Timestamp getInvoiceTs() {
		return invoiceTs;
	}

	public void setInvoiceTs(Timestamp invoiceTs) {
		this.invoiceTs = invoiceTs;
		setSumInvoiceDate(InvoiceDTO.dateFormat.format(invoiceTs));
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceSummaryDTO [sumRecordId=" + sumRecordId + ", sumTranceCode=" + sumTranceCode + ", sumInvoiceNo="
				+ sumInvoiceNo + ", sumPrice=" + sumPrice + ", sumTotalQuantity=" + sumTotalQuantity
				+ ", sumQuotationNo=" + sumQuotationNo + ", sumInvoiceDate=" + sumInvoiceDate + ", sumShipmentNo="
				+ sumShipmentNo + ", sumShippingDate=" + sumShippingDate + ", sumProcDate=" + sumProcDate + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sumInvoiceDate == null) ? 0 : sumInvoiceDate.hashCode());
		result = prime * result + ((sumInvoiceNo == null) ? 0 : sumInvoiceNo.hashCode());
		result = prime * result + ((sumPrice == null) ? 0 : sumPrice.hashCode());
		result = prime * result + ((sumProcDate == null) ? 0 : sumProcDate.hashCode());
		result = prime * result + ((sumQuotationNo == null) ? 0 : sumQuotationNo.hashCode());
		result = prime * result + ((sumRecordId == null) ? 0 : sumRecordId.hashCode());
		result = prime * result + ((sumShipmentNo == null) ? 0 : sumShipmentNo.hashCode());
		result = prime * result + ((sumShippingDate == null) ? 0 : sumShippingDate.hashCode());
		result = prime * result + ((sumTotalQuantity == null) ? 0 : sumTotalQuantity.hashCode());
		result = prime * result + ((sumTranceCode == null) ? 0 : sumTranceCode.hashCode());
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
		InvoiceSummaryDTO other = (InvoiceSummaryDTO) obj;
		if (sumInvoiceDate == null) {
			if (other.sumInvoiceDate != null)
				return false;
		} else if (!sumInvoiceDate.equals(other.sumInvoiceDate))
			return false;
		if (sumInvoiceNo == null) {
			if (other.sumInvoiceNo != null)
				return false;
		} else if (!sumInvoiceNo.equals(other.sumInvoiceNo))
			return false;
		if (sumPrice == null) {
			if (other.sumPrice != null)
				return false;
		} else if (!sumPrice.equals(other.sumPrice))
			return false;
		if (sumProcDate == null) {
			if (other.sumProcDate != null)
				return false;
		} else if (!sumProcDate.equals(other.sumProcDate))
			return false;
		if (sumQuotationNo == null) {
			if (other.sumQuotationNo != null)
				return false;
		} else if (!sumQuotationNo.equals(other.sumQuotationNo))
			return false;
		if (sumRecordId == null) {
			if (other.sumRecordId != null)
				return false;
		} else if (!sumRecordId.equals(other.sumRecordId))
			return false;
		if (sumShipmentNo == null) {
			if (other.sumShipmentNo != null)
				return false;
		} else if (!sumShipmentNo.equals(other.sumShipmentNo))
			return false;
		if (sumShippingDate == null) {
			if (other.sumShippingDate != null)
				return false;
		} else if (!sumShippingDate.equals(other.sumShippingDate))
			return false;
		if (sumTotalQuantity == null) {
			if (other.sumTotalQuantity != null)
				return false;
		} else if (!sumTotalQuantity.equals(other.sumTotalQuantity))
			return false;
		if (sumTranceCode == null) {
			if (other.sumTranceCode != null)
				return false;
		} else if (!sumTranceCode.equals(other.sumTranceCode))
			return false;
		return true;
	}
	
	
}
