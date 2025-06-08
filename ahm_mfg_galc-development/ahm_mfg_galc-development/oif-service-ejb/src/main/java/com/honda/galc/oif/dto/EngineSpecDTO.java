package com.honda.galc.oif.dto;

import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.util.GPCSData;

public class EngineSpecDTO {

	@GPCSData("PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@GPCSData("MISSION_PLANT_CODE")
    private String missionPlantCode;
    @GPCSData("MISSION_MODEL_CODE")
    private String missionModelCode;
    @GPCSData("MISSION_PROTOTYPE_CODE")
    private String missionPrototypeCode;
    @GPCSData("MISSION_MODEL_TYPE_CODE")
    private String missionModelTypeCode;
    @GPCSData("ENGINE_NO_PREFIX")
    private String engineNoPrefix;
    @GPCSData("TRANSMISSION")
    private String transmission;
    @GPCSData("TRANSMISSION_DESCRIPTION")
    private String transmissionDescription;
    @GPCSData("GEAR_SHIFT")
    private String gearShift;
    @GPCSData("GEAR_SHIFT_DESCRIPTION")
    private String gearShiftDescription;
    @GPCSData("DISPLACEMENT")
    private String displacement;
    @GPCSData("DISPLACEMENT_COMMENT")
    private String displacementComment;
    @GPCSData("ENGINE_PROTOTYPE_CODE")
    private String enginePrototypeCode;
    @GPCSData("PLANT_CODE")
    private String plantCode;
    @GPCSData("MISSION_MODEL_YEAR_CODE")
    private String missionModelYearCode;
	@GPCSData("MODEL_YEAR_CODE")
    private String modelYearCode;
	@GPCSData("MODEL_CODE")
	private String modelCode;
	@GPCSData("MODEL_TYPE_CODE")
	private String modelTypeCode;
	@GPCSData("MODEL_OPTION_CODE")
	private String modelOptionCode;
	@GPCSData("MODEL_YEAR_DESCRIPTION")
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
	public EngineSpec deriveEngineSpec() {
		EngineSpec engineSpec = new EngineSpec(); 
		engineSpec.setProductSpecCode(productSpecCode);
		engineSpec.setMissionPlantCode(missionPlantCode);
		engineSpec.setMissionModelCode(missionModelCode);
		engineSpec.setMissionPrototypeCode(missionPrototypeCode);
		engineSpec.setMissionModelTypeCode(missionModelTypeCode);
		engineSpec.setEngineNoPrefix(engineNoPrefix);
		engineSpec.setTransmission(transmission);
		engineSpec.setTransmissionDescription(transmissionDescription);
		engineSpec.setGearShift(gearShift);
		engineSpec.setGearShiftDescription(gearShiftDescription);
		engineSpec.setDisplacement(displacement);
		engineSpec.setDisplacementComment(displacementComment);
		engineSpec.setEnginePrototypeCode(enginePrototypeCode);
		engineSpec.setPlantCode(plantCode);
		engineSpec.setMissionModelYearCode(missionModelYearCode);
		engineSpec.setModelYearCode(modelYearCode);
		engineSpec.setModelCode(modelCode);
		engineSpec.setModelTypeCode(modelTypeCode);
		engineSpec.setModelOptionCode(modelOptionCode);
		engineSpec.setModelYearDescription(modelYearDescription);
		return engineSpec;
	}

}