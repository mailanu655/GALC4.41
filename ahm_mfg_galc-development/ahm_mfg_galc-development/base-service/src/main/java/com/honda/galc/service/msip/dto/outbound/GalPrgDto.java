package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;



/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class GalPrgDto extends BaseOutboundDto implements IMsipOutboundDto  {

	private static final long serialVersionUID = 1L;
	private String productId;
	private String planCode;
	private String lineNo;
	private String processPointId;
	private String productionDate;
	private String actualTimestamp;
	private String productSpecCode;
	private String lotSize;
	private String onSeqNo;
	private String productionLot;
	private String kdLotNumber;
	private String planOffDate;
	private String currentTimestamp;
	private Character sentFlag;
	private String partNumber; 
	private boolean isError;
	private String errorMsg;

	
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
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
	 * @return the lineNo
	 */
	public String getLineNo() {
		return lineNo;
	}

	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}


	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	/**
	 * @return the productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}

	/**
	 * @param productionDate the productionDate to set
	 */
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	/**
	 * @return the actualTimestamp
	 */
	public String getActualTimestamp() {
		return actualTimestamp;
	}

	/**
	 * @param actualTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
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
	 * @return the productionLot
	 */
	public String getProductionLot() {
		return productionLot;
	}

	/**
	 * @param productionLot the productionLot to set
	 */
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
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
	 * @return the planOffDate
	 */
	public String getPlanOffDate() {
		return planOffDate;
	}

	/**
	 * @param planOffDate the planOffDate to set
	 */
	public void setPlanOffDate(String planOffDate) {
		this.planOffDate = planOffDate;
	}

	/**
	 * @return the currentTimestamp
	 */
	public String getCurrentTimestamp() {
		return currentTimestamp;
	}

	/**
	 * @param currentTimestamp the currentTimestamp to set
	 */
	public void setCurrentTimestamp(String currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	/**
	 * @return the sentFlag
	 */
	public Character getSentFlag() {
		return sentFlag;
	}

	/**
	 * @param sentFlag the sentFlag to set
	 */
	public void setSentFlag(Character sentFlag) {
		this.sentFlag = sentFlag;
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
	 * @return the lotSize
	 */
	public String getLotSize() {
		return lotSize;
	}

	/**
	 * @param lotSize the lotSize to set
	 */
	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	/**
	 * @return the onSeqNo
	 */
	public String getOnSeqNo() {
		return onSeqNo;
	}

	/**
	 * @param onSeqNo the onSeqNo to set
	 */
	public void setOnSeqNo(String onSeqNo) {
		this.onSeqNo = onSeqNo;
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

	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((lotSize == null) ? 0 : lotSize.hashCode());
		result = prime * result + ((onSeqNo == null) ? 0 : onSeqNo.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((planOffDate == null) ? 0 : planOffDate.hashCode());
		result = prime * result + ((currentTimestamp == null) ? 0 : currentTimestamp.hashCode());
		result = prime * result + ((sentFlag == null) ? 0 : sentFlag.hashCode());
		result = prime * result + ((partNumber == null) ? 0 : partNumber.hashCode());	
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
		GalPrgDto other = (GalPrgDto) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (lotSize == null) {
			if (other.lotSize != null)
				return false;
		} else if (!lotSize.equals(other.lotSize))
			return false;
		if (onSeqNo == null) {
			if (other.onSeqNo != null)
				return false;
		} else if (!onSeqNo.equals(other.onSeqNo))
			return false;
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
		if (planOffDate == null) {
			if (other.planOffDate != null)
				return false;
		} else if (!planOffDate.equals(other.planOffDate))
			return false;
		if (currentTimestamp == null) {
			if (other.currentTimestamp != null)
				return false;
		} else if (!currentTimestamp.equals(other.currentTimestamp))
			return false;
		if (sentFlag == null) {
			if (other.sentFlag != null)
				return false;
		} else if (!sentFlag.equals(other.sentFlag))
			return false;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
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
