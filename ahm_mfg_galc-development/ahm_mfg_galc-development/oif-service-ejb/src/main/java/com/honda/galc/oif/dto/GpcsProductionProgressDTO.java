package com.honda.galc.oif.dto;


import com.honda.galc.util.OutputData;

public class GpcsProductionProgressDTO implements IOutputFormat {

	@OutputData("PLAN_CODE")
	private String planCode;
	
	@OutputData("LINE_NO")
	private String lineNo;
	
	@OutputData("PROCESS_LOCATION")
	private String processLocation;
	
	@OutputData("ON_OFF_FLAG")
	private String onOffFlag;
		
	@OutputData("KD_LOT_NO") //ACTUAL TIMESTAMP
	private String kdLotNo;
	
	@OutputData("PROD_SEQ_NO")
	private String prodSeqNo;

	@OutputData("MBPN")
	private String mbpn;
	
	@OutputData("HES_COLOR")
	private String hesColor;
	
	@OutputData("MTOC")
	private String mtoc;
	
	@OutputData("RESULT_QTY")
	private String resultQty;
	
	@OutputData("CREATED_DATE")
	private String createdDate;
	
	//this is get from gal217
	@OutputData("CREATED_TIME")
	private String createdTime;
	
	@OutputData("MINUS_FLAG")
	private String minusFlag;
		
	@OutputData("FILLER")
	private String filler;
	
	@OutputData("PRODUCTION_QUANTITY")
	private String productionQty;

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
	 * @return the processLocation
	 */
	public String getProcessLocation() {
		return processLocation;
	}

	/**
	 * @param processLocation the processLocation to set
	 */
	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	/**
	 * @return the onOffFlag
	 */
	public String getOnOffFlag() {
		return onOffFlag;
	}

	/**
	 * @param onOffFlag the onOffFlag to set
	 */
	public void setOnOffFlag(String onOffFlag) {
		this.onOffFlag = onOffFlag;
	}

	/**
	 * @return the kdLotNo
	 */
	public String getKdLotNo() {
		return kdLotNo;
	}

	/**
	 * @param kdLotNo the kdLotNo to set
	 */
	public void setKdLotNo(String kdLotNo) {
		this.kdLotNo = kdLotNo;
	}

	/**
	 * @return the prodSeqNo
	 */
	public String getProdSeqNo() {
		return prodSeqNo;
	}

	/**
	 * @param prodSeqNo the prodSeqNo to set
	 */
	public void setProdSeqNo(String prodSeqNo) {
		this.prodSeqNo = prodSeqNo;
	}

	/**
	 * @return the mbpn
	 */
	public String getMbpn() {
		return mbpn;
	}

	/**
	 * @param mbpn the mbpn to set
	 */
	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	/**
	 * @return the hesColor
	 */
	public String getHesColor() {
		return hesColor;
	}

	/**
	 * @param hesColor the hesColor to set
	 */
	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}

	/**
	 * @return the mtoc
	 */
	public String getMtoc() {
		return mtoc;
	}

	/**
	 * @param mtoc the mtoc to set
	 */
	public void setMtoc(String mtoc) {
		this.mtoc = mtoc;
	}

	/**
	 * @return the resultQty
	 */
	public String getResultQty() {
		return resultQty;
	}

	/**
	 * @param resultQty the resultQty to set
	 */
	public void setResultQty(String resultQty) {
		this.resultQty = resultQty;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the minusFlag
	 */
	public String getMinusFlag() {
		return minusFlag;
	}

	/**
	 * @param minusFlag the minusFlag to set
	 */
	public void setMinusFlag(String minusFlag) {
		this.minusFlag = minusFlag;
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
	 * @return the productionQty
	 */
	public String getProductionQty() {
		return productionQty;
	}

	/**
	 * @param productionQty the productionQty to set
	 */
	public void setProductionQty(String productionQty) {
		this.productionQty = productionQty;
	}
	
	
}