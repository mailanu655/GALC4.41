package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public class Giv706Dto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	private String	planCode;
	private String	lineNumber;
	private String	inHouseProcessLocation;
	private String	vinNumber;
	private String	productionSequenceNumber;
	private String	alcActualTimestamp;
	private String	productSpecCode;
	private String	bandNumber;
	private String	kdLotNumber;
	private String	partNumber;
	private String	partColorCode;
	private String	filler;
	private String	onSequenceNumber;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
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
	public String getInHouseProcessLocation() {
		return inHouseProcessLocation;
	}
	/**
	 * @param inHouseProcessLocation the inHouseProcessLocation to set
	 */
	public void setInHouseProcessLocation(String inHouseProcessLocation) {
		this.inHouseProcessLocation = inHouseProcessLocation;
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
	/**
	 * @return the onSequenceNumber
	 */
	public String getOnSequenceNumber() {
		return onSequenceNumber;
	}
	/**
	 * @param onSequenceNumber the onSequenceNumber to set
	 */
	public void setOnSequenceNumber(String onSequenceNumber) {
		this.onSequenceNumber = onSequenceNumber;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
		result = prime * result + ((inHouseProcessLocation == null) ? 0 : inHouseProcessLocation.hashCode());
		result = prime * result + ((vinNumber == null) ? 0 : vinNumber.hashCode());
		result = prime * result + ((productionSequenceNumber == null) ? 0 : productionSequenceNumber.hashCode());
		result = prime * result + ((alcActualTimestamp == null) ? 0 : alcActualTimestamp.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((bandNumber == null) ? 0 : bandNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((partNumber == null) ? 0 : partNumber.hashCode());
		result = prime * result + ((partColorCode == null) ? 0 : partColorCode.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((onSequenceNumber == null) ? 0 : onSequenceNumber.hashCode());
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
		Giv706Dto other = (Giv706Dto) obj;
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
		if (inHouseProcessLocation == null) {
			if (other.inHouseProcessLocation != null)
				return false;
		} else if (!inHouseProcessLocation.equals(other.inHouseProcessLocation))
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
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (onSequenceNumber == null) {
			if (other.onSequenceNumber != null)
				return false;
		} else if (!onSequenceNumber.equals(other.onSequenceNumber))
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
