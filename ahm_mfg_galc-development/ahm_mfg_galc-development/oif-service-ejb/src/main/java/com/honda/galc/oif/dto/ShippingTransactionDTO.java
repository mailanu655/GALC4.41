package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

public class ShippingTransactionDTO implements IOutputFormat{
	
	@OutputData ( "VIN" )
	private String vin;
	@OutputData ( "DATE" )
	private String dateString;
	@OutputData ( "TIME" )
	private String time;
	@OutputData ( "SEND_LOCATION" )
	private String sendLocation;
	@OutputData ( "TRAN_TYPE" )
	private String tranType;
	@OutputData ( "SALES_MODEL_CODE" )
	private String salesModelCode;
	@OutputData ( "SALES_MODEL_TYPE_CODE" )
	private String salesModelTypeCode;
	@OutputData ( "SALES_MODEL_OPTION_CODE" )
	private String salesModelOptionCode;
	@OutputData ( "SALES_MODEL_COLOR_CODE" )
	private String salesModelColorCode;
	@OutputData ( "ENGINE_NO" )
	private String engineNumber;
	@OutputData ( "KEY_NO" )
	private String keyNumber;
	@OutputData ( "CIC_ISSU_DATA" )
	private String cicIssuData;
	@OutputData ( "ADC_PROCESS_CODE" )
	private String adcProcessCode;
	@OutputData ( "PRODUCTION_LINE_NO" )
	private String lineNumber;
	@OutputData ( "PRODUCTION_DATE_STRING" )
	private String productionDate;
	@OutputData ( "PRODUCTION_SEQUENCE" )
	private String productionSequenceNumber;
	@OutputData ( "PRODUCTION_SUFFIX" )
	private String productionSuffix;
	@OutputData ( "KD_LINE_NO" )
	private String kdLotLineNumber;
	@OutputData ( "KD_DATE" )
	private String kdLotDate;
	@OutputData ( "KD_SEQ_NO" )
	private String kdLotSequenceNumber;
	@OutputData ( "KD_SUFFIX" )
	private String kdLotSuffix;
	@OutputData ( "PRICE_STRING" )
	private String priceString;
	@OutputData ( "VEHICLE_UNIT_ID" )
	private String vechicleUnitId;
	@OutputData ( "VEHICLE_COMM_UNIT_ID" )
	private String vechicleCommonUnitId;
	@OutputData ( "AF_OFF_DATE" )
	private String afOffDate;
	@OutputData ( "CCC_NUMBER" )
	private String cccRegNbr;
	@OutputData ( "PART_INSTALLED" )
	private String partInstalled;
	@OutputData ( "PURCHASE_CONTRACT_NUMBER" )
	private String purchaseContractNumber;
	@OutputData ( "FILLER_1" )
	private String filler1;
	@OutputData ( "FIF_CODES" )
	private String fifCode;
	@OutputData ( "FILLER_2" )
	private String filler2;
	
	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}
	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}
	/**
	 * @param dateString the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the sendLocation
	 */
	public String getSendLocation() {
		return sendLocation;
	}
	/**
	 * @param sendLocation the sendLocation to set
	 */
	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}
	/**
	 * @return the tranType
	 */
	public String getTranType() {
		return tranType;
	}
	/**
	 * @param tranType the tranType to set
	 */
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	/**
	 * @return the salesModelCode
	 */
	public String getSalesModelCode() {
		return salesModelCode;
	}
	/**
	 * @param salesModelCode the salesModelCode to set
	 */
	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}
	/**
	 * @return the salesModelTypeCode
	 */
	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}
	/**
	 * @param salesModelTypeCode the salesModelTypeCode to set
	 */
	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}
	/**
	 * @return the salesModelOptionCode
	 */
	public String getSalesModelOptionCode() {
		return salesModelOptionCode;
	}
	/**
	 * @param salesModelOptionCode the salesModelOptionCode to set
	 */
	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}
	/**
	 * @return the salesModelColorCode
	 */
	public String getSalesModelColorCode() {
		return salesModelColorCode;
	}
	/**
	 * @param salesModelColorCode the salesModelColorCode to set
	 */
	public void setSalesModelColorCode(String salesModelColorCode) {
		this.salesModelColorCode = salesModelColorCode;
	}
	/**
	 * @return the engineNumber
	 */
	public String getEngineNumber() {
		return engineNumber;
	}
	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	/**
	 * @return the keyNumber
	 */
	public String getKeyNumber() {
		return keyNumber;
	}
	/**
	 * @param keyNumber the keyNumber to set
	 */
	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}
	/**
	 * @return the cicIssuData
	 */
	public String getCicIssuData() {
		return cicIssuData;
	}
	/**
	 * @param cicIssuData the cicIssuData to set
	 */
	public void setCicIssuData(String cicIssuData) {
		this.cicIssuData = cicIssuData;
	}
	/**
	 * @return the adcProcessCode
	 */
	public String getAdcProcessCode() {
		return adcProcessCode;
	}
	/**
	 * @param adcProcessCode the adcProcessCode to set
	 */
	public void setAdcProcessCode(String adcProcessCode) {
		this.adcProcessCode = adcProcessCode;
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}
	/**
	 * @param productionDate the productionDate to set
	 */
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	/**
	 * @return the productionSequenceNumber
	 */
	public String getProductionSequenceNumber() {
		return productionSequenceNumber;
	}
	/**
	 * @param productionSequenceNumber the productionSequenceNumber to set
	 */
	public void setProductionSequenceNumber(String productionSequenceNumber) {
		this.productionSequenceNumber = productionSequenceNumber;
	}
	/**
	 * @return the productionSuffix
	 */
	public String getProductionSuffix() {
		return productionSuffix;
	}
	/**
	 * @param productionSuffix the productionSuffix to set
	 */
	public void setProductionSuffix(String productionSuffix) {
		this.productionSuffix = productionSuffix;
	}
	/**
	 * @return the kdLotLineNumber
	 */
	public String getKdLotLineNumber() {
		return kdLotLineNumber;
	}
	/**
	 * @param kdLotLineNumber the kdLotLineNumber to set
	 */
	public void setKdLotLineNumber(String kdLotLineNumber) {
		this.kdLotLineNumber = kdLotLineNumber;
	}
	/**
	 * @return the kdLotDate
	 */
	public String getKdLotDate() {
		return kdLotDate;
	}
	/**
	 * @param kdLotDate the kdLotDate to set
	 */
	public void setKdLotDate(String kdLotDate) {
		this.kdLotDate = kdLotDate;
	}
	/**
	 * @return the kdLotSequenceNumber
	 */
	public String getKdLotSequenceNumber() {
		return kdLotSequenceNumber;
	}
	/**
	 * @param kdLotSequenceNumber the kdLotSequenceNumber to set
	 */
	public void setKdLotSequenceNumber(String kdLotSequenceNumber) {
		this.kdLotSequenceNumber = kdLotSequenceNumber;
	}
	/**
	 * @return the kdLotSuffix
	 */
	public String getKdLotSuffix() {
		return kdLotSuffix;
	}
	/**
	 * @param kdLotSuffix the kdLotSuffix to set
	 */
	public void setKdLotSuffix(String kdLotSuffix) {
		this.kdLotSuffix = kdLotSuffix;
	}
	/**
	 * @return the priceString
	 */
	public String getPriceString() {
		return priceString;
	}
	/**
	 * @param priceString the priceString to set
	 */
	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}
	/**
	 * @return the vechicleUnitId
	 */
	public String getVechicleUnitId() {
		return vechicleUnitId;
	}
	/**
	 * @param vechicleUnitId the vechicleUnitId to set
	 */
	public void setVechicleUnitId(String vechicleUnitId) {
		this.vechicleUnitId = vechicleUnitId;
	}
	/**
	 * @return the vechicleCommonUnitId
	 */
	public String getVechicleCommonUnitId() {
		return vechicleCommonUnitId;
	}
	/**
	 * @param vechicleCommonUnitId the vechicleCommonUnitId to set
	 */
	public void setVechicleCommonUnitId(String vechicleCommonUnitId) {
		this.vechicleCommonUnitId = vechicleCommonUnitId;
	}
	
	/**
	 * @return the afOffDate
	 */
	public String getAfOffDate() {
		return afOffDate;
	}
	/**
	 * @param afOffDate the afOffDate to set
	 */
	public void setAfOffDate(String afOffDate) {
		this.afOffDate = afOffDate;
	}
	/**
	 * @return the cccRegNbr
	 */
	public String getCccRegNbr() {
		return cccRegNbr;
	}
	/**
	 * @param cccRegNbr the cccRegNbr to set
	 */
	public void setCccRegNbr(String cccRegNbr) {
		this.cccRegNbr = cccRegNbr;
	}
	/**
	 * @return the partInstalled
	 */
	public String getPartInstalled() {
		return partInstalled;
	}
	/**
	 * @param partInstalled the partInstalled to set
	 */
	public void setPartInstalled(String partInstalled) {
		this.partInstalled = partInstalled;
	}
	/**
	 * @return the filler1
	 */
	public String getFiller1() {
		return filler1;
	}
	/**
	 * @param filler1 the filler1 to set
	 */
	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}
	/**
	 * @return the fifCode
	 */
	public String getFifCode() {
		return fifCode;
	}
	/**
	 * @param fifCode the fifCode to set
	 */
	public void setFifCode(String fifCode) {
		this.fifCode = fifCode;
	}
	/**
	 * @return the filler2
	 */
	public String getFiller2() {
		return filler2;
	}
	/**
	 * @param filler2 the filler2 to set
	 */
	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}
	
	public String getPurchaseContractNumber() {
		return purchaseContractNumber;
	}
	public void setPurchaseContractNumber(String purchaseContractNumber) {
		this.purchaseContractNumber = purchaseContractNumber;
	}

}
