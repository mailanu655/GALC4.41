package com.honda.galc.oif.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.honda.galc.entity.product.PreProductionLot;

public class PreProductionLotDTO extends BaseProductionLotDTO {
	private String planCode;
	private String startProductId;
	private int sendStatusId;
	private int stampedCount;
	private int holdStatus;
	private Timestamp sentTimestamp;
	private String nextProductionLot;
    private String partNumber;
    private String kdLotNumber;
    private double sequence;
    private String mbpn;
    private String hesColor;
    private String buildSequenceNumber;
    private String productionDate;
    private Character buildSequenceNotFixedFlag;
    private String demandType;    
    private String remakeFlag;
    private String stampingFlag;
    private String carryInOutFlag;
    private int numberOfUnitsCarryInOut;
    private String notes;
    private Date planOffDate;
    
	PreProductionLotDTO() {
    }
    
    public PreProductionLotDTO(GPP306DTO dto306) {
		String strProductionLot = dto306.createProductionLot();
    	this.productionLot = strProductionLot;
    	this.planCode = dto306.getPlanCode();
    	this.lotSize = dto306.getLotSize();
    	this.startProductId = dto306.getStartProductId();
    	this.sendStatusId = 0;
    	this.stampedCount = 0;
    	this.holdStatus = 1;
    	this.lotNumber = dto306.getLotNumber();
    	this.processLocation = dto306.getProcessLocation();
    	this.productSpecCode = dto306.getProductSpecCode();
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
    	this.kdLotNumber	= dto306.getKdLotNumber();
    	this.mbpn			= dto306.getMbpn();
    	this.hesColor		= dto306.getHesColor();
    }
    
    public PreProductionLotDTO(GPP307DTO gpp307dto)
    {
    	this.productionLot =gpp307dto.createProductionLot();
		this.lineNo=gpp307dto.getLineNo();
		this.processLocation = gpp307dto.getProcessLocation();
		this.lotNumber = gpp307dto.getLotNumber();
		this.kdLotNumber = gpp307dto.getKdLotNumber();
		this.mbpn = gpp307dto.getMbpn();
		this.hesColor = gpp307dto.getHesColor();
		this.productSpecCode = gpp307dto.getProductSpecCode();
		this.lotSize = gpp307dto.getLotSize();
		this.planCode = gpp307dto.getPlanCode();
		if(planCode != null) {
        	if(planCode.length() > 3) {
        		String plantCode = planCode.substring(0, 3);
            	this.plantCode = plantCode;
        	}        
    	}
    }

    public PreProductionLotDTO(GPP305DTO dto305) {
		String strProductionLot = dto305.createProductionLot();
    	this.productionLot = strProductionLot;
    	this.nextProductionLot = null;
    	this.plantCode = dto305.getPlantCode();
    	this.planCode = dto305.getPlanCode();
    	this.lotSize = dto305.getProductionQuantity();
    	this.startProductId = dto305.getStartProductId();
    	this.sendStatusId = 0;
    	this.stampedCount = 0;
    	this.holdStatus = 1;
    	this.lotNumber = dto305.getLotNumber();
    	this.processLocation = dto305.getProcessLocation();
    	this.productSpecCode = dto305.getProductSpecCode();
    	this.lineNo = dto305.getLineNo();
    	this.kdLotNumber = dto305.getKdLotNumber();
    	this.buildSequenceNumber = dto305.getBuildSequenceNumber(); 
    	this.buildSequenceNotFixedFlag ='N';
    	this.demandType = dto305.getDemandType();
    	this.remakeFlag = dto305.getRemakeFlag();
    	this.stampingFlag = dto305.getStampingFlag();
    	this.carryInOutFlag = dto305.getCarryInOutFlag();
    	this.numberOfUnitsCarryInOut = dto305.getNumberOfUnitsCarryInOut();
    	this.planOffDate = dto305.getPlanOffDate();
    }
    
    public PreProductionLotDTO(PreProductionLot ppl) {
    	this.productionLot = ppl.getProductionLot();
    	this.lotSize = ppl.getLotSize();
    	this.startProductId = ppl.getStartProductId();
    	this.sendStatusId = ppl.getSendStatusId();
    	this.stampedCount = ppl.getStampedCount();
    	this.holdStatus = ppl.getHoldStatus();
    	this.sentTimestamp = ppl.getSentTimestamp();
    	this.nextProductionLot = ppl.getNextProductionLot();
    	this.lotNumber = ppl.getLotNumber();
    	this.planCode = ppl.getPlanCode();
    	this.plantCode = ppl.getPlantCode();
    	this.processLocation = ppl.getProcessLocation();
    	this.lineNo = ppl.getLineNo();
    	this.productSpecCode = ppl.getProductSpecCode();
    	this.partNumber = ppl.getPartNumber();
    	this.mbpn = ppl.getMbpn();
    	this.sequence = ppl.getSequence(); 
    	this.hesColor = ppl.getHesColor();
    	this.kdLotNumber = ppl.getPreProdLotKdLotNumber();
    	this.buildSequenceNotFixedFlag=ppl.getBuildSeqNotFixedFlag();
    	this.demandType = ppl.getDemandType();
    	this.remakeFlag = ppl.getRemakeFlag();
    	this.stampingFlag = ppl.getStampingFlag();
    	this.carryInOutFlag = ppl.getCarryInOutFlag();
    	this.numberOfUnitsCarryInOut = ppl.getCarryInOutUnits();
    	this.notes = ppl.getNotes();
    	this.planOffDate = ppl.getPlanOffDate();
    }

    public PreProductionLotDTO(GPP631DTO dto631, boolean useNextProductionLot) {
		this.productionLot = dto631.generateProductionLot(useNextProductionLot);
    	this.planCode = dto631.getPlanCode();
    	this.lotSize = dto631.getProdOrderQty();
    	this.lotNumber = dto631.getLotNumber();
    	this.processLocation = dto631.getProcessLocation();
    	this.productSpecCode = dto631.getProductSpecCode();
    	this.sequence = 0d;
    	this.planCode = dto631.getPlanCode();
    	this.plantCode = dto631.getPlantCode();
    	this.lineNo = dto631.getLineNo();
    	this.mbpn = dto631.getMbpn();
    	this.hesColor = dto631.getHesColor();
    	this.productionDate = dto631.getProductionDate();
    	this.demandType = dto631.getDemandType();
       	this.notes = dto631.getNotes();
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
	
	public int getSendStatusId() {
		return sendStatusId;
	}
	
	public void setSendStatusId(int sendStatusId) {
		this.sendStatusId = sendStatusId;
	}
	
	public int getStampedCount() {
		return stampedCount;
	}
	
	public void setStampedCount(int stampedCount) {
		this.stampedCount = stampedCount;
	}
	
	public int getHoldStatus() {
		return holdStatus;
	}
	
	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}
	
	public Timestamp getSentTimestamp() {
		return sentTimestamp;
	}
	
	public void setSentTimestamp(Timestamp sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}
	
	public String getNextProductionLot() {
		return nextProductionLot;
	}
	
	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}
	
	public String getPartNumber() {
		return partNumber;
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public double getSequence() {
		return sequence;
	}

	public void setSequence(double sequence) {
		this.sequence = sequence;
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
	 * @return the buildSequenceNumber
	 */
	public String getBuildSequenceNumber() {
		return buildSequenceNumber;
	}

	/**
	 * @param buildSequenceNumber the buildSequenceNumber to set
	 */
	public void setBuildSequenceNumber(String buildSequenceNumber) {
		this.buildSequenceNumber = buildSequenceNumber;
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
	
	

	public Character getBuildSequenceNotFixedFlag() {
		return buildSequenceNotFixedFlag;
	}

	public void setBuildSequenceNotFixedFlag(Character buildSequenceNotFixedFlag) {
		this.buildSequenceNotFixedFlag = buildSequenceNotFixedFlag;
	}
	
	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
	}

	public PreProductionLot derivePreProductionLot() {
		PreProductionLot ppl = new PreProductionLot();
    	ppl.setProductionLot(productionLot);
    	ppl.setLotSize(lotSize);
    	ppl.setStartProductId(startProductId);
    	ppl.setSendStatusId(sendStatusId);
    	ppl.setStampedCount(stampedCount);
    	ppl.setHoldStatus(holdStatus);
    	ppl.setSentTimestamp(sentTimestamp);
    	ppl.setNextProductionLot(nextProductionLot);
    	ppl.setLotNumber(lotNumber);
    	ppl.setPlantCode(plantCode);
    	ppl.setProcessLocation(processLocation);
    	ppl.setLineNo(lineNo);
    	ppl.setProductSpecCode(productSpecCode);
    	ppl.setPartNumber(partNumber);
		ppl.setPlanCode(planCode);
		ppl.setSequence(sequence);
		ppl.setKdLotNumber(kdLotNumber);
		ppl.setMbpn(mbpn);
		ppl.setHesColor(hesColor);
		ppl.setBuildSequenceNumber(buildSequenceNumber);
		ppl.setBuildSeqNotFixedFlag(buildSequenceNotFixedFlag);
		ppl.setDemandType(demandType);
		ppl.setRemakeFlag(remakeFlag);
		ppl.setStampingFlag(stampingFlag);
		ppl.setCarryInOutFlag(carryInOutFlag);
		ppl.setCarryInOutUnits(numberOfUnitsCarryInOut);
		ppl.setNotes(notes);
		ppl.setPlanOffDate(planOffDate);
		return ppl; 
	}
}