package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class RearDifferentialDto extends BaseOutboundDto implements IMsipOutboundDto {
private static final long serialVersionUID = 1L;
	private String modelYear;
	private String modelCode;
	private String modelType;
	private String vin;
	private String prodDate;
	private String shipDate;
	private String serialNo;
	private String installTs;
	private String filler;
	private String errorMsg;
	private Boolean isError;
	
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getModelYear() {
		return modelYear;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getProdDate() {
		return prodDate;
	}

	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getInstallTs() {
		return installTs;
	}

	public void setInstallTs(String installTs) {
		this.installTs = installTs;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String getSiteName() {
		
		return null;
	}

	public String getPlantName() {
		
		return null;
	}

	public String getProcessPointId() {
		
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((prodDate == null) ? 0 : prodDate.hashCode());
		result = prime * result + ((shipDate == null) ? 0 : shipDate.hashCode());
		result = prime * result + ((serialNo == null) ? 0 : serialNo.hashCode());
		result = prime * result + ((installTs == null) ? 0 : installTs.hashCode());
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
		RearDifferentialDto other = (RearDifferentialDto) obj;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelType == null) {
			if (other.modelType != null)
				return false;
		} else if (!modelType.equals(other.modelType))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (prodDate == null) {
			if (other.prodDate != null)
				return false;
		} else if (!prodDate.equals(other.prodDate))
			return false;
		if (shipDate == null) {
			if (other.shipDate != null)
				return false;
		} else if (!shipDate.equals(other.shipDate))
			return false;
		if (serialNo == null) {
			if (other.serialNo != null)
				return false;
		} else if (!serialNo.equals(other.serialNo))
			return false;
		if (installTs == null) {
			if (other.installTs != null)
				return false;
		} else if (!installTs.equals(other.installTs))
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
