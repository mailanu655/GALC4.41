package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * Entity to save the shipping to American Honda.
 * Have all the information necessary to send the YMS system.
 * RMT OIF 50A Processing
 * @author vjc80020
 *
 */
@Entity
@Table ( name = "GAL148TBX" )
public class ShippingTransaction extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column ( name = "VIN" )
	private String vin;
	@Column ( name = "SENDED_FLG" )
	private Character sendFlag;
	@Column ( name = "DATE_STRING" )
	private String dateString;
	@Column ( name = "TIME" )
	private String time;
	@Column ( name = "SEND_LOCATION" )
	private String sendLocation;
	@Column ( name = "TRAN_TYPE" )
	private String tranType;
	@Column ( name = "SALES_MODEL_CODE" )
	private String salesModelCode;
	@Column ( name = "SALES_MODEL_TYPE_CODE" )
	private String salesModelTypeCode;
	@Column ( name = "SALES_MODEL_OPTION_CODE" )
	private String salesModelOptionCode;
	@Column ( name = "SALES_MODEL_COLOR_CODE" )
	private String salesModelColorCode;
	@Column ( name = "ENGINE_NO" )
	private String engineNumber;
	@Column ( name = "KEY_NO" )
	private String keyNumber;
	@Column ( name = "CIC_ISSU_DATA" )
	private String cicIssuData;
	@Column ( name = "ADC_PROCESS_CODE" )
	private String adcProcessCode;
	@Column ( name = "PRODUCTION_LINE_NO" )
	private String lineNumber;
	@Column ( name = "PRODUCTION_DATE_STRING" )
	private String productionDate;
	@Column ( name = "PRODUCTION_SEQUENCE" )
	private String productionSequenceNumber;
	@Column ( name = "PRODUCTION_SUFFIX" )
	private String productionSuffix;
	@Column ( name = "KD_LINE_NO" )
	private String kdLotLineNumber;
	@Column ( name = "KD_DATE" )
	private String kdLotDate;
	@Column ( name = "KD_SEQ_NO" )
	private String kdLotSequenceNumber;
	@Column ( name = "KD_SUFFIX" )
	private String kdLotSuffix;
	@Column ( name = "PRICE_STRING" )
	private String priceString;
	@Column ( name = "VEHICLE_UNIT_ID" )
	private String vechicleUnitId;
	@Column ( name = "VEHICLE_COMM_UNIT" )
	private String vechicleCommonUnitId;
	@Column ( name = "PRINTED_FLAG" )
	private Character printedFlag;
	@Column ( name = "AF_OFF_DATE" )
	private String afOffDate;
	@Column ( name = "CCC_REG_NBR" )
	private String cccRegNbr;
	
	//Default constructor
	public ShippingTransaction() 
	{
		super();
	}

	public ShippingTransaction(String vin, Character sendFlag,
			String dateString, String time, String sendLocation,
			String tranType, String salesModelCode, String salesModelTypeCode,
			String salesModelOptionCode, String salesModelColorCode,
			String engineNumber, String keyNumber, String cicIssuData,
			String adcProcessCode, String lineNumber, String productionDate,
			String productionSequenceNumber, String productionSuffix,
			String kdLotLineNumber, String kdLotDate,
			String kdLotSequenceNumber, String kdLotSuffix, String priceString,
			String vechicleUnitId, String vechicleCommonUnitId,
			Character printedFlag, String afOffDate, String cccRegNbr) {
		super();
		this.vin = vin;
		this.sendFlag = sendFlag;
		this.dateString = dateString;
		this.time = time;
		this.sendLocation = sendLocation;
		this.tranType = tranType;
		this.salesModelCode = salesModelCode;
		this.salesModelTypeCode = salesModelTypeCode;
		this.salesModelOptionCode = salesModelOptionCode;
		this.salesModelColorCode = salesModelColorCode;
		this.engineNumber = engineNumber;
		this.keyNumber = keyNumber;
		this.cicIssuData = cicIssuData;
		this.adcProcessCode = adcProcessCode;
		this.lineNumber = lineNumber;
		this.productionDate = productionDate;
		this.productionSequenceNumber = productionSequenceNumber;
		this.productionSuffix = productionSuffix;
		this.kdLotLineNumber = kdLotLineNumber;
		this.kdLotDate = kdLotDate;
		this.kdLotSequenceNumber = kdLotSequenceNumber;
		this.kdLotSuffix = kdLotSuffix;
		this.priceString = priceString;
		this.vechicleUnitId = vechicleUnitId;
		this.vechicleCommonUnitId = vechicleCommonUnitId;
		this.printedFlag = printedFlag;
		this.afOffDate = afOffDate;
		this.cccRegNbr = cccRegNbr;
	}

	/**
	 * @return the vin
	 */
	public String getVin() {
		return StringUtils.trim( vin );
	}

	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the sendFlag
	 */
	public Character getSendFlag() {
		return sendFlag;
	}

	/**
	 * @param sendFlag the sendFlag to set
	 */
	public void setSendFlag(Character sendFlag) {
		this.sendFlag = sendFlag;
	}

	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return StringUtils.trim( dateString );
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
		return StringUtils.trim( time );
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
		return StringUtils.trim( sendLocation );
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
		return StringUtils.trim( tranType );
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
		return StringUtils.trim( salesModelCode );
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
		return StringUtils.trim( salesModelTypeCode );
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
		return StringUtils.trim( salesModelOptionCode );
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
		return StringUtils.trim( salesModelColorCode );
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
		return StringUtils.trim( engineNumber );
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
		return StringUtils.trim( keyNumber );
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
		return StringUtils.trim( cicIssuData );
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
		return StringUtils.trim( adcProcessCode );
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
		return StringUtils.trim( lineNumber );
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
		return StringUtils.trim( productionDate );
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
		return StringUtils.trim( productionSequenceNumber );
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
		return StringUtils.trim( productionSuffix );
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
		return StringUtils.trim( kdLotLineNumber );
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
		return StringUtils.trim( kdLotDate );
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
		return StringUtils.trim( kdLotSequenceNumber );
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
		return StringUtils.trim( kdLotSuffix );
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
		return StringUtils.trim( priceString );
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
		return StringUtils.trim( vechicleUnitId );
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
		return StringUtils.trim( vechicleCommonUnitId );
	}

	/**
	 * @param vechicleCommonUnitId the vechicleCommonUnitId to set
	 */
	public void setVechicleCommonUnitId(String vechicleCommonUnitId) {
		this.vechicleCommonUnitId = vechicleCommonUnitId;
	}

	/**
	 * @return the printedFlag
	 */
	public Character getPrintedFlag() {
		return printedFlag;
	}

	/**
	 * @param printedFlag the printedFlag to set
	 */
	public void setPrintedFlag(Character printedFlag) {
		this.printedFlag = printedFlag;
	}

	/**
	 * @return the afOffDate
	 */
	public String getAfOffDate() {
		return StringUtils.trim( afOffDate );
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
		return StringUtils.trim( cccRegNbr );
	}

	/**
	 * @param cccRegNbr the cccRegNbr to set
	 */
	public void setCccRegNbr(String cccRegNbr) {
		this.cccRegNbr = cccRegNbr;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.entity.IEntity#getId()
	 */
	public String getId() {
		return getVin();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString( vin, keyNumber, kdLotSequenceNumber, cccRegNbr );
	}

}
