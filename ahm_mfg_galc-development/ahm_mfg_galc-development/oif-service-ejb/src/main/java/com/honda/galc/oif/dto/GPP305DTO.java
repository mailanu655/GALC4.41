package com.honda.galc.oif.dto;

import java.sql.Date;

import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
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
 * Jan 29, 2014
 */

public class GPP305DTO implements IPlanCodeDTO {
	@GPCSData("PROCESS_FLAG")
	private String processFlag;
	@GPCSData("PLAN_CODE")
	private String planCode;
	@GPCSData("PLANT_CODE")
	private String plantCode;
	@GPCSData("LINE_NO")
    private String lineNo;
	@GPCSData("PROCESS_LOCATION")
	private String processLocation;
	@GPCSData("WE_LINE_NO")
    private String lineNoWE;
	@GPCSData("WE_PROCESS_LOCATION")
	private String processLocationWE;
	@GPCSData("PA_LINE_NO")
    private String lineNoPA;
	@GPCSData("PA_PROCESS_LOCATION")
	private String processLocationPA;
	@GPCSData("AF_OFF_AE_OFF_DATE")
    private Date planOffDate;
	@GPCSData("PRODUCTION_SEQUENCE_NUMBER")
	private String lotNumber;
	@GPCSData("DEMAND_TYPE")
    private String demandType;
	@GPCSData("KD_LOT_NUMBER")
	private String kdLotNumber;
	@GPCSData("MODEL_YEAR_CODE")
	private String modelYearCode;
	@GPCSData("MODEL_CODE")
	private String modelCode;
	@GPCSData("MODEL_TYPE_CODE")
	private String modelTypeCode;
	@GPCSData("MODEL_OPTION_CODE")
	private String modelOptionCode;
	@GPCSData("EXT_COLOR_CODE")
	private String exteriorColorCode;
	@GPCSData("INT_COLOR_CODE")
	private String internalColorCode;
	@GPCSData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@GPCSData("PRODUCTION_QUANTITY")
	private int productionQuantity;
	@GPCSData("START_VIN_OR_ENGINE_SERIAL_NO")
	private String startProductId;
	@GPCSData("REMAKE_FLAG")
	private String remakeFlag;
	@GPCSData("STAMPING_FLAG")
	private String stampingFlag;
	@GPCSData("BUILD_SEQUENCE_NUMBER")
	private String buildSequenceNumber;
	@GPCSData("CARRY_IN_OUT_FLAG")
	private String carryInOutFlag;
	@GPCSData("NUMBER_OF_UNITS_CARRY_IN_OUT")
	private int numberOfUnitsCarryInOut;

	private String productionLot;
	private String nextProductionLot;
	
	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
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

	public String getLineNoWE() {
		return lineNoWE;
	}

	public void setLineNoWE(String lineNoWE) {
		this.lineNoWE = lineNoWE;
	}

	public String getProcessLocationWE() {
		return processLocationWE;
	}

	public void setProcessLocationWE(String processLocationWE) {
		this.processLocationWE = processLocationWE;
	}

	public String getLineNoPA() {
		return lineNoPA;
	}

	public void setLineNoPA(String lineNoPA) {
		this.lineNoPA = lineNoPA;
	}

	public String getProcessLocationPA() {
		return processLocationPA;
	}

	public void setProcessLocationPA(String processLocationPA) {
		this.processLocationPA = processLocationPA;
	}

	public Date getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
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

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getExteriorColorCode() {
		return exteriorColorCode;
	}

	public void setExteriorColorCode(String exteriorColorCode) {
		this.exteriorColorCode = exteriorColorCode;
	}

	public String getInternalColorCode() {
		return internalColorCode;
	}

	public void setInternalColorCode(String internalColorCode) {
		this.internalColorCode = internalColorCode;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public int getProductionQuantity() {
		return productionQuantity;
	}

	public void setProductionQuantity(int productionQuantity) {
		this.productionQuantity = productionQuantity;
	}

	public String getStartProductId() {
		return startProductId;
	}

	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}

	public String getRemakeFlag() {
		return remakeFlag;
	}

	public void setRemakeFlag(String remakeFlag) {
		this.remakeFlag = remakeFlag;
	}

	public String getStampingFlag() {
		return stampingFlag;
	}

	public void setStampingFlag(String stampingFlag) {
		this.stampingFlag = stampingFlag;
	}

	public int getNumberOfUnitsCarryInOut() {
		return numberOfUnitsCarryInOut;
	}

	public void setNumberOfUnitsCarryInOut(int numberOfUnitsCarryInOut) {
		this.numberOfUnitsCarryInOut = numberOfUnitsCarryInOut;
	}

	public String getCarryInOutFlag() {
		return carryInOutFlag;
	}

	public void setCarryInOutFlag(String carryInOutFlag) {
		this.carryInOutFlag = carryInOutFlag;
	}

	public String getNextProductionLot() {
		return nextProductionLot;
	}

	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}

    public GPP305DTO() {
    }
    
    public String getProductionLot() {
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	
	public void setBuildSequenceNumber(String buildSequenceNumber) {
		this.buildSequenceNumber = buildSequenceNumber;
	}

	public String getBuildSequenceNumber() {
		return buildSequenceNumber;
	}

	public PreProductionLot derivePreProductionLot() {
		PreProductionLot preProductionLot = new PreProductionLot();
//		preProductionLot.setProductionLot(productionLot);
//		preProductionLot.setNextProductionLot(nextProductionLot);
		preProductionLot.setPlantCode(planCode.substring(1,3));
		preProductionLot.setLineNo(planCode.substring(5,2));
		preProductionLot.setProcessLocation(processLocation);
		preProductionLot.setProductSpecCode(productSpecCode);
		preProductionLot.setStartProductId(startProductId);
		preProductionLot.setKdLotNumber(kdLotNumber);
		preProductionLot.setLotNumber(lotNumber);
		preProductionLot.setBuildSequenceNumber(buildSequenceNumber);		
		preProductionLot.setHoldStatus(HoldStatus.ON_HOLD.getId());
		preProductionLot.setRemakeFlag(remakeFlag);
		preProductionLot.setDemandType(demandType);
		return preProductionLot;
	}

	public ProductionLot deriveProductionLot() {
		ProductionLot productionLot = new ProductionLot();
//		productionLot.setProductionLot(productionLot);
//		preProductionLot.setNextProductionLot(nextProductionLot);
		productionLot.setPlanCode(planCode);
		productionLot.setPlantCode(planCode.substring(1,3));
		productionLot.setLineNo(planCode.substring(5,2));
		productionLot.setProcessLocation(processLocation);
		productionLot.setProductSpecCode(productSpecCode);
		productionLot.setStartProductId(startProductId);
		productionLot.setLotNumber(lotNumber);
		productionLot.setKdLotNumber(kdLotNumber);
		return productionLot;
	}

	public String createProductionLot() {
		String result = "";
		if(planCode != null && planCode.length() >= 6 && processLocation != null) {  
			result = planCode.substring(0, 6) + processLocation + lotNumber;
		}
		return result;
	}

	public String generateProductionLot() {
		StringBuffer result = new StringBuffer();
		if(plantCode != null && lineNo != null && processLocation != null && lotNumber != null) {  
			result.append(plantCode).append(lineNo).append(processLocation).append(lotNumber);
		}
		return result.toString();
	}

}