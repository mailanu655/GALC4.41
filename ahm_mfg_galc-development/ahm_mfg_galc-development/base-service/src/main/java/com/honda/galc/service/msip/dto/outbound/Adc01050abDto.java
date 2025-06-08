package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
*/
public class Adc01050abDto extends BaseOutboundDto implements IMsipOutboundDto{
	private static final long serialVersionUID = 1L;
	private String vin;
	private String dateString;
	private String time;
	private String sendLocation;
	private String tranType;
	private String salesModelCode;
	private String salesModelTypeCode;
	private String salesModelOptionCode;
	private String salesModelColorCode;
	private String engineNumber;
	private String keyNumber;
	private String cicIssuData;
	private String adcProcessCode;
	private String lineNumber;
	private String productionDate;
	private String productionSequenceNumber;
	private String productionSuffix;
	private String kdLotLineNumber;
	private String kdLotDate;
	private String kdLotSequenceNumber;
	private String kdLotSuffix;
	private String priceString;
	private String vechicleUnitId;
	private String vechicleCommonUnitId;
	private String afOffDate;
	private String cccRegNbr;
	private String partInstalled;
	private String filler1;
	private String fifCode;
	private String filler2;
	private String errorMsg;
	private Boolean isError;
	
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSendLocation() {
		return sendLocation;
	}

	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
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

	public String getSalesModelOptionCode() {
		return salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getSalesModelColorCode() {
		return salesModelColorCode;
	}

	public void setSalesModelColorCode(String salesModelColorCode) {
		this.salesModelColorCode = salesModelColorCode;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(String keyNumber) {
		this.keyNumber = keyNumber;
	}

	public String getCicIssuData() {
		return cicIssuData;
	}

	public void setCicIssuData(String cicIssuData) {
		this.cicIssuData = cicIssuData;
	}

	public String getAdcProcessCode() {
		return adcProcessCode;
	}

	public void setAdcProcessCode(String adcProcessCode) {
		this.adcProcessCode = adcProcessCode;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductionSequenceNumber() {
		return productionSequenceNumber;
	}

	public void setProductionSequenceNumber(String productionSequenceNumber) {
		this.productionSequenceNumber = productionSequenceNumber;
	}

	public String getProductionSuffix() {
		return productionSuffix;
	}

	public void setProductionSuffix(String productionSuffix) {
		this.productionSuffix = productionSuffix;
	}

	public String getKdLotLineNumber() {
		return kdLotLineNumber;
	}

	public void setKdLotLineNumber(String kdLotLineNumber) {
		this.kdLotLineNumber = kdLotLineNumber;
	}

	public String getKdLotDate() {
		return kdLotDate;
	}

	public void setKdLotDate(String kdLotDate) {
		this.kdLotDate = kdLotDate;
	}

	public String getKdLotSequenceNumber() {
		return kdLotSequenceNumber;
	}

	public void setKdLotSequenceNumber(String kdLotSequenceNumber) {
		this.kdLotSequenceNumber = kdLotSequenceNumber;
	}

	public String getKdLotSuffix() {
		return kdLotSuffix;
	}

	public void setKdLotSuffix(String kdLotSuffix) {
		this.kdLotSuffix = kdLotSuffix;
	}

	public String getPriceString() {
		return priceString;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public String getVechicleUnitId() {
		return vechicleUnitId;
	}

	public void setVechicleUnitId(String vechicleUnitId) {
		this.vechicleUnitId = vechicleUnitId;
	}

	public String getVechicleCommonUnitId() {
		return vechicleCommonUnitId;
	}

	public void setVechicleCommonUnitId(String vechicleCommonUnitId) {
		this.vechicleCommonUnitId = vechicleCommonUnitId;
	}

	public String getAfOffDate() {
		return afOffDate;
	}

	public void setAfOffDate(String afOffDate) {
		this.afOffDate = afOffDate;
	}

	public String getCccRegNbr() {
		return cccRegNbr;
	}

	public void setCccRegNbr(String cccRegNbr) {
		this.cccRegNbr = cccRegNbr;
	}

	public String getPartInstalled() {
		return partInstalled;
	}

	public void setPartInstalled(String partInstalled) {
		this.partInstalled = partInstalled;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}

	public String getFifCode() {
		return fifCode;
	}

	public void setFifCode(String fifCode) {
		this.fifCode = fifCode;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
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
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((dateString == null) ? 0 : dateString.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((sendLocation == null) ? 0 : sendLocation.hashCode());
		result = prime * result + ((tranType == null) ? 0 : tranType.hashCode());
		result = prime * result + ((salesModelCode == null) ? 0 : salesModelCode.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((salesModelColorCode == null) ? 0 : salesModelColorCode.hashCode());
		result = prime * result + ((engineNumber == null) ? 0 : engineNumber.hashCode());
		result = prime * result + ((keyNumber == null) ? 0 : keyNumber.hashCode());
		result = prime * result + ((cicIssuData == null) ? 0 : cicIssuData.hashCode());
		result = prime * result + ((adcProcessCode == null) ? 0 : adcProcessCode.hashCode());
		result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
		
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionSequenceNumber == null) ? 0 : productionSequenceNumber.hashCode());
		result = prime * result + ((productionSuffix == null) ? 0 : productionSuffix.hashCode());
		result = prime * result + ((kdLotLineNumber == null) ? 0 : kdLotLineNumber.hashCode());
		result = prime * result + ((kdLotDate == null) ? 0 : kdLotDate.hashCode());
		result = prime * result + ((kdLotSequenceNumber == null) ? 0 : kdLotSequenceNumber.hashCode());
		result = prime * result + ((kdLotSuffix == null) ? 0 : kdLotSuffix.hashCode());
		result = prime * result + ((priceString == null) ? 0 : priceString.hashCode());
		result = prime * result + ((vechicleUnitId == null) ? 0 : vechicleUnitId.hashCode());
		result = prime * result + ((vechicleCommonUnitId == null) ? 0 : vechicleCommonUnitId.hashCode());
		result = prime * result + ((afOffDate == null) ? 0 : afOffDate.hashCode());
		result = prime * result + ((cccRegNbr == null) ? 0 : cccRegNbr.hashCode());
		result = prime * result + ((partInstalled == null) ? 0 : partInstalled.hashCode());
		result = prime * result + ((filler1 == null) ? 0 : filler1.hashCode());
		result = prime * result + ((fifCode == null) ? 0 : fifCode.hashCode());
		result = prime * result + ((filler2 == null) ? 0 : filler2.hashCode());
		
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
		Adc01050abDto other = (Adc01050abDto) obj;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (dateString == null) {
			if (other.dateString != null)
				return false;
		} else if (!dateString.equals(other.dateString))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (sendLocation == null) {
			if (other.sendLocation != null)
				return false;
		} else if (!sendLocation.equals(other.sendLocation))
			return false;
		if (tranType == null) {
			if (other.tranType != null)
				return false;
		} else if (!tranType.equals(other.tranType))
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
		if (salesModelOptionCode == null) {
			if (other.salesModelOptionCode != null)
				return false;
		} else if (!salesModelOptionCode.equals(other.salesModelOptionCode))
			return false;
		if (salesModelColorCode == null) {
			if (other.salesModelColorCode != null)
				return false;
		} else if (!salesModelColorCode.equals(other.salesModelColorCode))
			return false;
		if (engineNumber == null) {
			if (other.engineNumber != null)
				return false;
		} else if (!engineNumber.equals(other.engineNumber))
			return false;
		if (keyNumber == null) {
			if (other.keyNumber != null)
				return false;
		} else if (!keyNumber.equals(other.keyNumber))
			return false;
		if (cicIssuData == null) {
			if (other.cicIssuData != null)
				return false;
		} else if (!cicIssuData.equals(other.cicIssuData))
			return false;
		if (adcProcessCode == null) {
			if (other.adcProcessCode != null)
				return false;
		} else if (!adcProcessCode.equals(other.adcProcessCode))
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionSequenceNumber == null) {
			if (other.productionSequenceNumber != null)
				return false;
		} else if (!productionSequenceNumber.equals(other.productionSequenceNumber))
			return false;
		if (productionSuffix == null) {
			if (other.productionSuffix != null)
				return false;
		} else if (!productionSuffix.equals(other.productionSuffix))
			return false;
		if (kdLotLineNumber == null) {
			if (other.kdLotLineNumber != null)
				return false;
		} else if (!kdLotLineNumber.equals(other.kdLotLineNumber))
			return false;
		if (kdLotDate == null) {
			if (other.kdLotDate != null)
				return false;
		} else if (!kdLotDate.equals(other.kdLotDate))
			return false;
		if (kdLotSequenceNumber == null) {
			if (other.kdLotSequenceNumber != null)
				return false;
		} else if (!kdLotSequenceNumber.equals(other.kdLotSequenceNumber))
			return false;
		if (kdLotSuffix == null) {
			if (other.kdLotSuffix != null)
				return false;
		} else if (!kdLotSuffix.equals(other.kdLotSuffix))
			return false;
		if (priceString == null) {
			if (other.priceString != null)
				return false;
		} else if (!priceString.equals(other.priceString))
			return false;
		if (vechicleUnitId == null) {
			if (other.vechicleUnitId != null)
				return false;
		} else if (!vechicleUnitId.equals(other.vechicleUnitId))
			return false;
		if (vechicleCommonUnitId == null) {
			if (other.vechicleCommonUnitId != null)
				return false;
		} else if (!vechicleCommonUnitId.equals(other.vechicleCommonUnitId))
			return false;
		if (afOffDate == null) {
			if (other.afOffDate != null)
				return false;
		} else if (!afOffDate.equals(other.afOffDate))
			return false;
		if (cccRegNbr == null) {
			if (other.cccRegNbr != null)
				return false;
		} else if (!cccRegNbr.equals(other.cccRegNbr))
			return false;
		if (partInstalled == null) {
			if (other.partInstalled != null)
				return false;
		} else if (!partInstalled.equals(other.partInstalled))
			return false;
		if (filler1 == null) {
			if (other.filler1 != null)
				return false;
		} else if (!filler1.equals(other.filler1))
			return false;
		if (fifCode == null) {
			if (other.fifCode != null)
				return false;
		} else if (!fifCode.equals(other.fifCode))
			return false;
		if (filler2 == null) {
			if (other.filler2 != null)
				return false;
		} else if (!filler2.equals(other.filler2))
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
