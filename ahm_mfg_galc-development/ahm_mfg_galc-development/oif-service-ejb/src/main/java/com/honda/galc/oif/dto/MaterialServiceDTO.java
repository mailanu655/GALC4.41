package com.honda.galc.oif.dto;

import java.util.Date;

import com.honda.galc.util.OutputData;

public class MaterialServiceDTO implements IOutputFormat {

	@OutputData("VIN")
	private String productId;
	
	@OutputData("PLAN_CODE")
	private String planCode;
	
	@OutputData("LINE_NO")
	private String lineNo;
	
	@OutputData("PROCESS_POINT_ID")
	private String processPointId;
		
	@OutputData("PASSED_DATE") //ACTUAL TIMESTAMP
	private String productionDate;
	
	@OutputData("PASSED_TIME")
	private String actualTimestamp;

	@OutputData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@OutputData("PRODUCTION_QUANTITY")
	private String lotSize;
	
	@OutputData("ON_SEQ_NO")
	private String onSeqNo;
	
	@OutputData("PRODUCTION_SEQUENCE_NUMBER")
	private String productionLot;
	
	@OutputData("KD_LOT_NUMBER")
	private String kdLotNumber;
	
	//this is get from gal217
	@OutputData("PRODUCTION_DATE")
	private String planOffDate;
	
	@OutputData("SEND_DATE_AND_TIME")
	private String currentTimestamp;
		
	private Character sentFlag;

	@OutputData("PART_NUMBER")
	private String partNumber; 

	
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
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
	 * @return the lineNo
	 */
	public String getLineNo() {
		return lineNo;
	}

	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return processPointId;
	}

	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
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
	 * @return the actualTimestamp
	 */
	public String getActualTimestamp() {
		return actualTimestamp;
	}

	/**
	 * @param actualTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
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
	 * @return the productionLot
	 */
	public String getProductionLot() {
		return productionLot;
	}

	/**
	 * @param productionLot the productionLot to set
	 */
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
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
	 * @return the planOffDate
	 */
	public String getPlanOffDate() {
		return planOffDate;
	}

	/**
	 * @param planOffDate the planOffDate to set
	 */
	public void setPlanOffDate(String planOffDate) {
		this.planOffDate = planOffDate;
	}

	/**
	 * @return the currentTimestamp
	 */
	public String getCurrentTimestamp() {
		return currentTimestamp;
	}

	/**
	 * @param currentTimestamp the currentTimestamp to set
	 */
	public void setCurrentTimestamp(String currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	/**
	 * @return the sentFlag
	 */
	public Character getSentFlag() {
		return sentFlag;
	}

	/**
	 * @param sentFlag the sentFlag to set
	 */
	public void setSentFlag(Character sentFlag) {
		this.sentFlag = sentFlag;
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
	 * @return the lotSize
	 */
	public String getLotSize() {
		return lotSize;
	}

	/**
	 * @param lotSize the lotSize to set
	 */
	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	/**
	 * @return the onSeqNo
	 */
	public String getOnSeqNo() {
		return onSeqNo;
	}

	/**
	 * @param onSeqNo the onSeqNo to set
	 */
	public void setOnSeqNo(String onSeqNo) {
		this.onSeqNo = onSeqNo;
	}

			
}
