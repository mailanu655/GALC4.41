package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
public class Gal103DetailDto{
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
	private String hdrBatchSequence;
	private double price = 0.0D;
	
	public Gal103DetailDto() {}

	public String getDtlRecordId() {
		return dtlRecordId;
	}

	public void setDtlRecordId(String dtlRecordId) {
		this.dtlRecordId = dtlRecordId;
	}

	public String getDtlTranceCode() {
		return dtlTranceCode;
	}

	public void setDtlTranceCode(String dtlTranceCode) {
		this.dtlTranceCode = dtlTranceCode;
	}

	public String getDtlFrameNo() {
		return dtlFrameNo;
	}

	public void setDtlFrameNo(String dtlFrameNo) {
		this.dtlFrameNo = dtlFrameNo;
	}

	public String getDtlInspectionDate() {
		return dtlInspectionDate;
	}

	public void setDtlInspectionDate(String dtlInspectionDate) {
		this.dtlInspectionDate = dtlInspectionDate;
	}

	public String getDtlInspectionTime() {
		return dtlInspectionTime;
	}

	public void setDtlInspectionTime(String dtlInspectionTime) {
		this.dtlInspectionTime = dtlInspectionTime;
	}

	public String getDtlShippingDate() {
		return dtlShippingDate;
	}

	public void setDtlShippingDate(String dtlShippingDate) {
		this.dtlShippingDate = dtlShippingDate;
	}

	public String getDtlShippingTime() {
		return dtlShippingTime;
	}

	public void setDtlShippingTime(String dtlShippingTime) {
		this.dtlShippingTime = dtlShippingTime;
	}

	public String getDtlControlNo() {
		return dtlControlNo;
	}

	public void setDtlControlNo(String dtlControlNo) {
		this.dtlControlNo = dtlControlNo;
	}

	public String getDtlQuotationNo() {
		return dtlQuotationNo;
	}

	public void setDtlQuotationNo(String dtlQuotationNo) {
		this.dtlQuotationNo = dtlQuotationNo;
	}

	public String getDtlPriceType() {
		return dtlPriceType;
	}

	public void setDtlPriceType(String dtlPriceType) {
		this.dtlPriceType = dtlPriceType;
	}

	public String getDtlAhModel() {
		return dtlAhModel;
	}

	public void setDtlAhModel(String dtlAhModel) {
		this.dtlAhModel = dtlAhModel;
	}

	public String getDtlAhDestination() {
		return dtlAhDestination;
	}

	public void setDtlAhDestination(String dtlAhDestination) {
		this.dtlAhDestination = dtlAhDestination;
	}

	public String getDtlAhOption() {
		return dtlAhOption;
	}

	public void setDtlAhOption(String dtlAhOption) {
		this.dtlAhOption = dtlAhOption;
	}

	public String getDtlAhColor() {
		return dtlAhColor;
	}

	public void setDtlAhColor(String dtlAhColor) {
		this.dtlAhColor = dtlAhColor;
	}

	public String getDtlInvoiceNo() {
		return dtlInvoiceNo;
	}

	public void setDtlInvoiceNo(String dtlInvoiceNo) {
		this.dtlInvoiceNo = dtlInvoiceNo;
	}

	public String getDtlAhmInvoiceNo() {
		return dtlAhmInvoiceNo;
	}

	public void setDtlAhmInvoiceNo(String dtlAhmInvoiceNo) {
		this.dtlAhmInvoiceNo = dtlAhmInvoiceNo;
	}

	public String getDtlEngineNo() {
		return dtlEngineNo;
	}

	public void setDtlEngineNo(String dtlEngineNo) {
		this.dtlEngineNo = dtlEngineNo;
	}

	public String getDtlKeyNo() {
		return dtlKeyNo;
	}

	public void setDtlKeyNo(String dtlKeyNo) {
		this.dtlKeyNo = dtlKeyNo;
	}

	public String getDtlCorrectionFlag() {
		return dtlCorrectionFlag;
	}

	public void setDtlCorrectionFlag(String dtlCorrectionFlag) {
		this.dtlCorrectionFlag = dtlCorrectionFlag;
	}

	public String getDtlReceiptDate() {
		return dtlReceiptDate;
	}

	public void setDtlReceiptDate(String dtlReceiptDate) {
		this.dtlReceiptDate = dtlReceiptDate;
	}

	public String getDtlReceiptTime() {
		return dtlReceiptTime;
	}

	public void setDtlReceiptTime(String dtlReceiptTime) {
		this.dtlReceiptTime = dtlReceiptTime;
	}

	public String getDtlValidationDate() {
		return dtlValidationDate;
	}

	public void setDtlValidationDate(String dtlValidationDate) {
		this.dtlValidationDate = dtlValidationDate;
	}

	public String getDtlFinalDate() {
		return dtlFinalDate;
	}

	public void setDtlFinalDate(String dtlFinalDate) {
		this.dtlFinalDate = dtlFinalDate;
	}

	public String getDtlFinalReceiptFlg() {
		return dtlFinalReceiptFlg;
	}

	public void setDtlFinalReceiptFlg(String dtlFinalReceiptFlg) {
		this.dtlFinalReceiptFlg = dtlFinalReceiptFlg;
	}

	public String getDtlFactoryShipmentNo() {
		return dtlFactoryShipmentNo;
	}

	public void setDtlFactoryShipmentNo(String dtlFactoryShipmentNo) {
		this.dtlFactoryShipmentNo = dtlFactoryShipmentNo;
	}

	public String getDtlErrorCode() {
		return dtlErrorCode;
	}

	public void setDtlErrorCode(String dtlErrorCode) {
		this.dtlErrorCode = dtlErrorCode;
	}

	public String getDtlPrice() {
		return dtlPrice;
	}

	public void setDtlPrice(String dtlPrice) {
		this.dtlPrice = dtlPrice;
	}

	public String getDtlUniqueIdOption() {
		return dtlUniqueIdOption;
	}

	public void setDtlUniqueIdOption(String dtlUniqueIdOption) {
		this.dtlUniqueIdOption = dtlUniqueIdOption;
	}

	public String getDtlBatchSequence() {
		return dtlBatchSequence;
	}

	public void setDtlBatchSequence(String dtlBatchSequence) {
		this.dtlBatchSequence = dtlBatchSequence;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHdrBatchSequence() {
		return hdrBatchSequence;
	}

	public void setHdrBatchSequence(String hdrBatchSequence) {
		this.hdrBatchSequence = hdrBatchSequence;
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
		Gal103DetailDto other = (Gal103DetailDto) obj;
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