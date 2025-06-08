package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date May 8, 2017
 */
public class InvoiceDto implements IMsipInboundDto {

	private static final long serialVersionUID = 1L;

	private String dtlRecordId;
	private String dtlTranceCode;
	private String dtlFrameNo;
	private String dtlInspectionDate;
	private String dtlInspectionTime;
	private String dtlShippingDate;
	private String dtlShippingTime;
	private String dtlControlNo;
	private String dtlQuotationNo;
	private String dtlPriceType;
	private String dtlAhModel;
	private String dtlAhDestination;
	private String dtlAhOption;
	private String dtlAhColor;
	private String dtlInvoiceNo;
	private String dtlAhmInvoiceNo;
	private String dtlEngineNo;
	private String dtlKeyNo;
	private String dtlCorrectionFlag;
	private String dtlReceiptDate;
	private String dtlReceiptTime;
	private String dtlValidationDate;
	private String dtlFinalDate;
	private String dtlFinalReceiptFlg;
	private String dtlFactoryShipmentNo;
	private String dtlErrorCode;
	private String dtlPrice;
	private String dtlUniqueIdOption;
	private String dtlBatchSequence;
	private double price = 0.0D;
	
	public String getDtlRecordId()  {
		return dtlRecordId;
	}

	public void setDtlRecordId(String newDtlRecordId)  {
		dtlRecordId=newDtlRecordId;
	}

	public String getDtlTranceCode()  {
		return dtlTranceCode;
	}

	public void setDtlTranceCode(String newDtlTranceCode)  {
		dtlTranceCode=newDtlTranceCode;
	}

	public String getDtlFrameNo()  {
		return dtlFrameNo;
	}

	public void setDtlFrameNo(String newDtlFrameNo)  {
		dtlFrameNo=newDtlFrameNo;
	}

	public String getDtlInspectionDate()  {
		return dtlInspectionDate;
	}

	public void setDtlInspectionDate(String newDtlInspectionDate)  {
		dtlInspectionDate=newDtlInspectionDate;
	}

	public String getDtlInspectionTime()  {
		return dtlInspectionTime;
	}

	public void setDtlInspectionTime(String newDtlInspectionTime)  {
		dtlInspectionTime=newDtlInspectionTime;
	}

	public String getDtlShippingDate()  {
		return dtlShippingDate;
	}

	public void setDtlShippingDate(String newDtlShippingDate)  {
		dtlShippingDate=newDtlShippingDate;
	}

	public String getDtlShippingTime()  {
		return dtlShippingTime;
	}

	public void setDtlShippingTime(String newDtlShippingTime)  {
		dtlShippingTime=newDtlShippingTime;
	}

	public String getDtlControlNo()  {
		return dtlControlNo;
	}

	public void setDtlControlNo(String newDtlControlNo)  {
		dtlControlNo=newDtlControlNo;
	}

	public String getDtlQuotationNo()  {
		return dtlQuotationNo;
	}

	public void setDtlQuotationNo(String newDtlQuotationNo)  {
		dtlQuotationNo=newDtlQuotationNo;
	}

	public String getDtlPriceType()  {
		return dtlPriceType;
	}

	public void setDtlPriceType(String newDtlPriceType)  {
		dtlPriceType=newDtlPriceType;
	}

	public String getDtlAhModel()  {
		return dtlAhModel;
	}

	public void setDtlAhModel(String newDtlAhModel)  {
		dtlAhModel=newDtlAhModel;
	}

	public String getDtlAhDestination()  {
		return dtlAhDestination;
	}

	public void setDtlAhDestination(String newDtlAhDestination)  {
		dtlAhDestination=newDtlAhDestination;
	}

	public String getDtlAhOption()  {
		return dtlAhOption;
	}

	public void setDtlAhOption(String newDtlAhOption)  {
		dtlAhOption=newDtlAhOption;
	}

	public String getDtlAhColor()  {
		return dtlAhColor;
	}

	public void setDtlAhColor(String newDtlAhColor)  {
		dtlAhColor=newDtlAhColor;
	}

	public String getDtlInvoiceNo()  {
		return dtlInvoiceNo;
	}

	public void setDtlInvoiceNo(String newDtlInvoiceNo)  {
		dtlInvoiceNo=newDtlInvoiceNo;
	}

	public String getDtlAhmInvoiceNo()  {
		return dtlAhmInvoiceNo;
	}

	public void setDtlAhmInvoiceNo(String newDtlAhmInvoiceNo)  {
		dtlAhmInvoiceNo=newDtlAhmInvoiceNo;
	}

	public String getDtlEngineNo()  {
		return dtlEngineNo;
	}

	public void setDtlEngineNo(String newDtlEngineNo)  {
		dtlEngineNo=newDtlEngineNo;
	}

	public String getDtlKeyNo()  {
		return dtlKeyNo;
	}

	public void setDtlKeyNo(String newDtlKeyNo)  {
		dtlKeyNo=newDtlKeyNo;
	}

	public String getDtlCorrectionFlag()  {
		return dtlCorrectionFlag;
	}

	public void setDtlCorrectionFlag(String newDtlCorrectionFlag)  {
		dtlCorrectionFlag=newDtlCorrectionFlag;
	}

	public String getDtlReceiptDate()  {
		return dtlReceiptDate;
	}

	public void setDtlReceiptDate(String newDtlReceiptDate)  {
		dtlReceiptDate=newDtlReceiptDate;
	}

	public String getDtlReceiptTime()  {
		return dtlReceiptTime;
	}

	public void setDtlReceiptTime(String newDtlReceiptTime)  {
		dtlReceiptTime=newDtlReceiptTime;
	}

	public String getDtlValidationDate()  {
		return dtlValidationDate;
	}

	public void setDtlValidationDate(String newDtlValidationDate)  {
		dtlValidationDate=newDtlValidationDate;
	}

	public String getDtlFinalDate()  {
		return dtlFinalDate;
	}

	public void setDtlFinalDate(String newDtlFinalDate)  {
		dtlFinalDate=newDtlFinalDate;
	}

	public String getDtlFinalReceiptFlg()  {
		return dtlFinalReceiptFlg;
	}

	public void setDtlFinalReceiptFlg(String newDtlFinalReceiptFlg)  {
		dtlFinalReceiptFlg=newDtlFinalReceiptFlg;
	}

	public String getDtlFactoryShipmentNo()  {
		return dtlFactoryShipmentNo;
	}

	public void setDtlFactoryShipmentNo(String newDtlFactoryShipmentNo)  {
		dtlFactoryShipmentNo=newDtlFactoryShipmentNo;
	}

	public String getDtlErrorCode()  {
		return dtlErrorCode;
	}

	public void setDtlErrorCode(String newDtlErrorCode)  {
		dtlErrorCode=newDtlErrorCode;
	}

	public String getDtlPrice()  {
		return dtlPrice;
	}

	public void setDtlPrice(String newDtlPrice)  {
		dtlPrice=newDtlPrice;
	}

	public String getDtlUniqueIdOption()  {
		return dtlUniqueIdOption;
	}

	public void setDtlUniqueIdOption(String newDtlUniqueIdOption)  {
		dtlUniqueIdOption=newDtlUniqueIdOption;
	}

	public String getDtlBatchSequence()  {
		return dtlBatchSequence;
	}

	public void setDtlBatchSequence(String newDtlBatchSequence)  {
		dtlBatchSequence=newDtlBatchSequence;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtlAhColor == null) ? 0 : dtlAhColor.hashCode());
		result = prime * result + ((dtlAhDestination == null) ? 0 : dtlAhDestination.hashCode());
		result = prime * result + ((dtlAhModel == null) ? 0 : dtlAhModel.hashCode());
		result = prime * result + ((dtlAhOption == null) ? 0 : dtlAhOption.hashCode());
		result = prime * result + ((dtlAhmInvoiceNo == null) ? 0 : dtlAhmInvoiceNo.hashCode());
		result = prime * result + ((dtlBatchSequence == null) ? 0 : dtlBatchSequence.hashCode());
		result = prime * result + ((dtlControlNo == null) ? 0 : dtlControlNo.hashCode());
		result = prime * result + ((dtlCorrectionFlag == null) ? 0 : dtlCorrectionFlag.hashCode());
		result = prime * result + ((dtlEngineNo == null) ? 0 : dtlEngineNo.hashCode());
		result = prime * result + ((dtlErrorCode == null) ? 0 : dtlErrorCode.hashCode());
		result = prime * result + ((dtlFactoryShipmentNo == null) ? 0 : dtlFactoryShipmentNo.hashCode());
		result = prime * result + ((dtlFinalDate == null) ? 0 : dtlFinalDate.hashCode());
		result = prime * result + ((dtlFinalReceiptFlg == null) ? 0 : dtlFinalReceiptFlg.hashCode());
		result = prime * result + ((dtlFrameNo == null) ? 0 : dtlFrameNo.hashCode());
		result = prime * result + ((dtlInspectionDate == null) ? 0 : dtlInspectionDate.hashCode());
		result = prime * result + ((dtlInspectionTime == null) ? 0 : dtlInspectionTime.hashCode());
		result = prime * result + ((dtlInvoiceNo == null) ? 0 : dtlInvoiceNo.hashCode());
		result = prime * result + ((dtlKeyNo == null) ? 0 : dtlKeyNo.hashCode());
		result = prime * result + ((dtlPrice == null) ? 0 : dtlPrice.hashCode());
		result = prime * result + ((dtlPriceType == null) ? 0 : dtlPriceType.hashCode());
		result = prime * result + ((dtlQuotationNo == null) ? 0 : dtlQuotationNo.hashCode());
		result = prime * result + ((dtlReceiptDate == null) ? 0 : dtlReceiptDate.hashCode());
		result = prime * result + ((dtlReceiptTime == null) ? 0 : dtlReceiptTime.hashCode());
		result = prime * result + ((dtlRecordId == null) ? 0 : dtlRecordId.hashCode());
		result = prime * result + ((dtlShippingDate == null) ? 0 : dtlShippingDate.hashCode());
		result = prime * result + ((dtlShippingTime == null) ? 0 : dtlShippingTime.hashCode());
		result = prime * result + ((dtlTranceCode == null) ? 0 : dtlTranceCode.hashCode());
		result = prime * result + ((dtlUniqueIdOption == null) ? 0 : dtlUniqueIdOption.hashCode());
		result = prime * result + ((dtlValidationDate == null) ? 0 : dtlValidationDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		InvoiceDto other = (InvoiceDto) obj;
		if (dtlAhColor == null) {
			if (other.dtlAhColor != null)
				return false;
		} else if (!dtlAhColor.equals(other.dtlAhColor))
			return false;
		if (dtlAhDestination == null) {
			if (other.dtlAhDestination != null)
				return false;
		} else if (!dtlAhDestination.equals(other.dtlAhDestination))
			return false;
		if (dtlAhModel == null) {
			if (other.dtlAhModel != null)
				return false;
		} else if (!dtlAhModel.equals(other.dtlAhModel))
			return false;
		if (dtlAhOption == null) {
			if (other.dtlAhOption != null)
				return false;
		} else if (!dtlAhOption.equals(other.dtlAhOption))
			return false;
		if (dtlAhmInvoiceNo == null) {
			if (other.dtlAhmInvoiceNo != null)
				return false;
		} else if (!dtlAhmInvoiceNo.equals(other.dtlAhmInvoiceNo))
			return false;
		if (dtlBatchSequence == null) {
			if (other.dtlBatchSequence != null)
				return false;
		} else if (!dtlBatchSequence.equals(other.dtlBatchSequence))
			return false;
		if (dtlControlNo == null) {
			if (other.dtlControlNo != null)
				return false;
		} else if (!dtlControlNo.equals(other.dtlControlNo))
			return false;
		if (dtlCorrectionFlag == null) {
			if (other.dtlCorrectionFlag != null)
				return false;
		} else if (!dtlCorrectionFlag.equals(other.dtlCorrectionFlag))
			return false;
		if (dtlEngineNo == null) {
			if (other.dtlEngineNo != null)
				return false;
		} else if (!dtlEngineNo.equals(other.dtlEngineNo))
			return false;
		if (dtlErrorCode == null) {
			if (other.dtlErrorCode != null)
				return false;
		} else if (!dtlErrorCode.equals(other.dtlErrorCode))
			return false;
		if (dtlFactoryShipmentNo == null) {
			if (other.dtlFactoryShipmentNo != null)
				return false;
		} else if (!dtlFactoryShipmentNo.equals(other.dtlFactoryShipmentNo))
			return false;
		if (dtlFinalDate == null) {
			if (other.dtlFinalDate != null)
				return false;
		} else if (!dtlFinalDate.equals(other.dtlFinalDate))
			return false;
		if (dtlFinalReceiptFlg == null) {
			if (other.dtlFinalReceiptFlg != null)
				return false;
		} else if (!dtlFinalReceiptFlg.equals(other.dtlFinalReceiptFlg))
			return false;
		if (dtlFrameNo == null) {
			if (other.dtlFrameNo != null)
				return false;
		} else if (!dtlFrameNo.equals(other.dtlFrameNo))
			return false;
		if (dtlInspectionDate == null) {
			if (other.dtlInspectionDate != null)
				return false;
		} else if (!dtlInspectionDate.equals(other.dtlInspectionDate))
			return false;
		if (dtlInspectionTime == null) {
			if (other.dtlInspectionTime != null)
				return false;
		} else if (!dtlInspectionTime.equals(other.dtlInspectionTime))
			return false;
		if (dtlInvoiceNo == null) {
			if (other.dtlInvoiceNo != null)
				return false;
		} else if (!dtlInvoiceNo.equals(other.dtlInvoiceNo))
			return false;
		if (dtlKeyNo == null) {
			if (other.dtlKeyNo != null)
				return false;
		} else if (!dtlKeyNo.equals(other.dtlKeyNo))
			return false;
		if (dtlPrice == null) {
			if (other.dtlPrice != null)
				return false;
		} else if (!dtlPrice.equals(other.dtlPrice))
			return false;
		if (dtlPriceType == null) {
			if (other.dtlPriceType != null)
				return false;
		} else if (!dtlPriceType.equals(other.dtlPriceType))
			return false;
		if (dtlQuotationNo == null) {
			if (other.dtlQuotationNo != null)
				return false;
		} else if (!dtlQuotationNo.equals(other.dtlQuotationNo))
			return false;
		if (dtlReceiptDate == null) {
			if (other.dtlReceiptDate != null)
				return false;
		} else if (!dtlReceiptDate.equals(other.dtlReceiptDate))
			return false;
		if (dtlReceiptTime == null) {
			if (other.dtlReceiptTime != null)
				return false;
		} else if (!dtlReceiptTime.equals(other.dtlReceiptTime))
			return false;
		if (dtlRecordId == null) {
			if (other.dtlRecordId != null)
				return false;
		} else if (!dtlRecordId.equals(other.dtlRecordId))
			return false;
		if (dtlShippingDate == null) {
			if (other.dtlShippingDate != null)
				return false;
		} else if (!dtlShippingDate.equals(other.dtlShippingDate))
			return false;
		if (dtlShippingTime == null) {
			if (other.dtlShippingTime != null)
				return false;
		} else if (!dtlShippingTime.equals(other.dtlShippingTime))
			return false;
		if (dtlTranceCode == null) {
			if (other.dtlTranceCode != null)
				return false;
		} else if (!dtlTranceCode.equals(other.dtlTranceCode))
			return false;
		if (dtlUniqueIdOption == null) {
			if (other.dtlUniqueIdOption != null)
				return false;
		} else if (!dtlUniqueIdOption.equals(other.dtlUniqueIdOption))
			return false;
		if (dtlValidationDate == null) {
			if (other.dtlValidationDate != null)
				return false;
		} else if (!dtlValidationDate.equals(other.dtlValidationDate))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}

