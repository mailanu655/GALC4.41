package com.honda.galc.service.msip.dto.inbound;

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
 *
 *
 */
public class Gpp306Dto implements IPlanCodeDto {
	
	private static final long serialVersionUID = 1L;
	
	private String productionLot;
	private String nextProductionLot;
	private String planCode;
	private String processLocation;
	private String lotNumber;
	private String kdLotNumber;
	private String productSpecCode;
	private int lotSize;
	private String startProductId;
	//it is the Part number/MFG-BASIC-PART-NO
	private String mbpn;
	//it is the HES Color/PART CLR CD/Part color code
	private String hesColor;
	
    public Gpp306Dto() {
    }
    
    public String getProductionLot() {
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	
	public int getLotSize() {
		return lotSize;
	}
	
	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}
	
	public String getStartProductId() {
		return startProductId;
	}
	
	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}
	
	public String getNextProductionLot() {
		return nextProductionLot;
	}
	
	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}
	
	public String getLotNumber() {
		return lotNumber;
	}
	
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	
	public String getPlanCode() {
		return planCode;
	}
	
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
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

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public PreProductionLot derivePreProductionLot() {
		PreProductionLot preProductionLot = new PreProductionLot();
		preProductionLot.setPlantCode(planCode.substring(1,3));
		preProductionLot.setLineNo(planCode.substring(5,2));
		preProductionLot.setProcessLocation(processLocation);
		preProductionLot.setProductSpecCode(productSpecCode);
		preProductionLot.setStartProductId(startProductId);
		preProductionLot.setLotSize(lotSize);
		preProductionLot.setLotNumber(lotNumber);
		preProductionLot.setHoldStatus(1);
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
		productionLot.setLotSize(lotSize);
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

	/**
	 * @return the mbpn
	 */
	public String getMbpn() {
		return mbpn;
	}

	/**
	 * @param mbpn the mbpn to set
	 */
	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lotSize;
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((nextProductionLot == null) ? 0 : nextProductionLot.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((mbpn == null) ? 0 : mbpn.hashCode());
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
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
		Gpp306Dto other = (Gpp306Dto) obj;
		if (lotSize != other.lotSize)
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
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
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
		if (startProductId == null) {
			if (other.startProductId != null)
				return false;
		} else if (!startProductId.equals(other.startProductId))
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
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}