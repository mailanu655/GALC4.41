package com.honda.galc.service.msip.dto.inbound;

import java.sql.Timestamp;

import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.util.ToStringUtil;
/*
 * 
 * @author Anusha Gopalan
 * @date June, 2017
*/
public class PreProductionLotDto extends BaseProductionLotDto {
	
	private static final long serialVersionUID = 1L;
	
	private String planCode;
	private String startProductId;
	private int sendStatusId;
	private int stampedCount;
	private int holdStatus;
	private Timestamp sentTimestamp;
	private String nextProductionLot;
	String productSpecCode;
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
    
	PreProductionLotDto() {
    }
    
    public PreProductionLotDto(Gpp306Dto dto306) {
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
    
    public PreProductionLotDto(Gpp307Dto gpp307dto)
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

    public PreProductionLotDto(Gpp305Dto dto305) {
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
    }
    
    public PreProductionLotDto(PreProductionLot ppl) {
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
    }

    public PreProductionLotDto(Gpp631Dto dto631, boolean useNextProductionLot) {
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
		return ppl; 
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(sequence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((sentTimestamp == null) ? 0 : sentTimestamp.hashCode());
		result = prime * result + ((nextProductionLot == null) ? 0 : nextProductionLot.hashCode());
		result = prime * result + ((partNumber == null) ? 0 : partNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((mbpn == null) ? 0 : mbpn.hashCode());
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
		result = prime * result + ((buildSequenceNumber == null) ? 0 : buildSequenceNumber.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((buildSequenceNotFixedFlag == null) ? 0 : buildSequenceNotFixedFlag.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((remakeFlag == null) ? 0 : remakeFlag.hashCode());
		result = prime * result + ((stampingFlag == null) ? 0 : stampingFlag.hashCode());
		result = prime * result + ((carryInOutFlag == null) ? 0 : carryInOutFlag.hashCode());
		result = prime * result + sendStatusId;
		result = prime * result + stampedCount;
		result = prime * result + holdStatus;
		result = prime * result + numberOfUnitsCarryInOut;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreProductionLotDto other = (PreProductionLotDto) obj;
		if (Double.doubleToLongBits(sequence) != Double.doubleToLongBits(other.sequence))
			return false;
		
		if (sendStatusId != other.sendStatusId)
			return false;
		if (stampedCount != other.stampedCount)
			return false;
		if (holdStatus != other.holdStatus)
			return false;
		if (numberOfUnitsCarryInOut != other.numberOfUnitsCarryInOut)
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (startProductId == null) {
			if (other.startProductId != null)
				return false;
		} else if (!startProductId.equals(other.startProductId))
			return false;
		if (sentTimestamp == null) {
			if (other.sentTimestamp != null)
				return false;
		} else if (!sentTimestamp.equals(other.sentTimestamp))
			return false;
		if (nextProductionLot == null) {
			if (other.nextProductionLot != null)
				return false;
		} else if (!nextProductionLot.equals(other.nextProductionLot))
			return false;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (mbpn == null) {
			if (other.mbpn != null)
				return false;
		} else if (!mbpn.equals(other.mbpn))
			return false;
		if (hesColor == null) {
			if (other.hesColor != null)
				return false;
		} else if (!hesColor.equals(other.hesColor))
			return false;
		if (buildSequenceNumber == null) {
			if (other.buildSequenceNumber != null)
				return false;
		} else if (!buildSequenceNumber.equals(other.buildSequenceNumber))
			return false;
		if (buildSequenceNotFixedFlag == null) {
			if (other.buildSequenceNotFixedFlag != null)
				return false;
		} else if (!buildSequenceNotFixedFlag.equals(other.buildSequenceNotFixedFlag))
			return false;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (remakeFlag == null) {
			if (other.remakeFlag != null)
				return false;
		} else if (!remakeFlag.equals(other.remakeFlag))
			return false;
		if (stampingFlag == null) {
			if (other.stampingFlag != null)
				return false;
		} else if (!stampingFlag.equals(other.stampingFlag))
			return false;
		if (carryInOutFlag == null) {
			if (other.carryInOutFlag != null)
				return false;
		} else if (!carryInOutFlag.equals(other.carryInOutFlag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}