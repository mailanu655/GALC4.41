package com.honda.galc.oif.dto;


import com.honda.galc.util.OutputData;

public class TransmissionProductionReportDTO implements IOutputFormat {

	@OutputData("PLAN_CD")
	private String planCode;
	
	@OutputData("MFG_BASIC_PART_NO")
	private String mfgBasicPartNo;
	
	@OutputData("PART_CLR_CD")
	private String partClrCd;
	
	@OutputData("PROD_QTY")
	private String prodQty;
		
	@OutputData("PROD_DT")
	private String prodDate;
	
	@OutputData("PROD_TIME")
	private String prodTime;

	@OutputData("FILLER_1")
	private String filler;

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
	 * @return the mfgBasicPartNo
	 */
	public String getMfgBasicPartNo() {
		return mfgBasicPartNo;
	}

	/**
	 * @param mfgBasicPartNo the mfgBasicPartNo to set
	 */
	public void setMfgBasicPartNo(String mfgBasicPartNo) {
		this.mfgBasicPartNo = mfgBasicPartNo;
	}

	/**
	 * @return the partClrCd
	 */
	public String getPartClrCd() {
		return partClrCd;
	}

	/**
	 * @param partClrCd the partClrCd to set
	 */
	public void setPartClrCd(String partClrCd) {
		this.partClrCd = partClrCd;
	}
	
	/**
	 * @return the prodDate
	 */
	public String getProdDate() {
		return prodDate;
	}

	/**
	 * @param prodDate the prodDate to set
	 */
	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}

	/**
	 * @return the prodTime
	 */
	public String getProdTime() {
		return prodTime;
	}

	/**
	 * @param prodTime the prodTime to set
	 */
	public void setProdTime(String prodTime) {
		this.prodTime = prodTime;
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
	 * @return the prodQty
	 */
	public String getProdQty() {
		return prodQty;
	}

	/**
	 * @param prodQty the prodQty to set
	 */
	public void setProdQty(String prodQty) {
		this.prodQty = prodQty;
	}

	
		
	
}