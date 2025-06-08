package com.honda.galc.service.msip.dto.inbound;

import java.sql.Date;

import com.honda.galc.util.ToStringUtil;
/*
 * 
 * @author Anusha Gopalan
 * @date June, 2017
*/
public class ProductionLotDto extends BaseProductionLotDto {

	private static final long serialVersionUID = 1L;
	
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

	public ProductionLotDto() {
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
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//TODO: lotPassFlag
		result = prime * result + lotStatus;
		result = prime * result + numberOfUnitsCarryInOut;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((planOffDate == null) ? 0 : planOffDate.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((prodLotKd == null) ? 0 : prodLotKd.hashCode());
		result = prime * result + ((processFlag == null) ? 0 : processFlag.hashCode());
		result = prime * result + ((lineNoWE == null) ? 0 : lineNoWE.hashCode());
		
		result = prime * result + ((processLocationWE == null) ? 0 : processLocationWE.hashCode());
		result = prime * result + ((lineNoPA == null) ? 0 : lineNoPA.hashCode());
		result = prime * result + ((processLocationPA == null) ? 0 : processLocationPA.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((exteriorColorCode == null) ? 0 : exteriorColorCode.hashCode());
		result = prime * result + ((internalColorCode == null) ? 0 : internalColorCode.hashCode());
		result = prime * result + ((remakeFlag == null) ? 0 : remakeFlag.hashCode());
		result = prime * result + ((stampingFlag == null) ? 0 : stampingFlag.hashCode());
		result = prime * result + ((carryInOutFlag == null) ? 0 : carryInOutFlag.hashCode());
		
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
		ProductionLotDto other = (ProductionLotDto) obj;
		//TODO:lotPassFlag
		if (lotPassFlag != other.lotPassFlag) 
			return false;
		if (lotStatus != other.lotStatus)
			return false;
		if (numberOfUnitsCarryInOut != other.numberOfUnitsCarryInOut)
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (startProductId == null) {
			if (other.startProductId != null)
				return false;
		} else if (!startProductId.equals(other.startProductId))
			return false;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (planOffDate == null) {
			if (other.planOffDate != null)
				return false;
		} else if (!planOffDate.equals(other.planOffDate))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (prodLotKd == null) {
			if (other.prodLotKd != null)
				return false;
		} else if (!prodLotKd.equals(other.prodLotKd))
			return false;
		if (processFlag == null) {
			if (other.processFlag != null)
				return false;
		} else if (!processFlag.equals(other.processFlag))
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