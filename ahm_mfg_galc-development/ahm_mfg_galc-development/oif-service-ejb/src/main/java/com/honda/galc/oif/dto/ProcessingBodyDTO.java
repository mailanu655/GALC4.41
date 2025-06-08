package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

/**
 * Class to mapping the layout for Processing Body GPCS interface GIV706.
 *
 */
public class ProcessingBodyDTO implements IOutputFormat {

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
	@OutputData( "FILLER" )
	private String	filler;
	@OutputData( "ON_SEQUENCE_NUMBER" )
	private String	onSequenceNumber;
	
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
	/**
	 * @return the onSequenceNumber
	 */
	public String getOnSequenceNumber() {
		return onSequenceNumber;
	}
	/**
	 * @param onSequenceNumber the onSequenceNumber to set
	 */
	public void setOnSequenceNumber(String onSequenceNumber) {
		this.onSequenceNumber = onSequenceNumber;
	}
}
