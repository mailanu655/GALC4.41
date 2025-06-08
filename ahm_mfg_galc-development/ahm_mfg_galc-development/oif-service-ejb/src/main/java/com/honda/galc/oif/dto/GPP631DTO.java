package com.honda.galc.oif.dto;

import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>GPP306DTO Class description</h3>
 * <p> GPP306DTO description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Larry Karpov<br>
 * Feb 12, 2015
 */

public class GPP631DTO implements IPlanCodeDTO {
	@GPCSData("LOT_NUMBER")
	private String lotNumber;
	@GPCSData("MBPN")
	private String mbpn;
	@GPCSData("HES_COLOR")
	private String hesColor;
	@GPCSData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@GPCSData("PRODUCTION_REMAINING_QTY")
	private int productionRemainingQty;
	@GPCSData("PRODUCTION_DATE")
	private String productionDate;
	@GPCSData("PRODUCTION_SEQUENCE")
	private String productionSequence;
	@GPCSData("CURRENT_ORDER_STATUS")
	private int currentOrderStatus;
	@GPCSData("PLAN_CODE")
	private String planCode;
	@GPCSData("PLANT_CODE")
	private String plantCode;
	@GPCSData("LINE_NO")
    private String lineNo;
	@GPCSData("PROCESS_LOCATION")
	private String processLocation;
	@GPCSData("SEQ")
	private String seq;
	@GPCSData("PROD_ORDER_QTY")
	private int prodOrderQty;
	@GPCSData("DEMAND_TYPE")
	private String demandType;
	@GPCSData("PRIORITY_DATE")
	private String priorityDate;
	@GPCSData("PRIORITY_TIME")
	private String priorityTime;
	@GPCSData("YMTO")
	private String ymto;
	@GPCSData("NOTES")
	private String notes;

	private String productionLot;
	private String nextProductionLot;
	

    public GPP631DTO() {
    }
    
	public String getMbpn() {
		return mbpn;
	}

	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	public String getHesColor() {
		return hesColor;
	}

	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}

	public int getProductionRemainingQty() {
		return productionRemainingQty;
	}

	public void setProductionRemainingQty(int productionRemainingQty) {
		this.productionRemainingQty = productionRemainingQty;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getProdOrderQty() {
		return prodOrderQty;
	}

	public void setProdOrderQty(int prodOrderQty) {
		this.prodOrderQty = prodOrderQty;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getNextProductionLot() {
		return nextProductionLot;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getPriorityDate() {
		return priorityDate;
	}

	public void setPriorityDate(String priorityDate) {
		this.priorityDate = priorityDate;
	}

	public String getPriorityTime() {
		return priorityTime;
	}

	public void setPriorityTime(String priorityTime) {
		this.priorityTime = priorityTime;
	}

	/**
	 * @return the ymto
	 */
	public String getYmto() {
		return ymto;
	}

	/**
	 * @param ymto the ymto to set
	 */
	public void setYmto(String ymto) {
		this.ymto = ymto;
	}

	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}

    public String getProductionLot() {
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductionSequence(String productionSequence) {
		this.productionSequence = productionSequence;
	}

	public int getCurrentOrderStatus() {
		return currentOrderStatus;
	}

	public void setCurrentOrderStatus(int currentOrderStatus) {
		this.currentOrderStatus = currentOrderStatus;
	}

	public String getProductionSequence() {
		return productionSequence;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String generateProductionLot(boolean useNextProductionLot) {
		StringBuffer result = new StringBuffer();

		if(plantCode != null && lineNo != null && processLocation != null && lotNumber != null)
			result.append(plantCode).append(lineNo).append(processLocation).append(lotNumber);

		return result.toString();
	}

}