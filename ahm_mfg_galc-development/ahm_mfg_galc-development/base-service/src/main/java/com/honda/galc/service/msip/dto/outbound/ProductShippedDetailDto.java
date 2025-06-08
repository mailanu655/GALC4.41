package com.honda.galc.service.msip.dto.outbound;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date Jun 22, 2017
 */
public class ProductShippedDetailDto extends BaseOutboundDto implements IMsipOutboundDto {

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
	private String plantName = "";
	private String siteName;
	private String processPointId = "";
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public ProductShippedDetailDto() {}

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
	
	public String getSiteName() {
		return siteName;
	}
	
	public String getPlantName() {
		return StringUtils.trimToEmpty(plantName);
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
}