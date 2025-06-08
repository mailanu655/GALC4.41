package com.honda.galc.oif.dto;

import java.sql.Date;
import java.sql.Time;

import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>GPP307DTO Class description</h3>
 * <p> GPP307DTO description </p>
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
 * Jan 07, 2014
 *
 *
 */
public class GPP307DTO implements IPlanCodeDTO {
    private String productionLot;
	@GPCSData("PLAN_CD")
    private String planCode;
	@GPCSData("AFAE_L_NO")
    private String lineNo;
	@GPCSData("AFAE_PROC_LOC")
    private String processLocation;
	@GPCSData("PROD_SEQ_NO")
    private String lotNumber;
	@GPCSData("DEMAND_TYPE")
    private String demandType;
	@GPCSData("KD_LOT_NO")
    private String kdLotNumber;
	@GPCSData("MTOC")
    private String productSpecCode;
	@GPCSData("NO_OF_UNIT_CICO")
    private int lotSize;
	@GPCSData("PRI_DATE")
    private Date productionDate;
	@GPCSData("PRI_TIME")
    private Time productionTime;
//    private short lotPassFlag;
//    private String plantCode;
//    private int lotStatus;
	
	
	//new fields
	@GPCSData("MFG_BASIC_PART_NO")
	private String Mbpn;
	
	@GPCSData("PART_CLR_CD")
	private String hesColor;
	
	@GPCSData("NUMBER_OF_REMAIN")
	private String numberRemaining;
	
	private static final long serialVersionUID = 1L;
	
	public String createProductionLot() {
		String result = "";
		if(planCode != null && planCode.length() >= 6 && processLocation != null) {  
			result = planCode.substring(0, 6) + processLocation + lotNumber;
		}
		return result;
	}

	public static enum ROW_PROCESSED {
		P, Y
	};
	
	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = createProductionLot();// productionLot;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
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

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

//	public String getPlantCode() {
//		return plantCode;
//	}
//
//	public void setPlantCode(String plantCode) {
//		this.plantCode = plantCode;
//	}

	public void setProductionTime(Time productionTime) {
		this.productionTime = productionTime;
	}

	public Time getProductionTime() {
		return productionTime;
	}

	/**
	 * @return the mbpn
	 */
	public String getMbpn() {
		return Mbpn;
	}

	/**
	 * @param mbpn the mbpn to set
	 */
	public void setMbpn(String mbpn) {
		Mbpn = mbpn;
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
	 * @return the numberRemaining
	 */
	public String getNumberRemaining() {
		return numberRemaining;
	}

	/**
	 * @param numberRemaining the numberRemaining to set
	 */
	public void setNumberRemaining(String numberRemaining) {
		this.numberRemaining = numberRemaining;
	}

}
