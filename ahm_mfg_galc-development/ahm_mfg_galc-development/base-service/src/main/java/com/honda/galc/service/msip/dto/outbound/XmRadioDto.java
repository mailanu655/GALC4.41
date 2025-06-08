package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;


/**
 * @author Anusha Gopalan
 * @date Aug 18, 2017
 */
public class XmRadioDto extends BaseOutboundDto implements IMsipOutboundDto{
	private static final long serialVersionUID = 1L;
	private String vin;
	private String productDivisionCode;
	private String modelName;
	private String modelYear;
	private String prodDate;
	private String radioInstalledDate;
	private String shipDate;
	private String xmRadioIdPrefix;
	private String xmRadioIdMusic;
	private String xmRadioIdData;
	private String tcuNumber;
	private String filler;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getProductDivisionCode() {
		return productDivisionCode;
	}
	public void setProductDivisionCode(String productDivisionCode) {
		this.productDivisionCode = productDivisionCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getProdDate() {
		return prodDate;
	}
	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}
	public String getRadioInstalledDate() {
		return radioInstalledDate;
	}
	public void setRadioInstalledDate(String radioInstalledDate) {
		this.radioInstalledDate = radioInstalledDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getXmRadioIdPrefix() {
		return xmRadioIdPrefix;
	}
	public void setXmRadioIdPrefix(String xmRadioIdPrefix) {
		this.xmRadioIdPrefix = xmRadioIdPrefix;
	}
	public String getXmRadioIdMusic() {
		return xmRadioIdMusic;
	}
	public void setXmRadioIdMusic(String xmRadioIdMusic) {
		this.xmRadioIdMusic = xmRadioIdMusic;
	}
	public String getXmRadioIdData() {
		return xmRadioIdData;
	}
	public void setXmRadioIdData(String xmRadioIdData) {
		this.xmRadioIdData = xmRadioIdData;
	}
	public String getTcuNumber() {
		return tcuNumber;
	}
	public void setTcuNumber(String tcuNumber) {
		this.tcuNumber = tcuNumber;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((productDivisionCode == null) ? 0 : productDivisionCode.hashCode());
		result = prime * result + ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((prodDate == null) ? 0 : prodDate.hashCode());
		result = prime * result + ((radioInstalledDate == null) ? 0 : radioInstalledDate.hashCode());
		result = prime * result + ((shipDate == null) ? 0 : shipDate.hashCode());
		result = prime * result + ((xmRadioIdPrefix == null) ? 0 : xmRadioIdPrefix.hashCode());
		result = prime * result + ((xmRadioIdMusic == null) ? 0 : xmRadioIdMusic.hashCode());
		result = prime * result + ((xmRadioIdData == null) ? 0 : xmRadioIdData.hashCode());
		result = prime * result + ((tcuNumber == null) ? 0 : tcuNumber.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + ((isError == null) ? 0 : isError.hashCode());
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
		XmRadioDto other = (XmRadioDto) obj;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (productDivisionCode == null) {
			if (other.productDivisionCode != null)
				return false;
		} else if (!productDivisionCode.equals(other.productDivisionCode))
			return false;
		if (modelName == null) {
			if (other.modelName != null)
				return false;
		} else if (!modelName.equals(other.modelName))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (prodDate == null) {
			if (other.prodDate != null)
				return false;
		} else if (!prodDate.equals(other.prodDate))
			return false;
		if (radioInstalledDate == null) {
			if (other.radioInstalledDate != null)
				return false;
		} else if (!radioInstalledDate.equals(other.radioInstalledDate))
			return false;
		if (shipDate == null) {
			if (other.shipDate != null)
				return false;
		} else if (!shipDate.equals(other.shipDate))
			return false;
		if (xmRadioIdPrefix == null) {
			if (other.xmRadioIdPrefix != null)
				return false;
		} else if (!xmRadioIdPrefix.equals(other.xmRadioIdPrefix))
			return false;
		if (xmRadioIdMusic == null) {
			if (other.xmRadioIdMusic != null)
				return false;
		} else if (!xmRadioIdMusic.equals(other.xmRadioIdMusic))
			return false;
		if (xmRadioIdData == null) {
			if (other.xmRadioIdData != null)
				return false;
		} else if (!xmRadioIdData.equals(other.xmRadioIdData))
			return false;
		if (tcuNumber == null) {
			if (other.tcuNumber != null)
				return false;
		} else if (!tcuNumber.equals(other.tcuNumber))
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
		if (isError == null) {
			if (other.isError != null)
				return false;
		} else if (!isError.equals(other.isError))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
