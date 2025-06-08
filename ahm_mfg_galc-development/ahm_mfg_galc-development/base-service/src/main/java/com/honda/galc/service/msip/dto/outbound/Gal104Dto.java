package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date Aug 18, 2017
 */
public class Gal104Dto extends BaseOutboundDto implements IMsipOutboundDto{
	private static final long serialVersionUID = 1L;
	private String shipperId;
	private String versionNumber;
	private String vin;
	private String condensedKdLot;
	private String condensedProductionLot;
	private String salesModelCode;
	private String salesModelTypeCode;
	private String salesModelOption;
	private String salesExtColorCode;
	private String salesIntColorCode;
	private String manufacturerModelYearCode;
	private String manufacturerModelCode;
	private String manufacturerModelTypeCode;
	private String manufacturerModelOptionCode;
	private String manufacturerExtColorCode;
	private String manufacturerIntColorCode;
	private String expectedShipDate;
	private String lastWorkingDate;
	private String lastWorkingTime;
	private String filler;
	private String errorMsg;
	private Boolean isError;
	
	public Gal104Dto() {
		super();
	}

	public String getShipperId() {
		return shipperId;
	}

	public void setShipperId(String shipperId) {
		this.shipperId = shipperId;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}
	
	public String getSalesModelOption() {
		return salesModelOption;
	}

	public void setSalesModelOption(String salesModelOption) {
		this.salesModelOption = salesModelOption;
	}

	public String getSalesExtColorCode() {
		return salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return salesIntColorCode;
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getCondensedKdLot() {
		return condensedKdLot;
	}

	public void setCondensedKdLot(String condensedKdLot) {
		this.condensedKdLot = condensedKdLot;
	}

	public String getCondensedProductionLot() {
		return condensedProductionLot;
	}

	public void setCondensedProductionLot(String condensedProductionLot) {
		this.condensedProductionLot = condensedProductionLot;
	}

	public String getManufacturerModelYearCode() {
		return manufacturerModelYearCode;
	}

	public void setManufacturerModelYearCode(String manufacturerModelYearCode) {
		this.manufacturerModelYearCode = manufacturerModelYearCode;
	}

	public String getManufacturerModelCode() {
		return manufacturerModelCode;
	}

	public void setManufacturerModelCode(String manufacturerModelCode) {
		this.manufacturerModelCode = manufacturerModelCode;
	}

	public String getManufacturerModelTypeCode() {
		return manufacturerModelTypeCode;
	}

	public void setManufacturerModelTypeCode(String manufacturerModelTypeCode) {
		this.manufacturerModelTypeCode = manufacturerModelTypeCode;
	}

	public String getManufacturerModelOptionCode() {
		return manufacturerModelOptionCode;
	}

	public void setManufacturerModelOptionCode(String manufacturerModelOptionCode) {
		this.manufacturerModelOptionCode = manufacturerModelOptionCode;
	}

	public String getManufacturerExtColorCode() {
		return manufacturerExtColorCode;
	}

	public void setManufacturerExtColorCode(String manufacturerExtColorCode) {
		this.manufacturerExtColorCode = manufacturerExtColorCode;
	}

	public String getManufacturerIntColorCode() {
		return manufacturerIntColorCode;
	}

	public void setManufacturerIntColorCode(String manufacturerIntColorCode) {
		this.manufacturerIntColorCode = manufacturerIntColorCode;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public String getLastWorkingDate() {
		return lastWorkingDate;
	}

	public void setLastWorkingDate(String lastWorkingDate) {
		this.lastWorkingDate = lastWorkingDate;
	}

	public String getLastWorkingTime() {
		return lastWorkingTime;
	}

	public void setLastWorkingTime(String lastWorkingTime) {
		this.lastWorkingTime = lastWorkingTime;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
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

	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shipperId == null) ? 0 : shipperId.hashCode());
		result = prime * result + ((versionNumber == null) ? 0 : versionNumber.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((condensedKdLot == null) ? 0 : condensedKdLot.hashCode());
		result = prime * result + ((condensedProductionLot == null) ? 0 : condensedProductionLot.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((salesModelOption == null) ? 0 : salesModelOption.hashCode());
		result = prime * result + ((salesExtColorCode == null) ? 0 : salesExtColorCode.hashCode());
		result = prime * result + ((salesIntColorCode == null) ? 0 : salesIntColorCode.hashCode());
		result = prime * result + ((manufacturerModelYearCode == null) ? 0 : manufacturerModelYearCode.hashCode());
		result = prime * result + ((manufacturerModelCode == null) ? 0 : manufacturerModelCode.hashCode());
		result = prime * result + ((manufacturerModelTypeCode == null) ? 0 : manufacturerModelTypeCode.hashCode());
		result = prime * result + ((manufacturerModelOptionCode == null) ? 0 : manufacturerModelOptionCode.hashCode());
		result = prime * result + ((manufacturerExtColorCode == null) ? 0 : manufacturerExtColorCode.hashCode());
		result = prime * result + ((manufacturerIntColorCode == null) ? 0 : manufacturerIntColorCode.hashCode());
		result = prime * result + ((expectedShipDate == null) ? 0 : expectedShipDate.hashCode());
		result = prime * result + ((lastWorkingDate == null) ? 0 : lastWorkingDate.hashCode());
		result = prime * result + ((lastWorkingTime == null) ? 0 : lastWorkingTime.hashCode());
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
		Gal104Dto other = (Gal104Dto) obj;
		if (shipperId == null) {
			if (other.shipperId != null)
				return false;
		} else if (!shipperId.equals(other.shipperId))
			return false;
		if (versionNumber == null) {
			if (other.versionNumber != null)
				return false;
		} else if (!versionNumber.equals(other.versionNumber))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (condensedKdLot == null) {
			if (other.condensedKdLot != null)
				return false;
		} else if (!condensedKdLot.equals(other.condensedKdLot))
			return false;
		if (condensedProductionLot == null) {
			if (other.condensedProductionLot != null)
				return false;
		} else if (!condensedProductionLot.equals(other.condensedProductionLot))
			return false;
		if (salesModelCode == null) {
			if (other.salesModelCode != null)
				return false;
		} else if (!salesModelCode.equals(other.salesModelCode))
			return false;
		if (salesModelTypeCode == null) {
			if (other.salesModelTypeCode != null)
				return false;
		} else if (!salesModelTypeCode.equals(other.salesModelTypeCode))
			return false;
		if (salesModelOption == null) {
			if (other.salesModelOption != null)
				return false;
		} else if (!salesModelOption.equals(other.salesModelOption))
			return false;
		if (salesExtColorCode == null) {
			if (other.salesExtColorCode != null)
				return false;
		} else if (!salesExtColorCode.equals(other.salesExtColorCode))
			return false;
		if (salesIntColorCode == null) {
			if (other.salesIntColorCode != null)
				return false;
		} else if (!salesIntColorCode.equals(other.salesIntColorCode))
			return false;
		if (manufacturerModelYearCode == null) {
			if (other.manufacturerModelYearCode != null)
				return false;
		} else if (!manufacturerModelYearCode.equals(other.manufacturerModelYearCode))
			return false;
		if (manufacturerModelCode == null) {
			if (other.manufacturerModelCode != null)
				return false;
		} else if (!manufacturerModelCode.equals(other.manufacturerModelCode))
			return false;
		if (manufacturerModelTypeCode == null) {
			if (other.manufacturerModelTypeCode != null)
				return false;
		} else if (!manufacturerModelTypeCode.equals(other.manufacturerModelTypeCode))
			return false;
		if (manufacturerModelOptionCode == null) {
			if (other.manufacturerModelOptionCode != null)
				return false;
		} else if (!manufacturerModelOptionCode.equals(other.manufacturerModelOptionCode))
			return false;
		if (manufacturerExtColorCode == null) {
			if (other.manufacturerExtColorCode != null)
				return false;
		} else if (!manufacturerExtColorCode.equals(other.manufacturerExtColorCode))
			return false;
		if (manufacturerIntColorCode == null) {
			if (other.manufacturerIntColorCode != null)
				return false;
		} else if (!manufacturerIntColorCode.equals(other.manufacturerIntColorCode))
			return false;
		if (expectedShipDate == null) {
			if (other.expectedShipDate != null)
				return false;
		} else if (!expectedShipDate.equals(other.expectedShipDate))
			return false;
		if (lastWorkingDate == null) {
			if (other.lastWorkingDate != null)
				return false;
		} else if (!lastWorkingDate.equals(other.lastWorkingDate))
			return false;
		if (lastWorkingTime == null) {
			if (other.lastWorkingTime != null)
				return false;
		} else if (!lastWorkingTime.equals(other.lastWorkingTime))
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
