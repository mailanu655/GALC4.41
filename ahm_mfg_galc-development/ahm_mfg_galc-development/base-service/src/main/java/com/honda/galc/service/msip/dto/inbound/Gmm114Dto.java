package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

public class Gmm114Dto implements IMsipInboundDto {

	private static final long serialVersionUID = 1L;
	
	private String productSpecCode;    
    private String plantCode;	
    private String modelYearCode;	
	private String modelCode;	
	private String modelTypeCode;	
	private String modelYearDescription;    
    private String missionNoPrefix;    
    private String stampSerialDescription;    
    private String boundaryMark;
    
	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelYearDescription() {
		return modelYearDescription;
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getMissionNoPrefix() {
		return missionNoPrefix;
	}

	public void setMissionNoPrefix(String missionNoPrefix) {
		this.missionNoPrefix = missionNoPrefix;
	}

	public String getStampSerialDescription() {
		return stampSerialDescription;
	}

	public void setStampSerialDescription(String stampSerialDescription) {
		this.stampSerialDescription = stampSerialDescription;
	}

	public String getBoundaryMark() {
		return boundaryMark;
	}

	public void setBoundaryMark(String boundaryMark) {
		this.boundaryMark = boundaryMark;
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelYearDescription == null) ? 0 : modelYearDescription.hashCode());
		result = prime * result + ((missionNoPrefix == null) ? 0 : missionNoPrefix.hashCode());
		result = prime * result + ((stampSerialDescription == null) ? 0 : stampSerialDescription.hashCode());
		result = prime * result + ((boundaryMark == null) ? 0 : boundaryMark.hashCode());
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
		Gmm114Dto other = (Gmm114Dto) obj;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelYearDescription == null) {
			if (other.modelYearDescription != null)
				return false;
		} else if (!modelYearDescription.equals(other.modelYearDescription))
			return false;
		if (missionNoPrefix == null) {
			if (other.missionNoPrefix != null)
				return false;
		} else if (!missionNoPrefix.equals(other.missionNoPrefix))
			return false;
		if (stampSerialDescription == null) {
			if (other.stampSerialDescription != null)
				return false;
		} else if (!stampSerialDescription.equals(other.stampSerialDescription))
			return false;
		if (boundaryMark == null) {
			if (other.boundaryMark != null)
				return false;
		} else if (!boundaryMark.equals(other.boundaryMark))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}