package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

public class Gmm112Dto implements IMsipInboundDto {
	
	private static final long serialVersionUID = 1L;

	private String productSpecCode;
    private String missionPlantCode;
    private String missionModelCode;
    private String missionPrototypeCode;
    private String missionModelTypeCode;
    private String engineNoPrefix;
    private String transmission;
    private String transmissionDescription;
    private String gearShift;
    private String gearShiftDescription;
    private String displacement;
    private String displacementComment;
    private String enginePrototypeCode;
    private String plantCode;
    private String missionModelYearCode;
    private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String modelYearDescription;

	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public String getMissionPlantCode() {
		return missionPlantCode;
	}
	public void setMissionPlantCode(String missionPlantCode) {
		this.missionPlantCode = missionPlantCode;
	}
	public String getMissionModelCode() {
		return missionModelCode;
	}
	public void setMissionModelCode(String missionModelCode) {
		this.missionModelCode = missionModelCode;
	}
	public String getMissionPrototypeCode() {
		return missionPrototypeCode;
	}
	public void setMissionPrototypeCode(String missionPrototypeCode) {
		this.missionPrototypeCode = missionPrototypeCode;
	}
	public String getMissionModelTypeCode() {
		return missionModelTypeCode;
	}
	public void setMissionModelTypeCode(String missionModelTypeCode) {
		this.missionModelTypeCode = missionModelTypeCode;
	}
	public String getEngineNoPrefix() {
		return engineNoPrefix;
	}
	public void setEngineNoPrefix(String engineNoPrefix) {
		this.engineNoPrefix = engineNoPrefix;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getTransmissionDescription() {
		return transmissionDescription;
	}
	public void setTransmissionDescription(String transmissionDescription) {
		this.transmissionDescription = transmissionDescription;
	}
	public String getGearShift() {
		return gearShift;
	}
	public void setGearShift(String gearShift) {
		this.gearShift = gearShift;
	}
	public String getGearShiftDescription() {
		return gearShiftDescription;
	}
	public void setGearShiftDescription(String gearShiftDescription) {
		this.gearShiftDescription = gearShiftDescription;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getDisplacementComment() {
		return displacementComment;
	}
	public void setDisplacementComment(String displacementComment) {
		this.displacementComment = displacementComment;
	}
	public String getEnginePrototypeCode() {
		return enginePrototypeCode;
	}
	public void setEnginePrototypeCode(String enginePrototypeCode) {
		this.enginePrototypeCode = enginePrototypeCode;
	}
	public String getPlantCode() {
		return plantCode;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getMissionModelYearCode() {
		return missionModelYearCode;
	}
	public void setMissionModelYearCode(String missionModelYearCode) {
		this.missionModelYearCode = missionModelYearCode;
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
	public String getModelOptionCode() {
		return modelOptionCode;
	}
	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}
	public String getModelYearDescription() {
		return modelYearDescription;
	}
	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((missionPlantCode == null) ? 0 : missionPlantCode.hashCode());
		result = prime * result + ((missionModelCode == null) ? 0 : missionModelCode.hashCode());
		result = prime * result + ((missionPrototypeCode == null) ? 0 : missionPrototypeCode.hashCode());
		result = prime * result + ((missionModelTypeCode == null) ? 0 : missionModelTypeCode.hashCode());
		result = prime * result + ((engineNoPrefix == null) ? 0 : engineNoPrefix.hashCode());
		result = prime * result + ((transmission == null) ? 0 : transmission.hashCode());
		result = prime * result + ((transmissionDescription == null) ? 0 : transmissionDescription.hashCode());
		result = prime * result + ((gearShift == null) ? 0 : gearShift.hashCode());
		result = prime * result + ((gearShiftDescription == null) ? 0 : gearShiftDescription.hashCode());
		result = prime * result + ((displacement == null) ? 0 : displacement.hashCode());
		result = prime * result + ((displacementComment == null) ? 0 : displacementComment.hashCode());
		result = prime * result + ((enginePrototypeCode == null) ? 0 : enginePrototypeCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((missionModelYearCode == null) ? 0 : missionModelYearCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((modelYearDescription == null) ? 0 : modelYearDescription.hashCode());
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
		Gmm112Dto other = (Gmm112Dto) obj;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (missionPlantCode == null) {
			if (other.missionPlantCode != null)
				return false;
		} else if (!missionPlantCode.equals(other.missionPlantCode))
			return false;
		if (missionModelCode == null) {
			if (other.missionModelCode != null)
				return false;
		} else if (!missionModelCode.equals(other.missionModelCode))
			return false;
		if (missionPrototypeCode == null) {
			if (other.missionPrototypeCode != null)
				return false;
		} else if (!missionPrototypeCode.equals(other.missionPrototypeCode))
			return false;
		if (missionModelTypeCode == null) {
			if (other.missionModelTypeCode != null)
				return false;
		} else if (!missionModelTypeCode.equals(other.missionModelTypeCode))
			return false;
		if (engineNoPrefix == null) {
			if (other.engineNoPrefix != null)
				return false;
		} else if (!engineNoPrefix.equals(other.engineNoPrefix))
			return false;
		if (transmission == null) {
			if (other.transmission != null)
				return false;
		} else if (!transmission.equals(other.transmission))
			return false;
		if (transmissionDescription == null) {
			if (other.transmissionDescription != null)
				return false;
		} else if (!transmissionDescription.equals(other.transmissionDescription))
			return false;
		if (gearShift == null) {
			if (other.gearShift != null)
				return false;
		} else if (!gearShift.equals(other.gearShift))
			return false;
		if (gearShiftDescription == null) {
			if (other.gearShiftDescription != null)
				return false;
		} else if (!gearShiftDescription.equals(other.gearShiftDescription))
			return false;
		if (displacement == null) {
			if (other.displacement != null)
				return false;
		} else if (!displacement.equals(other.displacement))
			return false;
		if (displacementComment == null) {
			if (other.displacementComment != null)
				return false;
		} else if (!displacementComment.equals(other.displacementComment))
			return false;
		if (enginePrototypeCode == null) {
			if (other.enginePrototypeCode != null)
				return false;
		} else if (!enginePrototypeCode.equals(other.enginePrototypeCode))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (missionModelYearCode == null) {
			if (other.missionModelYearCode != null)
				return false;
		} else if (!missionModelYearCode.equals(other.missionModelYearCode))
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
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (modelYearDescription == null) {
			if (other.modelYearDescription != null)
				return false;
		} else if (!modelYearDescription.equals(other.modelYearDescription))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}