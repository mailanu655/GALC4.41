package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;



/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv705Dto extends BaseOutboundDto implements IMsipOutboundDto {
	
	private static final long serialVersionUID = 1L;
	
	private String	planCode;
	private String	lineNumber;
	private String	processLocation;
	/** Number of product_id */
	private String	vinNumber;
	private String	productionSequenceNumber;
	/** The history actual time stamp the process point off */
	private String	alcActualTimestamp;
	private String	productSpecCode;
	private String	bandNumber;
	private String	kdLotNumber;
	private String	partNumber;
	private String	partColorCode;
	private String	bosSerialNumber;
	private String	filler;
	/** Mandatory Item if Cancel Flag 'Y'
	 * '1'=Cut, '2'=Remake
	 */
	private String	cancelReasonCode;
	private String	resultFlag;
	private String	cancelFlag;
	private Boolean isError;
	private String errorMsg;
	
	public String getVersion() {
		return version;
	}
	
	/**
	 * @return the vinNumber
	 */
	public String getVinNumber() {
		return vinNumber;
	}
	/**
	 * @param vinNumber the vinNumber to set
	 */
	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}
	/**
	 * @return the alcActualTimestamp
	 */
	public String getAlcActualTimestamp() {
		return alcActualTimestamp;
	}
	/**
	 * @param alcActualTimestamp the alcActualTimestamp to set
	 */
	public void setAlcActualTimestamp(String alcActualTimestamp) {
		this.alcActualTimestamp = alcActualTimestamp;
	}
	/**
	 * @return the bandNumber
	 */
	public String getBandNumber() {
		return bandNumber;
	}
	/**
	 * @param bandNumber the bandNumber to set
	 */
	public void setBandNumber(String bandNumber) {
		this.bandNumber = bandNumber;
	}
	/**
	 * @return the bosSerialNumber
	 */
	public String getBosSerialNumber() {
		return bosSerialNumber;
	}
	/**
	 * @param bosSerialNumber the bosSerialNumber to set
	 */
	public void setBosSerialNumber(String bosSerialNumber) {
		this.bosSerialNumber = bosSerialNumber;
	}
	/**
	 * @return the resultFlag
	 */
	public String getResultFlag() {
		return resultFlag;
	}
	/**
	 * @param resultFlag the resultFlag to set
	 */
	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}
	/**
	 * @return the cancelReasonCode
	 */
	public String getCancelReasonCode() {
		return cancelReasonCode;
	}
	/**
	 * @param cancelReasonCode the cancelReasonCode to set
	 */
	public void setCancelReasonCode(String cancelReasonCode) {
		this.cancelReasonCode = cancelReasonCode;
	}
	/**
	 * @return the cancelFlag
	 */
	public String getCancelFlag() {
		return cancelFlag;
	}
	/**
	 * @param cancelFlag the cancelFlag to set
	 */
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}


	/**
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}


	/**
	 * @param planCode the planCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}


	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}


	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}


	/**
	 * @return the inHouseProcessLocation
	 */
	public String getProcessLocation() {
		return processLocation;
	}


	/**
	 * @param inHouseProcessLocation the inHouseProcessLocation to set
	 */
	public void setProcessLocation(String inHouseProcessLocation) {
		this.processLocation = inHouseProcessLocation;
	}


	/**
	 * @return the productionSequenceNumber
	 */
	public String getProductionSequenceNumber() {
		return productionSequenceNumber;
	}


	/**
	 * @param productionSequenceNumber the productionSequenceNumber to set
	 */
	public void setProductionSequenceNumber(String productionSequenceNumber) {
		this.productionSequenceNumber = productionSequenceNumber;
	}


	/**
	 * @return the productSpecCode
	 */
	public String getProductSpecCode() {
		return productSpecCode;
	}


	/**
	 * @param productSpecCode the productSpecCode to set
	 */
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}


	/**
	 * @return the kdLotNumber
	 */
	public String getKdLotNumber() {
		return kdLotNumber;
	}


	/**
	 * @param kdLotNumber the kdLotNumber to set
	 */
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}


	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}


	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}


	/**
	 * @return the partColorCode
	 */
	public String getPartColorCode() {
		return partColorCode;
	}


	/**
	 * @param partColorCode the partColorCode to set
	 */
	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}


	/**
	 * @return the filler
	 */
	public String getFiller() {
		return filler;
	}


	/**
	 * @param filler the filler to set
	 */
	public void setFiller(String filler) {
		this.filler = filler;
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
	public Boolean getIsError() {
		// TODO Auto-generated method stub
		return isError;
	}


	@Override
	public String getErrorMsg() {
		// TODO Auto-generated method stub
		return errorMsg;
	}


	@Override
	public void setIsError(Boolean isError) {
		// TODO Auto-generated method stub
		this.isError = isError;
	}


	@Override
	public void setErrorMsg(String errorMsg) {
		// TODO Auto-generated method stub
		this.errorMsg = errorMsg;
	} 
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((vinNumber == null) ? 0 : vinNumber.hashCode());
		result = prime * result + ((productionSequenceNumber == null) ? 0 : productionSequenceNumber.hashCode());
		result = prime * result + ((alcActualTimestamp == null) ? 0 : alcActualTimestamp.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((bandNumber == null) ? 0 : bandNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((partNumber == null) ? 0 : partNumber.hashCode());
		result = prime * result + ((partColorCode == null) ? 0 : partColorCode.hashCode());
		result = prime * result + ((bosSerialNumber == null) ? 0 : bosSerialNumber.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((cancelReasonCode == null) ? 0 : cancelReasonCode.hashCode());
		result = prime * result + ((resultFlag == null) ? 0 : resultFlag.hashCode());
		result = prime * result + ((cancelFlag == null) ? 0 : cancelFlag.hashCode());
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
		Giv705Dto other = (Giv705Dto) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (vinNumber == null) {
			if (other.vinNumber != null)
				return false;
		} else if (!vinNumber.equals(other.vinNumber))
			return false;
		if (productionSequenceNumber == null) {
			if (other.productionSequenceNumber != null)
				return false;
		} else if (!productionSequenceNumber.equals(other.productionSequenceNumber))
			return false;
		if (alcActualTimestamp == null) {
			if (other.alcActualTimestamp != null)
				return false;
		} else if (!alcActualTimestamp.equals(other.alcActualTimestamp))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (bandNumber == null) {
			if (other.bandNumber != null)
				return false;
		} else if (!bandNumber.equals(other.bandNumber))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
			return false;
		if (partColorCode == null) {
			if (other.partColorCode != null)
				return false;
		} else if (!partColorCode.equals(other.partColorCode))
			return false;
		if (bosSerialNumber == null) {
			if (other.bosSerialNumber != null)
				return false;
		} else if (!bosSerialNumber.equals(other.bosSerialNumber))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (cancelReasonCode == null) {
			if (other.cancelReasonCode != null)
				return false;
		} else if (!cancelReasonCode.equals(other.cancelReasonCode))
			return false;
		if (resultFlag == null) {
			if (other.resultFlag != null)
				return false;
		} else if (!resultFlag.equals(other.resultFlag))
			return false;
		if (cancelFlag == null) {
			if (other.cancelFlag != null)
				return false;
		} else if (!cancelFlag.equals(other.cancelFlag))
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
