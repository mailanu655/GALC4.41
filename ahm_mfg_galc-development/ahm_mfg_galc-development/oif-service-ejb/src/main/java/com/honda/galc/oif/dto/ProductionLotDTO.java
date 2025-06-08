package com.honda.galc.oif.dto;

import java.sql.Date;
import java.text.ParseException;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.util.OIFConstants;

public class ProductionLotDTO extends BaseProductionLotDTO {

	private String planCode;
    private String kdLotNumber;
	private String startProductId;
    private String demandType;
    private Date planOffDate;
    private Date productionDate;
    private short lotPassFlag;
	private int lotStatus;
	private String prodLotKd;
	private String processFlag;
	private String lineNoWE;
	private String processLocationWE;
	private String lineNoPA;
	private String processLocationPA;
	private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String exteriorColorCode;
	private String internalColorCode;
	private String remakeFlag;
	private String stampingFlag;
	private String carryInOutFlag;
	private int numberOfUnitsCarryInOut;

	ProductionLotDTO() {
    }
    
    public ProductionLotDTO(GPP307DTO dto307) {
    	this.productionLot = dto307.getProductionLot();
    	this.planCode = dto307.getPlanCode();
    	this.lotSize = dto307.getLotSize();
    	this.lotNumber = dto307.getLotNumber();
    	this.processLocation = dto307.getProcessLocation();
    	this.productSpecCode = dto307.getProductSpecCode();
    	this.kdLotNumber = dto307.getKdLotNumber();
    	this.demandType = dto307.getDemandType();
    	this.productionDate = dto307.getProductionDate();
    	this.lotNumber = dto307.getLotNumber();
    	String planCode = dto307.getPlanCode();
    	if(planCode != null) {
        	if(planCode.length() > 3) {
        		String plantCode = planCode.substring(0, 3);
            	this.plantCode = plantCode;
        	}
        	if(planCode.length() > 6) {
        		String strLineNo = planCode.substring(4, 6);
            	this.lineNo = strLineNo;
        	}
    	}
    }

    public ProductionLotDTO(GPP306DTO dto306, Logger logger) {
    	this.productionLot = dto306.getProductionLot();
    	this.planCode = dto306.getPlanCode();
    	this.lotSize = dto306.getLotSize();
    	this.startProductId = dto306.getStartProductId();
    	this.lotNumber = dto306.getLotNumber();
    	this.processLocation = dto306.getProcessLocation();
    	this.productSpecCode = dto306.getProductSpecCode();
    	this.kdLotNumber = dto306.getKdLotNumber();
    	String planCode = dto306.getPlanCode();
    	if(planCode != null) {
        	if(planCode.length() > 3) {
        		String plantCode = planCode.substring(0, 3);
            	this.plantCode = plantCode;
        	}
        	if(planCode.length() > 6) {
        		String strLineNo = planCode.substring(4, 6);
            	this.lineNo = strLineNo;
        	}
    	}
    	if(lotNumber != null && lotNumber.length() > 8) {
    		try {
				this.planOffDate = new Date(OIFConstants.sdf1.parse(lotNumber.substring(0,8)).getTime());
				this.productionDate = this.planOffDate;
			} catch (ParseException e) {
				logger.error("Parsing date error from: " + lotNumber);
			}
    	}
    }

    public ProductionLotDTO(GPP305DTO dto305) {
    	this.productionLot = dto305.getProductionLot();
    	this.planCode = dto305.getPlanCode();
    	this.plantCode = dto305.getPlantCode();
    	this.lotSize = dto305.getProductionQuantity();
    	this.startProductId = dto305.getStartProductId();
    	this.lotNumber = dto305.getLotNumber();
    	this.lineNo = dto305.getLineNo();
    	this.processLocation = dto305.getProcessLocation();
    	this.productSpecCode = dto305.getProductSpecCode();
    	this.kdLotNumber = dto305.getKdLotNumber();
    	this.demandType = dto305.getDemandType();
    	this.productionDate = dto305.getPlanOffDate();
    	this.processFlag = dto305.getProcessFlag();
    	this.lineNoWE = dto305.getLineNoWE();
    	this.processLocationWE = dto305.getProcessLocationWE();
    	this.lineNoPA = dto305.getLineNoPA();
    	this.processLocationPA = dto305.getProcessLocationPA();
    	this.modelYearCode = dto305.getModelYearCode();
    	this.modelCode = dto305.getModelCode();
    	this.modelTypeCode = dto305.getModelTypeCode();
    	this.modelOptionCode = dto305.getModelOptionCode();
    	this.exteriorColorCode = dto305.getExteriorColorCode();
    	this.internalColorCode = dto305.getInternalColorCode();
    	this.remakeFlag = dto305.getRemakeFlag();
    	this.stampingFlag = dto305.getStampingFlag();
    	this.carryInOutFlag = dto305.getCarryInOutFlag();
    	this.numberOfUnitsCarryInOut = dto305.getNumberOfUnitsCarryInOut();
    }

    public ProductionLotDTO(GPP631DTO dto631) {
    	this.productionLot = dto631.getProductionLot();
    	this.planCode = dto631.getPlanCode();
    	this.plantCode = dto631.getPlantCode();
    	this.lotNumber = dto631.getLotNumber();
    	this.lineNo = dto631.getLineNo();
    	this.processLocation = dto631.getProcessLocation();
    	this.productSpecCode = dto631.getProductSpecCode();
    	this.demandType = dto631.getDemandType();
    	//this.lotSize = dto631.getProdOrderQty();
    	String prodDate = dto631.getProductionDate();
    	if(prodDate != null && prodDate.length() == 8) {
    		try {
				this.productionDate = new Date(OIFConstants.sdf1.parse(prodDate).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    }

    public ProductionLot deriveProductionLot() {
		ProductionLot pl = new ProductionLot();
    	pl.setProductionLot(productionLot);
    	pl.setLotSize(lotSize);
    	pl.setStartProductId(startProductId);
    	pl.setDemandType(demandType);
    	pl.setKdLotNumber(kdLotNumber);
    	pl.setPlantCode(plantCode);
    	pl.setLineNo(lineNo);
    	pl.setProcessLocation(processLocation);
    	pl.setProductSpecCode(productSpecCode);
    	pl.setPlanCode(planCode);
    	pl.setLotNumber(lotNumber);
		pl.setPlanOffDate(productionDate);
		pl.setProductionDate(productionDate);
		pl.setProdLotKd(prodLotKd);
		pl.setDemandType(demandType);
    	pl.setWeLineNo(lineNoWE);
    	pl.setWeProcessLocation(processLocationWE);
    	pl.setPaLineNo(lineNoPA);
    	pl.setPaProcessLocation(processLocationPA);
		return pl; 
	}

    public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public Date getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public short getLotPassFlag() {
		return lotPassFlag;
	}

	public void setLotPassFlag(short lotPassFlag) {
		this.lotPassFlag = lotPassFlag;
	}

	public int getLotStatus() {
		return lotStatus;
	}

	public void setLotStatus(int lotStatus) {
		this.lotStatus = lotStatus;
	}

	public String getProdLotKd() {
		return prodLotKd;
	}

	public void setProdLotKd(String prodLotKd) {
		this.prodLotKd = prodLotKd;
	}

    public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

    public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanCode() {
		return planCode;
	}

	public String getStartProductId() {
		return startProductId;
	}
	
	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public String getProcessFlag() {
		return processFlag;
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

	public String getCarryInOutFlag() {
		return carryInOutFlag;
	}

	public void setCarryInOutFlag(String carryInOutFlag) {
		this.carryInOutFlag = carryInOutFlag;
	}

	public int getNumberOfUnitsCarryInOut() {
		return numberOfUnitsCarryInOut;
	}

	public void setNumberOfUnitsCarryInOut(int numberOfUnitsCarryInOut) {
		this.numberOfUnitsCarryInOut = numberOfUnitsCarryInOut;
	}
	
}