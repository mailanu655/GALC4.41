package com.honda.galc.service.msip.dto.outbound;

import java.sql.Timestamp;


/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */

public class PrgSafDto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;

	private String productionLot;	
	private String kdLotNumber;
	private String processLocation;
	private int lotSize;
	private String ppId;	
	private String ppId7;
	private String ppIdDescription;
	private int unitsPassed;
	private Timestamp lineOnTimestamp;
	private boolean isError;
	private String errorMsg;
	
	public String getVersion() {
		return version;
	}	

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public String getPpIdDescription() {
		return ppIdDescription;
	}

	public void setPpIdDescription(String ppIdDescription) {
		this.ppIdDescription = ppIdDescription;
	}

	public int getUnitsPassed() {
		return unitsPassed;
	}

	public void setUnitsPassed(int unitsPassed) {
		this.unitsPassed = unitsPassed;
	}

	public void setLineOnTimestamp(Timestamp lineOnTimestamp) {
		this.lineOnTimestamp = lineOnTimestamp;
	}

	public Timestamp getLineOnTimestamp() {
		return lineOnTimestamp;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setPpId7(String ppId7) {
		this.ppId7 = ppId7;
	}

	public String getPpId7() {
		return ppId7;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("ProductionProgressDTO:");
		result.append("\nProduction Lot: " + productionLot);
		result.append("\nKD Lot: " + kdLotNumber);
		result.append("\nProcess Location: " + processLocation);
		result.append("\nLot Size: " + lotSize);
		result.append("\nProcess Point Id : " + ppId);
		result.append("\nppId Description: " + ppIdDescription);
		result.append("\nUnits Passed: " + unitsPassed);
		result.append("\nLineOn Timestamp: " + lineOnTimestamp);
		return result.toString();
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
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + lotSize;
		result = prime * result + ((ppId == null) ? 0 : ppId.hashCode());
		result = prime * result + ((ppId7 == null) ? 0 : ppId7.hashCode());
		result = prime * result + ((ppIdDescription == null) ? 0 : ppIdDescription.hashCode());
		result = prime * result + unitsPassed;
		result = prime * result + ((lineOnTimestamp == null) ? 0 : lineOnTimestamp.hashCode());
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
		PrgSafDto other = (PrgSafDto) obj;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (other.lotSize != lotSize)
			return false;
		if (ppId == null) {
			if (other.ppId != null)
				return false;
		} else if (!ppId.equals(other.ppId))
			return false;
		if (ppId7 == null) {
			if (other.ppId7 != null)
				return false;
		} else if (!ppId7.equals(other.ppId7))
			return false;
		if (ppIdDescription == null) {
			if (other.ppIdDescription != null)
				return false;
		} else if (!ppIdDescription.equals(other.ppIdDescription))
			return false;
		if (other.unitsPassed != unitsPassed)
			return false;
		if (lineOnTimestamp == null) {
			if (other.lineOnTimestamp != null)
				return false;
		} else if (!lineOnTimestamp.equals(other.lineOnTimestamp))
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
}
