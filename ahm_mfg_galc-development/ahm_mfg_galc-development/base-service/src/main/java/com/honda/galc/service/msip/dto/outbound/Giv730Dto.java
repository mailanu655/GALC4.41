package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date Aug 31, 2017
 */
public class Giv730Dto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	private String planCode;
	private String productSpecCode;
	private String productionQuantity;
	private String productionDate;
	private String productionTime;
	private String lotNumber;
	private String filler;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public String getProductionQuantity() {
		return productionQuantity;
	}
	public void setProductionQuantity(String productionQuantity) {
		this.productionQuantity = productionQuantity;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getProductionTime() {
		return productionTime;
	}
	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionQuantity == null) ? 0 : productionQuantity.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionTime == null) ? 0 : productionTime.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
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
		Giv730Dto other = (Giv730Dto) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productionQuantity == null) {
			if (other.productionQuantity != null)
				return false;
		} else if (!productionQuantity.equals(other.productionQuantity))
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
		if (lotNumber == null) {
			if (other.lotNumber != null)
				return false;
		} else if (!lotNumber.equals(other.lotNumber))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

	
	
}
