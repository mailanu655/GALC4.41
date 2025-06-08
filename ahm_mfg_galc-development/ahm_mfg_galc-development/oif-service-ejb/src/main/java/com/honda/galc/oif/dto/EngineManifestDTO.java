package com.honda.galc.oif.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.GPCSData;

public class EngineManifestDTO {

	@GPCSData("ENGINE_SERIAL_NUMBER")
	private String engineSerialNumber;
	@GPCSData("ENGINE_MODEL")
	private String engineModel;
	@GPCSData("ENGINE_TYPE")
	private String engineType;
	@GPCSData("ENGINE_OPTION")
	private String engineOption;
	@GPCSData("ENGINE_PART_NO")
	private String enginePartNo;
	@GPCSData("ENGINE_SHIP_KD")
    private String engineShipKD;
	@GPCSData("ENGINE_FIRED_FLAG")
    private String engineFiredFlag;
	@GPCSData("ENGINE_SOURCE")
    private String engineSource;
	@GPCSData("MISSION_SERIAL_NO")
    private String missionSerialNumber;
	@GPCSData("MISSION_MODEL_TYPE")
    private String missionModelType;
    @GPCSData("AEP_PROD_DATE")
    private String aepProductionDate;
    @GPCSData("AEP_PROD_SEQ_NO")
    private String aepProdSeqNO;
	@GPCSData("AEP_PROD_SUF_NO")
    private String aepProdSufNO;
	@GPCSData("LINE_NO")
    private String lineNo;
    
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

	public String getEngineShipKD() {
		return engineShipKD;
	}

	public void setEngineShipKD(String engineShipKD) {
		this.engineShipKD = engineShipKD;
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

	public void setEngineFiredFlag(String engineFiredFlag) {
		this.engineFiredFlag = engineFiredFlag;
	}

	public String getEngineFiredFlag() {
		return engineFiredFlag;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	@Override
	public String toString() {
		return engineSerialNumber;
	}
	
	public String getEngineSource() {
		return StringUtils.trim(engineSource);
	}

	public void setEngineSource(String engineSource) {
		this.engineSource = engineSource;
	}

}