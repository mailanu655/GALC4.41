package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;


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
 * @author Anusha Gopalan
 * @date May, 2017
 *
 *
 */
public class Gpp307Dto implements IPlanCodeDto {
    private String productionLot;
    private String planCode;
    private String lineNo;
    private String processLocation;
    private String lotNumber;
    private String demandType;
    private String kdLotNumber;
    private String productSpecCode;
    private int lotSize;
    private String productionDate;
    private String productionTime;
	private String Mbpn;
	private String hesColor;
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

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	public String getProductionTime() {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lotSize;
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionTime == null) ? 0 : productionTime.hashCode());
		result = prime * result + ((Mbpn == null) ? 0 : Mbpn.hashCode());
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
		result = prime * result + ((numberRemaining == null) ? 0 : numberRemaining.hashCode());
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
		Gpp307Dto other = (Gpp307Dto) obj;
		if (lotSize != other.lotSize)
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
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
		if (productionTime == null) {
			if (other.productionTime != null)
				return false;
		} else if (!productionTime.equals(other.productionTime))
			return false;
		if (numberRemaining == null) {
			if (other.numberRemaining != null)
				return false;
		} else if (!numberRemaining.equals(other.numberRemaining))
			return false;
		if (Mbpn == null) {
			if (other.Mbpn != null)
				return false;
		} else if (!Mbpn.equals(other.Mbpn))
			return false;
		if (hesColor == null) {
			if (other.hesColor != null)
				return false;
		} else if (!hesColor.equals(other.hesColor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}
