package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
//Additional fields not in DailyDepartmentSchedule
public class Gal103SummaryDto{ 

	private String sumRecordId;

	private String sumTranceCode;

	private String sumInvoiceNo;

	private String sumPrice;

	private String sumTotalQuantity;

	private String space4;

	private String sumQuotationNo;

	private String sumInvoiceDate;

	private String sumShipmentNo;

	private String sumShippingDate;

	private String space5;

	private String sumProcDate;

	private String space6;

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

	public void setSumInvoiceDate(String newSumInvoiceDate)  {
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

	public void setSumShippingDate(String newSumShippingDate)  {
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

	public void setSumProcDate(String newSumProcDate)  {
		sumProcDate=newSumProcDate;
	}

	public String getSpace6()  {
		return space6;
	}

	public void setSpace6(String newSpace6)  {
		space6=newSpace6;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sumRecordId == null) ? 0 : sumRecordId.hashCode());
		result = prime * result + ((sumTranceCode == null) ? 0 : sumTranceCode.hashCode());
		result = prime * result + ((sumInvoiceNo == null) ? 0 : sumInvoiceNo.hashCode());
		result = prime * result + ((sumPrice == null) ? 0 : sumPrice.hashCode());
		result = prime * result + ((sumTotalQuantity == null) ? 0 : sumTotalQuantity.hashCode());
		result = prime * result + ((space4 == null) ? 0 : space4.hashCode());
		result = prime * result + ((sumQuotationNo == null) ? 0 : sumQuotationNo.hashCode());
		result = prime * result + ((sumInvoiceDate == null) ? 0 : sumInvoiceDate.hashCode());
		result = prime * result + ((sumShipmentNo == null) ? 0 : sumShipmentNo.hashCode());
		result = prime * result + ((sumShippingDate == null) ? 0 : sumShippingDate.hashCode());
		result = prime * result + ((space5 == null) ? 0 : space5.hashCode());
		result = prime * result + ((sumProcDate == null) ? 0 : sumProcDate.hashCode());
		result = prime * result + ((space6 == null) ? 0 : space6.hashCode());
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
		Gal103SummaryDto other = (Gal103SummaryDto) obj;
		if (sumRecordId == null) {
			if (other.sumRecordId != null)
				return false;
		} else if (!sumRecordId.equals(other.sumRecordId))
			return false;
		if (sumTranceCode == null) {
			if (other.sumTranceCode != null)
				return false;
		} else if (!sumTranceCode.equals(other.sumTranceCode))
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
		if (sumTotalQuantity == null) {
			if (other.sumTotalQuantity != null)
				return false;
		} else if (!sumTotalQuantity.equals(other.sumTotalQuantity))
			return false;
		if (space4 == null) {
			if (other.space4 != null)
				return false;
		} else if (!space4.equals(other.space4))
			return false;
		if (sumQuotationNo == null) {
			if (other.sumQuotationNo != null)
				return false;
		} else if (!sumQuotationNo.equals(other.sumQuotationNo))
			return false;
		if (sumInvoiceDate == null) {
			if (other.sumInvoiceDate != null)
				return false;
		} else if (!sumInvoiceDate.equals(other.sumInvoiceDate))
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
		if (space5 == null) {
			if (other.space5 != null)
				return false;
		} else if (!space5.equals(other.space5))
			return false;
		if (sumProcDate == null) {
			if (other.sumProcDate != null)
				return false;
		} else if (!sumProcDate.equals(other.sumProcDate))
			return false;
		if (space6 == null) {
			if (other.space6 != null)
				return false;
		} else if (!space6.equals(other.space6))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
