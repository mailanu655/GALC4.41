package com.honda.galc.service.msip.dto.inbound;

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

public class Gpp631Dto implements IPlanCodeDto {
	
	private static final long serialVersionUID = 1L;
	
	private String lotNumber;
	private String mbpn;
	private String hesColor;
	private String productSpecCode;
	private int productionRemainingQty;
	private String productionDate;
	private String productionSequence;
	private int currentOrderStatus;
	private String planCode;
	private String plantCode;
    private String lineNo;
	private String processLocation;
	private String seq;
	private int prodOrderQty;
	private String demandType;
	private String priorityDate;
	private String priorityTime;
	private String ymto;
	private String productionLot;
	private String nextProductionLot;

    public Gpp631Dto() {
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

	public String generateProductionLot(boolean useNextProductionLot) {
		StringBuffer result = new StringBuffer();

		if(plantCode != null && lineNo != null && processLocation != null && lotNumber != null)
			result.append(plantCode).append(lineNo).append(processLocation).append(lotNumber);

		return result.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productionRemainingQty;
		result = prime * result + currentOrderStatus;
		result = prime * result + prodOrderQty;
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((mbpn == null) ? 0 : mbpn.hashCode());
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionSequence == null) ? 0 : productionSequence.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((seq == null) ? 0 : seq.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((priorityDate == null) ? 0 : priorityDate.hashCode());
		result = prime * result + ((priorityTime == null) ? 0 : priorityTime.hashCode());
		result = prime * result + ((ymto == null) ? 0 : ymto.hashCode());
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
		Gpp631Dto other = (Gpp631Dto) obj;
		if (productionRemainingQty != other.productionRemainingQty)
			return false;
		if (currentOrderStatus != other.currentOrderStatus)
			return false;
		if (prodOrderQty != other.prodOrderQty)
			return false;
		if (lotNumber == null) {
			if (other.lotNumber != null)
				return false;
		} else if (!lotNumber.equals(other.lotNumber))
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
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionSequence == null) {
			if (other.productionSequence != null)
				return false;
		} else if (!productionSequence.equals(other.productionSequence))
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
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
		if (seq == null) {
			if (other.seq != null)
				return false;
		} else if (!seq.equals(other.seq))
			return false;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (priorityDate == null) {
			if (other.priorityDate != null)
				return false;
		} else if (!priorityDate.equals(other.priorityDate))
			return false;
		if (priorityTime == null) {
			if (other.priorityTime != null)
				return false;
		} else if (!priorityTime.equals(other.priorityTime))
			return false;
		if (ymto == null) {
			if (other.ymto != null)
				return false;
		} else if (!ymto.equals(other.ymto))
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