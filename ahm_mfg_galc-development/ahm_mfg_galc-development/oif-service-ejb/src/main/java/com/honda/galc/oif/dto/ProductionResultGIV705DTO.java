package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

public class ProductionResultGIV705DTO implements IOutputFormat {
	
	@OutputData( "PLAN_CODE" )
	private String	planCode;
	@OutputData( "LINE_NUMBER" )
	private String	lineNumber;
	@OutputData( "PROCESS_LOCATION" )
	private String	inHouseProcessLocation;
	/** Number of product_id */
	@OutputData( "VIN_NUMBER" )
	private String	vinNumber;
	@OutputData( "PRODUCTION_SEQUENCE_NUMBER")
	private String	productionSequenceNumber;
	/** The history actual time stamp the process point off */
	@OutputData( "ACL_ACTUAL_TIMESTAMP" )
	private String	alcActualTimestamp;
	@OutputData( "PRODUCT_SPEC_CODE" )
	private String	productSpecCode;
	@OutputData( "BAND_NUMBER" )
	private String	bandNumber;
	@OutputData( "KD_LOT_NUMBER" )
	private String	kdLotNumber;
	@OutputData( "PART_NUMBER" )
	private String	partNumber;
	@OutputData( "PART_COLOR_CODE")
	private String	partColorCode;
	@OutputData( "BOS_SERIAL_NUMBER" )
	private String	bosSerialNumber;
	@OutputData( "FILLER" )
	private String	filler;
	/** Mandatory Item if Cancel Flag 'Y'
	 * '1'=Cut, '2'=Remake
	 */
	@OutputData( "CANCEL_REASON_CODE")
	private Character	cancelReasonCode;
	@OutputData( "RESULT_FLAG" )
	private Character	resultFlag;
	@OutputData( "CANCEL_FLAG" )
	private Character	cancelFlag;
	
	public ProductionResultGIV705DTO ()
	{
		super();
	}
	
	
	/**
	 * @return the vinNumber
	 */
	public String getVinNumber() {
		return vinNumber;
	}
	/**
	 * @param vinNumber the vinNumber to set
	 */
	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}
	/**
	 * @return the alcActualTimestamp
	 */
	public String getAlcActualTimestamp() {
		return alcActualTimestamp;
	}
	/**
	 * @param alcActualTimestamp the alcActualTimestamp to set
	 */
	public void setAlcActualTimestamp(String alcActualTimestamp) {
		this.alcActualTimestamp = alcActualTimestamp;
	}
	/**
	 * @return the bandNumber
	 */
	public String getBandNumber() {
		return bandNumber;
	}
	/**
	 * @param bandNumber the bandNumber to set
	 */
	public void setBandNumber(String bandNumber) {
		this.bandNumber = bandNumber;
	}
	/**
	 * @return the bosSerialNumber
	 */
	public String getBosSerialNumber() {
		return bosSerialNumber;
	}
	/**
	 * @param bosSerialNumber the bosSerialNumber to set
	 */
	public void setBosSerialNumber(String bosSerialNumber) {
		this.bosSerialNumber = bosSerialNumber;
	}
	/**
	 * @return the resultFlag
	 */
	public Character getResultFlag() {
		return resultFlag;
	}
	/**
	 * @param resultFlag the resultFlag to set
	 */
	public void setResultFlag(Character resultFlag) {
		this.resultFlag = resultFlag;
	}
	/**
	 * @return the cancelReasonCode
	 */
	public Character getCancelReasonCode() {
		return cancelReasonCode;
	}
	/**
	 * @param cancelReasonCode the cancelReasonCode to set
	 */
	public void setCancelReasonCode(Character cancelReasonCode) {
		this.cancelReasonCode = cancelReasonCode;
	}
	/**
	 * @return the cancelFlag
	 */
	public Character getCancelFlag() {
		return cancelFlag;
	}
	/**
	 * @param cancelFlag the cancelFlag to set
	 */
	public void setCancelFlag(Character cancelFlag) {
		this.cancelFlag = cancelFlag;
	}


	/**
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}


	/**
	 * @param planCode the planCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
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
	 * @return the inHouseProcessLocation
	 */
	public String getInHouseProcessLocation() {
		return inHouseProcessLocation;
	}


	/**
	 * @param inHouseProcessLocation the inHouseProcessLocation to set
	 */
	public void setInHouseProcessLocation(String inHouseProcessLocation) {
		this.inHouseProcessLocation = inHouseProcessLocation;
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
	 * @return the productSpecCode
	 */
	public String getProductSpecCode() {
		return productSpecCode;
	}


	/**
	 * @param productSpecCode the productSpecCode to set
	 */
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}


	/**
	 * @return the kdLotNumber
	 */
	public String getKdLotNumber() {
		return kdLotNumber;
	}


	/**
	 * @param kdLotNumber the kdLotNumber to set
	 */
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}


	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}


	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}


	/**
	 * @return the partColorCode
	 */
	public String getPartColorCode() {
		return partColorCode;
	}


	/**
	 * @param partColorCode the partColorCode to set
	 */
	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}


	/**
	 * @return the filler
	 */
	public String getFiller() {
		return filler;
	}


	/**
	 * @param filler the filler to set
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	} 

}
