package com.honda.galc.oif.dto;

import com.honda.galc.entity.product.MissionSpec;
import com.honda.galc.util.GPCSData;

public class MissionSpecDTO {

	@GPCSData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
    @GPCSData("PLANT_CODE")
    private String plantCode;
	@GPCSData("MODEL_YEAR_CODE")
    private String modelYearCode;
	@GPCSData("MODEL_CODE")
	private String modelCode;
	@GPCSData("MODEL_TYPE_CODE")
	private String modelTypeCode;
	@GPCSData("MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;
    @GPCSData("MISSION_NO_PREFIX")
    private String missionNoPrefix;
    @GPCSData("STAMP_SERIAL_DESC")
    private String stampSerialDescription;
    @GPCSData("BOUNDARY_MARK")
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
	public void setMissionNoPrefix(String missionNoPrefix) {
		this.missionNoPrefix = missionNoPrefix;
	}
	public String getMissionNoPrefix() {
		return missionNoPrefix;
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
	public void setStampSerialDescription(String stampSerialDescription) {
		this.stampSerialDescription = stampSerialDescription;
	}
	public String getStampSerialDescription() {
		return stampSerialDescription;
	}
	public void setBoundaryMark(String boundaryMark) {
		this.boundaryMark = boundaryMark;
	}
	public String getBoundaryMark() {
		return boundaryMark;
	}

	public MissionSpec deriveMissionSpec() {
		MissionSpec missionSpec = new MissionSpec(); 
		missionSpec.setProductSpecCode(modelYearCode+modelCode+modelTypeCode);
		missionSpec.setPlantCode(plantCode);
		missionSpec.setModelYearCode(modelYearCode);
		missionSpec.setModelCode(modelCode);
		missionSpec.setModelTypeCode(modelTypeCode);
		missionSpec.setModelYearDescription(modelYearDescription);
		missionSpec.setModelYearDescription(modelYearDescription);
		missionSpec.setMissionNoPrefix(missionNoPrefix);
//		missionSpec.setBoundaryMark(boundaryMark);
//		missionSpec.setStampSerialDesc(stampSerialDescription);
		return missionSpec;
	}

	@Override
	public String toString() {
		return productSpecCode;
	}

	
}