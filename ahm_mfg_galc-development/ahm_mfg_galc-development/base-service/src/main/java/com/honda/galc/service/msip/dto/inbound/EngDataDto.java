package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;


/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class EngDataDto implements IMsipInboundDto {

	private static final long serialVersionUID = 1L;
	
	private String engineSerialNumber;
	private String engineModel;
	private String engineType;
	private String engineOption;
	private String enginePartNo;
    private String engineShipKD;
    private String engineFiredFlag;
    private String missionSerialNumber;
    private String missionModelType;
    private String aepProductionDate;
    private String aepProdSeqNO;
    private String aepProdSufNO;
    private String lineNo;

	public EngDataDto() {}

	public String getEngineSerialNumber() {
		return engineSerialNumber;
	}

	public void setEngineSerialNumber(String engineSerialNumber) {
		this.engineSerialNumber = engineSerialNumber;
	}

	public String getEngineModel() {
		return engineModel;
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getEngineOption() {
		return engineOption;
	}

	public void setEngineOption(String engineOption) {
		this.engineOption = engineOption;
	}

	public String getEnginePartNo() {
		return enginePartNo;
	}

	public void setEnginePartNo(String enginePartNo) {
		this.enginePartNo = enginePartNo;
	}

	public String getEngineShipKD() {
		return engineShipKD;
	}

	public void setEngineShipKD(String engineShipKD) {
		this.engineShipKD = engineShipKD;
	}

	public String getEngineFiredFlag() {
		return engineFiredFlag;
	}

	public void setEngineFiredFlag(String engineFiredFlag) {
		this.engineFiredFlag = engineFiredFlag;
	}

	public String getMissionSerialNumber() {
		return missionSerialNumber;
	}

	public void setMissionSerialNumber(String missionSerialNumber) {
		this.missionSerialNumber = missionSerialNumber;
	}

	public String getMissionModelType() {
		return missionModelType;
	}

	public void setMissionModelType(String missionModelType) {
		this.missionModelType = missionModelType;
	}

	public String getAepProductionDate() {
		return aepProductionDate;
	}

	public void setAepProductionDate(String aepProductionDate) {
		this.aepProductionDate = aepProductionDate;
	}

	public String getAepProdSeqNO() {
		return aepProdSeqNO;
	}

	public void setAepProdSeqNO(String aepProdSeqNO) {
		this.aepProdSeqNO = aepProdSeqNO;
	}

	public String getAepProdSufNO() {
		return aepProdSufNO;
	}

	public void setAepProdSufNO(String aepProdSufNO) {
		this.aepProdSufNO = aepProdSufNO;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((engineSerialNumber == null) ? 0 : engineSerialNumber.hashCode());
		result = prime * result + ((engineModel == null) ? 0 : engineModel.hashCode());
		result = prime * result + ((engineType == null) ? 0 : engineType.hashCode());
		result = prime * result + ((engineOption == null) ? 0 : engineOption.hashCode());
		result = prime * result + ((enginePartNo == null) ? 0 : enginePartNo.hashCode());
		result = prime * result + ((engineShipKD == null) ? 0 : engineShipKD.hashCode());
		result = prime * result + ((engineFiredFlag == null) ? 0 : engineFiredFlag.hashCode());
		result = prime * result + ((missionSerialNumber == null) ? 0 : missionSerialNumber.hashCode());
		result = prime * result + ((missionModelType == null) ? 0 : missionModelType.hashCode());
		result = prime * result + ((aepProductionDate == null) ? 0 : aepProductionDate.hashCode());
		result = prime * result + ((aepProdSeqNO == null) ? 0 : aepProdSeqNO.hashCode());
		result = prime * result + ((aepProdSufNO == null) ? 0 : aepProdSufNO.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
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
		EngDataDto other = (EngDataDto) obj;
		if (engineSerialNumber == null) {
			if (other.engineSerialNumber != null)
				return false;
		} else if (!engineSerialNumber.equals(other.engineSerialNumber))
			return false;
		if (engineModel == null) {
			if (other.engineModel != null)
				return false;
		} else if (!engineModel.equals(other.engineModel))
			return false;
		if (engineType == null) {
			if (other.engineType != null)
				return false;
		} else if (!engineType.equals(other.engineType))
			return false;
		if (engineOption == null) {
			if (other.engineOption != null)
				return false;
		} else if (!engineOption.equals(other.engineOption))
			return false;
		if (enginePartNo == null) {
			if (other.enginePartNo != null)
				return false;
		} else if (!enginePartNo.equals(other.enginePartNo))
			return false;
		if (engineShipKD == null) {
			if (other.engineShipKD != null)
				return false;
		} else if (!engineShipKD.equals(other.engineShipKD))
			return false;
		if (engineFiredFlag == null) {
			if (other.engineFiredFlag != null)
				return false;
		} else if (!engineFiredFlag.equals(other.engineFiredFlag))
			return false;
		if (missionSerialNumber == null) {
			if (other.missionSerialNumber != null)
				return false;
		} else if (!missionSerialNumber.equals(other.missionSerialNumber))
			return false;
		if (missionModelType == null) {
			if (other.missionModelType != null)
				return false;
		} else if (!missionModelType.equals(other.missionModelType))
			return false;
		if (aepProductionDate == null) {
			if (other.aepProductionDate != null)
				return false;
		} else if (!aepProductionDate.equals(other.aepProductionDate))
			return false;
		if (aepProdSeqNO == null) {
			if (other.aepProdSeqNO != null)
				return false;
		} else if (!aepProdSeqNO.equals(other.aepProdSeqNO))
			return false;
		if (aepProdSufNO == null) {
			if (other.aepProdSufNO != null)
				return false;
		} else if (!aepProdSufNO.equals(other.aepProdSufNO))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}



