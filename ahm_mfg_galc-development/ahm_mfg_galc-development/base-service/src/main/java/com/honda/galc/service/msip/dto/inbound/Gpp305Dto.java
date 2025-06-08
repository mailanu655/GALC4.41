package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.util.ToStringUtil;

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
 * @author Anusha Gopalan
 * @date May, 2017
 */

public class Gpp305Dto implements IPlanCodeDto {
	
	private static final long serialVersionUID = 1L;
	
	private String processFlag;
	private String planCode;
	private String plantCode;
    private String lineNo;
	private String processLocation;
    private String lineNoWE;
	private String processLocationWE;
    private String lineNoPA;
	private String processLocationPA;
    private String planOffDate;
	private String lotNumber;
    private String demandType;
	private String kdLotNumber;
	private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String exteriorColorCode;
	private String internalColorCode;
	private String productSpecCode;
	private int productionQuantity;
	private String startProductId;
	private String remakeFlag;
	private String stampingFlag;
	private String buildSequenceNumber;
	private String carryInOutFlag;
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

	public String getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(String planOffDate) {
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

    public Gpp305Dto() {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productionQuantity;
		result = prime * result + numberOfUnitsCarryInOut;
		result = prime * result + ((processFlag == null) ? 0 : processFlag.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((lineNoWE == null) ? 0 : lineNoWE.hashCode());
		result = prime * result + ((processLocationWE == null) ? 0 : processLocationWE.hashCode());
		result = prime * result + ((lineNoPA == null) ? 0 : lineNoPA.hashCode());
		result = prime * result + ((processLocationPA == null) ? 0 : processLocationPA.hashCode());
		result = prime * result + ((planOffDate == null) ? 0 : planOffDate.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((exteriorColorCode == null) ? 0 : exteriorColorCode.hashCode());
		result = prime * result + ((internalColorCode == null) ? 0 : internalColorCode.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((remakeFlag == null) ? 0 : remakeFlag.hashCode());
		result = prime * result + ((stampingFlag == null) ? 0 : stampingFlag.hashCode());
		result = prime * result + ((buildSequenceNumber == null) ? 0 : buildSequenceNumber.hashCode());
		result = prime * result + ((carryInOutFlag == null) ? 0 : carryInOutFlag.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((nextProductionLot == null) ? 0 : nextProductionLot.hashCode());
		
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
		Gpp305Dto other = (Gpp305Dto) obj;
		if (productionQuantity != other.productionQuantity)
			return false;
		if (numberOfUnitsCarryInOut != other.numberOfUnitsCarryInOut)
			return false;
		if (processFlag == null) {
			if (other.processFlag != null)
				return false;
		} else if (!processFlag.equals(other.processFlag))
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (lineNoWE == null) {
			if (other.lineNoWE != null)
				return false;
		} else if (!lineNoWE.equals(other.lineNoWE))
			return false;
		if (processLocationWE == null) {
			if (other.processLocationWE != null)
				return false;
		} else if (!processLocationWE.equals(other.processLocationWE))
			return false;
		if (lineNoPA == null) {
			if (other.lineNoPA != null)
				return false;
		} else if (!lineNoPA.equals(other.lineNoPA))
			return false;
		if (processLocationPA == null) {
			if (other.processLocationPA != null)
				return false;
		} else if (!processLocationPA.equals(other.processLocationPA))
			return false;
		if (planOffDate == null) {
			if (other.planOffDate != null)
				return false;
		} else if (!planOffDate.equals(other.planOffDate))
			return false;
		if (lotNumber == null) {
			if (other.lotNumber != null)
				return false;
		} else if (!lotNumber.equals(other.lotNumber))
			return false;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (exteriorColorCode == null) {
			if (other.exteriorColorCode != null)
				return false;
		} else if (!exteriorColorCode.equals(other.exteriorColorCode))
			return false;
		if (internalColorCode == null) {
			if (other.internalColorCode != null)
				return false;
		} else if (!internalColorCode.equals(other.internalColorCode))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (startProductId == null) {
			if (other.startProductId != null)
				return false;
		} else if (!startProductId.equals(other.startProductId))
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
		if (buildSequenceNumber == null) {
			if (other.buildSequenceNumber != null)
				return false;
		} else if (!buildSequenceNumber.equals(other.buildSequenceNumber))
			return false;
		if (carryInOutFlag == null) {
			if (other.carryInOutFlag != null)
				return false;
		} else if (!carryInOutFlag.equals(other.carryInOutFlag))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (nextProductionLot == null) {
			if (other.nextProductionLot != null)
				return false;
		} else if (!nextProductionLot.equals(other.nextProductionLot))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}