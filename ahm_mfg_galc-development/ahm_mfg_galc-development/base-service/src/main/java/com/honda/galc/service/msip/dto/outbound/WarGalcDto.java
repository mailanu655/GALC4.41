package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;


/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class WarGalcDto extends BaseOutboundDto implements IMsipOutboundDto {
	
	private static final long serialVersionUID = 1L;
	private String 	keyNo	;
	private String 	afOnSequenceNumber	;
	private String 	modelOptionCode	;
	private String 	modelCode	;
	private String 	time	;
	private String 	date	;
	private String 	extColorCode	;
	private String 	fifCodes	;
	private String 	productionLot	;
	private String 	productId	;
	private String 	engineSerialNo	;
	private String 	framePlantCode	;
	private String 	lineNumber	;
	private String 	modelYearCode	;
	private String 	intColorCode	;
	private String 	afLineNumber	;
	private String 	actualTimestamp	;
	private String 	engKdLotNumber	;
	private String 	modelTypeCode	;
	private String 	kdLotNumber	;
	private String 	enginePlantCode	;
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
	
	public String getKeyNo() {
		return keyNo;
	}
	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}
	public String getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}
	public void setAfOnSequenceNumber(String afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}
	public String getModelOptionCode() {
		return modelOptionCode;
	}
	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getExtColorCode() {
		return extColorCode;
	}
	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}
	public String getFifCodes() {
		return fifCodes;
	}
	public void setFifCodes(String fifCodes) {
		this.fifCodes = fifCodes;
	}
	public String getProductionLot() {
		return productionLot;
	}
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getEngineSerialNo() {
		return engineSerialNo;
	}
	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}
	public String getFramePlantCode() {
		return framePlantCode;
	}
	public void setFramePlantCode(String framePlantCode) {
		this.framePlantCode = framePlantCode;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getModelYearCode() {
		return modelYearCode;
	}
	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}
	public String getIntColorCode() {
		return intColorCode;
	}
	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}
	public String getAfLineNumber() {
		return afLineNumber;
	}
	public void setAfLineNumber(String afLineNumber) {
		this.afLineNumber = afLineNumber;
	}
	public String getActualTimestamp() {
		return actualTimestamp;
	}
	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	public String getEngKdLotNumber() {
		return engKdLotNumber;
	}
	public void setEngKdLotNumber(String engKdLotNumber) {
		this.engKdLotNumber = engKdLotNumber;
	}
	public String getModelTypeCode() {
		return modelTypeCode;
	}
	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}
	public String getKdLotNumber() {
		return kdLotNumber;
	}
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	public String getEnginePlantCode() {
		return enginePlantCode;
	}
	public void setEnginePlantCode(String enginePlantCode) {
		this.enginePlantCode = enginePlantCode;
	}
	
	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyNo == null) ? 0 : keyNo.hashCode());
		result = prime * result + ((afOnSequenceNumber == null) ? 0 : afOnSequenceNumber.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((extColorCode == null) ? 0 : extColorCode.hashCode());
		result = prime * result + ((fifCodes == null) ? 0 : fifCodes.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((engineSerialNo == null) ? 0 : engineSerialNo.hashCode());
		result = prime * result + ((framePlantCode == null) ? 0 : framePlantCode.hashCode());
		result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((intColorCode == null) ? 0 : intColorCode.hashCode());
		result = prime * result + ((afLineNumber == null) ? 0 : afLineNumber.hashCode());
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((engKdLotNumber == null) ? 0 : engKdLotNumber.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((enginePlantCode == null) ? 0 : enginePlantCode.hashCode());
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
		WarGalcDto other = (WarGalcDto) obj;
		if (keyNo == null) {
			if (other.keyNo != null)
				return false;
		} else if (!keyNo.equals(other.keyNo))
			return false;
		if (afOnSequenceNumber == null) {
			if (other.afOnSequenceNumber != null)
				return false;
		} else if (!afOnSequenceNumber.equals(other.afOnSequenceNumber))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (extColorCode == null) {
			if (other.extColorCode != null)
				return false;
		} else if (!extColorCode.equals(other.extColorCode))
			return false;
		if (fifCodes == null) {
			if (other.fifCodes != null)
				return false;
		} else if (!fifCodes.equals(other.fifCodes))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (engineSerialNo == null) {
			if (other.engineSerialNo != null)
				return false;
		} else if (!engineSerialNo.equals(other.engineSerialNo))
			return false;
		if (framePlantCode == null) {
			if (other.framePlantCode != null)
				return false;
		} else if (!framePlantCode.equals(other.framePlantCode))
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (intColorCode == null) {
			if (other.intColorCode != null)
				return false;
		} else if (!intColorCode.equals(other.intColorCode))
			return false;
		if (afLineNumber == null) {
			if (other.afLineNumber != null)
				return false;
		} else if (!afLineNumber.equals(other.afLineNumber))
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (engKdLotNumber == null) {
			if (other.engKdLotNumber != null)
				return false;
		} else if (!engKdLotNumber.equals(other.engKdLotNumber))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (enginePlantCode == null) {
			if (other.enginePlantCode != null)
				return false;
		} else if (!enginePlantCode.equals(other.enginePlantCode))
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
