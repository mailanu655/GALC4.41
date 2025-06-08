package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;


/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class TranGalDto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;
	private String productId;
	private String buildDate;
	private String buildTime;
	private String lineNumber;
	private String plantCode;
	private String shift;
	private String teamCd;
	private String typeCd;
	private String castLotNumber;
	private String machLotNumber;
	private String torqueCastLotNumber;
	private String torqueMachLotNumber;
	private String kdLotNumber;
	private String prodLotKd;
	private String filler;
	private String warrantyHeaderRecord;
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
	
	public String getWarrantyHeaderRecord() {
		return warrantyHeaderRecord;
	}

	public void setWarrantyHeaderRecord(String warrantyHeaderRecord) {
		this.warrantyHeaderRecord = warrantyHeaderRecord;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getTeamCd() {
		return teamCd;
	}

	public void setTeamCd(String teamCd) {
		this.teamCd = teamCd;
	}

	public String getTypeCd() {
		return typeCd;
	}

	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}

	public String getCastLotNumber() {
		return castLotNumber;
	}

	public void setCastLotNumber(String castLotNumber) {
		this.castLotNumber = castLotNumber;
	}

	public String getMachLotNumber() {
		return machLotNumber;
	}

	public void setMachLotNumber(String machLotNumber) {
		this.machLotNumber = machLotNumber;
	}

	public String getTorqueCastLotNumber() {
		return torqueCastLotNumber;
	}

	public void setTorqueCastLotNumber(String torqueCastLotNumber) {
		this.torqueCastLotNumber = torqueCastLotNumber;
	}

	public String getTorqueMachLotNumber() {
		return torqueMachLotNumber;
	}

	public void setTorqueMachLotNumber(String torqueMachLotNumber) {
		this.torqueMachLotNumber = torqueMachLotNumber;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getProdLotKd() {
		return prodLotKd;
	}

	public void setProdLotKd(String prodLotKd) {
		this.prodLotKd = prodLotKd;
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
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((buildDate == null) ? 0 : buildDate.hashCode());
		result = prime * result + ((buildTime == null) ? 0 : buildTime.hashCode());
		result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((teamCd == null) ? 0 : teamCd.hashCode());
		result = prime * result + ((typeCd == null) ? 0 : typeCd.hashCode());
		result = prime * result + ((castLotNumber == null) ? 0 : castLotNumber.hashCode());
		result = prime * result + ((machLotNumber == null) ? 0 : machLotNumber.hashCode());
		result = prime * result + ((torqueCastLotNumber == null) ? 0 : torqueCastLotNumber.hashCode());
		result = prime * result + ((torqueMachLotNumber == null) ? 0 : torqueMachLotNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((prodLotKd == null) ? 0 : prodLotKd.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((warrantyHeaderRecord == null) ? 0 : warrantyHeaderRecord.hashCode());
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
		TranGalDto other = (TranGalDto) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (buildDate == null) {
			if (other.buildDate != null)
				return false;
		} else if (!buildDate.equals(other.buildDate))
			return false;
		if (buildTime == null) {
			if (other.buildTime != null)
				return false;
		} else if (!buildTime.equals(other.buildTime))
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (teamCd == null) {
			if (other.teamCd != null)
				return false;
		} else if (!teamCd.equals(other.teamCd))
			return false;
		if (typeCd == null) {
			if (other.typeCd != null)
				return false;
		} else if (!typeCd.equals(other.typeCd))
			return false;
		if (castLotNumber == null) {
			if (other.castLotNumber != null)
				return false;
		} else if (!castLotNumber.equals(other.castLotNumber))
			return false;
		if (machLotNumber == null) {
			if (other.machLotNumber != null)
				return false;
		} else if (!machLotNumber.equals(other.machLotNumber))
			return false;
		if (torqueCastLotNumber == null) {
			if (other.torqueCastLotNumber != null)
				return false;
		} else if (!torqueCastLotNumber.equals(other.torqueCastLotNumber))
			return false;
		if (torqueMachLotNumber == null) {
			if (other.torqueMachLotNumber != null)
				return false;
		} else if (!torqueMachLotNumber.equals(other.torqueMachLotNumber))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (prodLotKd == null) {
			if (other.prodLotKd != null)
				return false;
		} else if (!prodLotKd.equals(other.prodLotKd))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (warrantyHeaderRecord == null) {
			if (other.warrantyHeaderRecord != null)
				return false;
		} else if (!warrantyHeaderRecord.equals(other.warrantyHeaderRecord))
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
