package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.honda.galc.entity.oif.InvoiceDetail;
import com.honda.galc.entity.oif.InvoiceDetailId;
import com.honda.galc.util.OutputData;

//Additional fields not in DailyDepartmentSchedule
public class InvoiceDTO   implements IOutputFormat { 


	@OutputData("DTL_RECORD_ID")
	private String dtlRecordId;

	@OutputData("DTL_TRANCE_CODE")
	private String dtlTranceCode;

	@OutputData("DTL_FRAME_NO")
	private String dtlFrameNo;

	@OutputData("DTL_INSPECTION_DATE")
	private String dtlInspectionDate;

	@OutputData("DTL_INSPECTION_TIME")
	private String dtlInspectionTime;

	@OutputData("DTL_SHIPPING_DATE")
	private String dtlShippingDate;

	@OutputData("DTL_SHIPPING_TIME")
	private String dtlShippingTime;

	@OutputData("SPACE2")
	private String space2;

	@OutputData("DTL_CONTROL_NO")
	private String dtlControlNo;

	@OutputData("DTL_QUOTATION_NO")
	private String dtlQuotationNo;

	@OutputData("DTL_PRICE_TYPE")
	private String dtlPriceType;

	@OutputData("DTL_AH_MODEL")
	private String dtlAhModel;

	@OutputData("DTL_AH_DESTINATION")
	private String dtlAhDestination;

	@OutputData("DTL_AH_OPTION")
	private String dtlAhOption;

	@OutputData("DTL_AH_COLOR")
	private String dtlAhColor;

	@OutputData("DTL_INVOICE_NO")
	private String dtlInvoiceNo;

	@OutputData("DTL_AHM_INVOICE_NO")
	private String dtlAhmInvoiceNo;

	@OutputData("DTL_ENGINE_NO")
	private String dtlEngineNo;

	@OutputData("DTL_KEY_NO")
	private String dtlKeyNo;

	@OutputData("DTL_CORRECTION_FLAG")
	private String dtlCorrectionFlag;

	@OutputData("DTL_RECEIPT_DATE")
	private String dtlReceiptDate;

	@OutputData("DTL_RECEIPT_TIME")
	private String dtlReceiptTime;

	@OutputData("DTL_VALIDATION_DATE")
	private String dtlValidationDate;

	@OutputData("DTL_FINAL_DATE")
	private String dtlFinalDate;

	@OutputData("DTL_FINAL_RECEIPT_FLG")
	private String dtlFinalReceiptFlg;

	@OutputData("DTL_FACTORY_SHIPMENT_NO")
	private String dtlFactoryShipmentNo;

	@OutputData("DTL_ERROR_CODE")
	private String dtlErrorCode;

	@OutputData("DTL_PRICE")
	private String dtlPrice;

	@OutputData("DTL_UNIQUE_ID_OPTION")
	private String dtlUniqueIdOption;

	@OutputData("SPACE3")
	private String space3;

	@OutputData("DTL_BATCH_SEQUENCE")
	private String dtlBatchSequence;

	public static DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
	public static DateFormat timeFormat = new SimpleDateFormat("HHmmss");
	private Timestamp shippingTs = null;
	private Timestamp inspectTs = null;
	
	public InvoiceDetail getInvoiceDetail()  {
		InvoiceDetail invD = new InvoiceDetail();
		InvoiceDetailId id = new InvoiceDetailId();
		id.setProductId(getDtlFrameNo());
		invD.setId(id);
		invD.setInspectionTimestamp(getInspectTs());
		invD.setShippingTimestamp(getShippingTs());
		invD.setPrice(Double.valueOf(getDtlPrice())/100);
		invD.setQuotationNo(getDtlQuotationNo());
		invD.setPriceType(getDtlPriceType());
		invD.setAhmInvoiceNo(getDtlAhmInvoiceNo());
		invD.setCorrectionFlag(getDtlCorrectionFlag());
		invD.setReceiptTimestamp("0");
		invD.setValidationDate("0");
		invD.setFinalDate("0");
		invD.setFinalReceiptFlg("F");
		invD.setErrorCode(" ");
		invD.setFactoryShipmentNo(getDtlFactoryShipmentNo());
		invD.setDtlRowNo(Integer.valueOf(getDtlBatchSequence().trim()));
		
		//Dtl_row_no?
		return invD;
	}
	
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

	public String getSpace2()  {
		return space2;
	}

	public void setSpace2(String newSpace2)  {
		space2=newSpace2;
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

	public String getSpace3()  {
		return space3;
	}

	public void setSpace3(String newSpace3)  {
		space3=newSpace3;
	}

	public String getDtlBatchSequence()  {
		return dtlBatchSequence;
	}

	public void setDtlBatchSequence(String newDtlBatchSequence)  {
		dtlBatchSequence=newDtlBatchSequence;
	}


	private double price = 0.0D;
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Timestamp getShippingTs() {
		return shippingTs;
	}

	public void setShippingTs(Timestamp shippingTs) {
		this.shippingTs = shippingTs;
		setDtlShippingDate(dateFormat.format(shippingTs));
		setDtlShippingTime(timeFormat.format(shippingTs));
	}

	public Timestamp getInspectTs() {
		return inspectTs;
	}

	public void setInspectTs(Timestamp inspectTs) {
		this.inspectTs = inspectTs;
		setDtlInspectionDate(dateFormat.format(shippingTs));
		setDtlInspectionTime(timeFormat.format(shippingTs));
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

	@Override
	public String toString() {
		return "InvoiceDTO [dtlRecordId=" + dtlRecordId + ", dtlTranceCode=" + dtlTranceCode + ", dtlFrameNo="
				+ dtlFrameNo + ", dtlInspectionDate=" + dtlInspectionDate + ", dtlInspectionTime=" + dtlInspectionTime
				+ ", dtlShippingDate=" + dtlShippingDate + ", dtlShippingTime=" + dtlShippingTime + ", space2="
				+ space2 + ", dtlControlNo=" + dtlControlNo + ", dtlQuotationNo=" + dtlQuotationNo + ", dtlPriceType="
				+ dtlPriceType + ", dtlAhModel=" + dtlAhModel + ", dtlAhDestination=" + dtlAhDestination
				+ ", dtlAhOption=" + dtlAhOption + ", dtlAhColor=" + dtlAhColor + ", dtlInvoiceNo=" + dtlInvoiceNo
				+ ", dtlAhmInvoiceNo=" + dtlAhmInvoiceNo + ", dtlEngineNo=" + dtlEngineNo + ", dtlKeyNo=" + dtlKeyNo
				+ ", dtlCorrectionFlag=" + dtlCorrectionFlag + ", dtlReceiptDate=" + dtlReceiptDate
				+ ", dtlReceiptTime=" + dtlReceiptTime + ", dtlValidationDate=" + dtlValidationDate + ", dtlFinalDate="
				+ dtlFinalDate + ", dtlFinalReceiptFlg=" + dtlFinalReceiptFlg + ", dtlFactoryShipmentNo="
				+ dtlFactoryShipmentNo + ", dtlErrorCode=" + dtlErrorCode + ", dtlPrice=" + dtlPrice
				+ ", dtlUniqueIdOption=" + dtlUniqueIdOption + ", space3=" + space3 + ", dtlBatchSequence="
				+ dtlBatchSequence + ", price=" + price + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
		InvoiceDTO other = (InvoiceDTO) obj;
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


}
