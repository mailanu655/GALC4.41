package com.honda.ahm.lc.model;

public class ShippingTransaction extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	
	private String vin;
	
	private Character sendFlag;
	
	private String dateString;
	
	private String time;
	
	private String sendLocation;
	
	private String tranType;
	
	private String salesModelCode;
	
	private String salesModelTypeCode;
	
	private String salesModelOptionCode;
	
	private String salesModelColorCode;
	
	private String engineNumber;
	
	private String keyNumber;
	
	private String cicIssuData;
	
	private String adcProcessCode;
	
	private String lineNumber;
	
	private String productionDate;
	
	private String productionSequenceNumber;
	
	private String productionSuffix;
	
	private String kdLotLineNumber;
	
	private String kdLotDate;
	
	private String kdLotSequenceNumber;
	
	private String kdLotSuffix;
	
	private String priceString;
	
	private String vechicleUnitId;

	private String vechicleCommonUnitId;
	
	private Character printedFlag;

	private String afOffDate;
	
	private String cccRegNbr;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Character getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(Character sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSendLocation() {
		return sendLocation;
	}

	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSalesModelOptionCode() {
		return salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getSalesModelColorCode() {
		return salesModelColorCode;
	}

	public void setSalesModelColorCode(String salesModelColorCode) {
		this.salesModelColorCode = salesModelColorCode;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}

	public String getCicIssuData() {
		return cicIssuData;
	}

	public void setCicIssuData(String cicIssuData) {
		this.cicIssuData = cicIssuData;
	}

	public String getAdcProcessCode() {
		return adcProcessCode;
	}

	public void setAdcProcessCode(String adcProcessCode) {
		this.adcProcessCode = adcProcessCode;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductionSequenceNumber() {
		return productionSequenceNumber;
	}

	public void setProductionSequenceNumber(String productionSequenceNumber) {
		this.productionSequenceNumber = productionSequenceNumber;
	}

	public String getProductionSuffix() {
		return productionSuffix;
	}

	public void setProductionSuffix(String productionSuffix) {
		this.productionSuffix = productionSuffix;
	}

	public String getKdLotLineNumber() {
		return kdLotLineNumber;
	}

	public void setKdLotLineNumber(String kdLotLineNumber) {
		this.kdLotLineNumber = kdLotLineNumber;
	}

	public String getKdLotDate() {
		return kdLotDate;
	}

	public void setKdLotDate(String kdLotDate) {
		this.kdLotDate = kdLotDate;
	}

	public String getKdLotSequenceNumber() {
		return kdLotSequenceNumber;
	}

	public void setKdLotSequenceNumber(String kdLotSequenceNumber) {
		this.kdLotSequenceNumber = kdLotSequenceNumber;
	}

	public String getKdLotSuffix() {
		return kdLotSuffix;
	}

	public void setKdLotSuffix(String kdLotSuffix) {
		this.kdLotSuffix = kdLotSuffix;
	}

	public String getPriceString() {
		return priceString;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public String getVechicleUnitId() {
		return vechicleUnitId;
	}

	public void setVechicleUnitId(String vechicleUnitId) {
		this.vechicleUnitId = vechicleUnitId;
	}

	public String getVechicleCommonUnitId() {
		return vechicleCommonUnitId;
	}

	public void setVechicleCommonUnitId(String vechicleCommonUnitId) {
		this.vechicleCommonUnitId = vechicleCommonUnitId;
	}

	public Character getPrintedFlag() {
		return printedFlag;
	}

	public void setPrintedFlag(Character printedFlag) {
		this.printedFlag = printedFlag;
	}

	public String getAfOffDate() {
		return afOffDate;
	}

	public void setAfOffDate(String afOffDate) {
		this.afOffDate = afOffDate;
	}

	public String getCccRegNbr() {
		return cccRegNbr;
	}

	public void setCccRegNbr(String cccRegNbr) {
		this.cccRegNbr = cccRegNbr;
	}
	
	
}
