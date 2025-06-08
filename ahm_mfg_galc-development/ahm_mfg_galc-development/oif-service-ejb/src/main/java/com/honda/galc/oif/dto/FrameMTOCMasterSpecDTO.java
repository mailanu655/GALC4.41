package com.honda.galc.oif.dto;

import com.honda.galc.entity.product.FrameSpec;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameMTOCMasterSpecId;
import com.honda.galc.util.GPCSData;

/*
*
* GMM111 Table GAL158TBX Definitions
*  Plant_Code_Frame         |K|C|0|4|U| 
*  Model_Year_Code          |K|C|4|1|U|
*  Model_Code               |K|C|5|3|U|
*  Model_Type_Code          |K|C|8|3|U|
*  Model_Option_Code        |K|C|11|3|U|
*  Ext_Color_Code           |K|C|14|10|U|
*  Int_Color_Code           |K|C|24|2|U|
*  Model_Year_Description   |F|C|26|4|U|
*  Ext_Color_Description    |F|C|30|10|U|
*  Int_Color_Description    |F|C|40|40|U|
*  Frame_No_Prefix          |F|C|80|11|U|
*  Series_Code              |F|C|91|1|U|
*  Series_Description       |F|C|92|40|U|
*  Grade_Code               |F|C|132|10|U|
*  Body_And_Trans_Type_Code |F|C|142|1|U|
*  Body_And_Trans_Type_Desc |F|C|143|30|U|
*  Sales_Model_Code         |F|C|173|10|U|
*  Sales_Model_Type_Code    |F|C|183|2|U|
*  Sales_Model_Option_Code  |F|C|185|2|U|
*  Sales_Ext_Color_Code     |F|C|187|10|U|
*  Sales_Int_Color_Code     |F|C|197|2|U|
*  Prototype_Code           |F|C|199|1|U|
*  F_E                      |F|C|200|1|U|
*  Plant_Code_Engine        |F|C|201|4|U|
*  Engine_Model_Year_Code   |F|C|205|1|U|
*  Engine_Model_Code        |F|C|206|3|U|
*  Engine_Model_Type_Code   |F|C|209|3|U|
*  Engine_Option_Code       |F|C|212|3|U|
*  
*  length is 215 char
*/

//Additional fields not in DailyDepartmentSchedule
public class FrameMTOCMasterSpecDTO { 
	
	@GPCSData("PLANT_CODE_FRAME")
	private String plantCodeFrame;

	@GPCSData("MODEL_YEAR_CODE")
	private String modelYearCode;

	@GPCSData("MODEL_CODE")
	private String modelCode;

	@GPCSData("MODEL_TYPE_CODE")
	private String modelTypeCode;

	@GPCSData("MODEL_OPTION_CODE")
	private String modelOptionCode;

	@GPCSData("ENGINE_MODEL_CODE")
	private String engineModelCode;

	@GPCSData("ENGINE_MODEL_TYPE_CODE")
	private String engineModelTypeCode;

	@GPCSData("ENGINE_MODEL_YEAR_CODE")
	private String engineModelYearCode;

	@GPCSData("ENGINE_OPTION_CODE")
	private String engineOptionCode;

	@GPCSData("F_E")
	private String fE;

	@GPCSData("MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;

	@GPCSData("MODEL_DESCRIPTION")
	private String modelDescription;

	@GPCSData("SHORT_MODEL_DESCRIPTION")
	private String shortModelDescription;

	@GPCSData("PLANT_CODE_ENGINE")
	private String plantCodeEngine;

	@GPCSData("SALES_MODEL_OPTION_CODE")
	private String salesModelOptionCode;
	
	@GPCSData("EXT_COLOR_CODE")
	private String extColorCode;

	@GPCSData("INT_COLOR_CODE")
	private String intColorCode;

	@GPCSData("EXT_COLOR_DESCRIPTION")
	private String extColorDescription;

	@GPCSData("INT_COLOR_DESCRIPTION")
	private String intColorDescription;

	@GPCSData("SALES_MODEL_CODE")
	private String salesModelCode;

	@GPCSData("SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@GPCSData("SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;

	@GPCSData("SALES_INT_COLOR_CODE")
	private String salesIntColorCode;

	@GPCSData("PROTOTYPE_CODE")
	private String prototypeCode;

	@GPCSData("FRAME_NO_PREFIX")
	private String frameNoPrefix;

	@GPCSData("SERIES_CODE")
	private String seriesCode;

	@GPCSData("SERIES_DESCRIPTION")
	private String seriesDescription;

	@GPCSData("GRADE_CODE")
	private String gradeCode;

	@GPCSData("BODY_AND_TRANS_TYPE_CODE")
	private String bodyAndTransTypeCode;

	@GPCSData("BODY_AND_TRANS_TYPE_DESC")
	private String bodyAndTransTypeDesc;
	
	@GPCSData("BOUNDARY_MARK_REQUIRED")
	private String boundaryMarkRequired;
	
	@GPCSData("COMMON_SALES_MODEL_CODE")
	private String commonSalesModelCode;

	@GPCSData("COMMON_SALES_MODEL_TYPE_CODE")
	private String commonSalesModelTypeCode;

	private String productSpecCode;
	
	private String engineMTO;
	
	@GPCSData("ALT_ENGINE_MTO")
	private String altEngineMTO;
	
	public FrameMTOCMasterSpecDTO() {
	}
	
	public String getPlantCodeFrame() {
		return plantCodeFrame;
	}

	public void setPlantCodeFrame(String plantCodeFrame) {
		this.plantCodeFrame = plantCodeFrame;
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

	public String getEngineModelCode() {
		return engineModelCode;
	}

	public void setEngineModelCode(String engineModelCode) {
		this.engineModelCode = engineModelCode;
	}

	public String getEngineModelTypeCode() {
		return engineModelTypeCode;
	}

	public void setEngineModelTypeCode(String engineModelTypeCode) {
		this.engineModelTypeCode = engineModelTypeCode;
	}

	public String getEngineModelYearCode() {
		return engineModelYearCode;
	}

	public void setEngineModelYearCode(String engineModelYearCode) {
		this.engineModelYearCode = engineModelYearCode;
	}

	public String getEngineOptionCode() {
		return engineOptionCode;
	}

	public void setEngineOptionCode(String engineOptionCode) {
		this.engineOptionCode = engineOptionCode;
	}

	public String getFE() {
		return fE;
	}

	public void setFE(String fE) {
		this.fE = fE;
	}

	public String getModelYearDescription() {
		return modelYearDescription;
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getShortModelDescription() {
		return shortModelDescription;
	}

	public void setShortModelDescription(String shortModelDescription) {
		this.shortModelDescription = shortModelDescription;
	}

	public String getPlantCodeEngine() {
		return plantCodeEngine;
	}

	public void setPlantCodeEngine(String plantCodeEngine) {
		this.plantCodeEngine = plantCodeEngine;
	}

	public String getSalesModelOptionCode() {
		return salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getExtColorCode() {
		return extColorCode;
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public String getIntColorCode() {
		return intColorCode;
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public String getExtColorDescription() {
		return extColorDescription;
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	public String getIntColorDescription() {
		return intColorDescription;
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSalesExtColorCode() {
		return salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return salesIntColorCode;
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getPrototypeCode() {
		return prototypeCode;
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getFrameNoPrefix() {
		return frameNoPrefix;
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getBodyAndTransTypeCode() {
		return bodyAndTransTypeCode;
	}

	public void setBodyAndTransTypeCode(String bodyAndTransTypeCode) {
		this.bodyAndTransTypeCode = bodyAndTransTypeCode;
	}

	public String getBodyAndTransTypeDesc() {
		return bodyAndTransTypeDesc;
	}

	public void setBodyAndTransTypeDesc(String bodyAndTransTypeDesc) {
		this.bodyAndTransTypeDesc = bodyAndTransTypeDesc;
	}

	public String getBoundaryMarkRequired() {
		return boundaryMarkRequired;
	}

	public void setBoundaryMarkRequired(String boundaryMarkRequired) {
		this.boundaryMarkRequired = boundaryMarkRequired;
	}

	public void setEngineMTO(String engineMTO) {
		this.engineMTO = engineMTO;
	}

	public String getEngineMTO() {
		return engineMTO;
	}



	public String getCommonSalesModelCode() {
		return StringUtils.trim(commonSalesModelCode);
	}

	public void setCommonSalesModelCode(String commonSalesModelCode) {
		this.commonSalesModelCode = commonSalesModelCode;
	}

	public String getCommonSalesModelTypeCode() {
		return StringUtils.trim(commonSalesModelTypeCode);
	}

	public void setCommonSalesModelTypeCode(String commonSalesModelTypeCode) {
		this.commonSalesModelTypeCode = commonSalesModelTypeCode;
	}
	
	public String getAltEngineMTO() {
		return altEngineMTO;
	}

	public void setAltEngineMTO(String altEngineMTO) {
		this.altEngineMTO = altEngineMTO;
	}

	// Copy Constructor
	public FrameMTOCMasterSpecDTO(FrameMTOCMasterSpecDTO mtoSpec) {
		this.bodyAndTransTypeCode = mtoSpec.getBodyAndTransTypeCode();
		this.bodyAndTransTypeDesc = mtoSpec.getBodyAndTransTypeDesc();
		this.boundaryMarkRequired = mtoSpec.getBoundaryMarkRequired();
		this.engineModelCode = mtoSpec.getEngineModelCode();
		this.engineModelTypeCode = mtoSpec.getEngineModelTypeCode();
		this.engineModelYearCode = mtoSpec.getEngineModelYearCode();
		this.engineMTO = mtoSpec.getEngineMTO();
		this.altEngineMTO = mtoSpec.getAltEngineMTO();
		this.engineOptionCode = mtoSpec.getEngineOptionCode();
		this.extColorCode = mtoSpec.getExtColorCode();
		this.extColorDescription = mtoSpec.getExtColorDescription();
		this.fE = mtoSpec.getFE();
		this.frameNoPrefix = mtoSpec.getFrameNoPrefix();
		this.gradeCode = mtoSpec.getGradeCode();
		this.intColorCode = mtoSpec.getIntColorCode();
		this.intColorDescription = mtoSpec.getIntColorDescription();
		this.modelCode = mtoSpec.getModelCode();
		this.modelOptionCode = mtoSpec.getModelOptionCode();
		this.modelTypeCode = mtoSpec.getModelTypeCode();
		this.modelYearCode = mtoSpec.getModelYearCode();
		this.modelYearDescription = mtoSpec.getModelYearDescription();
		this.modelDescription = mtoSpec.getModelDescription();
		this.shortModelDescription = mtoSpec.getShortModelDescription();
		this.plantCodeEngine = mtoSpec.getPlantCodeEngine();
		this.plantCodeFrame = mtoSpec.getPlantCodeFrame();
		this.productSpecCode = mtoSpec.getProductSpecCode();
		this.prototypeCode = mtoSpec.getPrototypeCode();
		this.salesExtColorCode = mtoSpec.getSalesExtColorCode();
		this.salesIntColorCode = mtoSpec.getIntColorCode();
		this.salesModelCode = mtoSpec.getSalesModelCode();
		this.salesModelOptionCode = mtoSpec.getSalesModelOptionCode();
		this.salesModelTypeCode = mtoSpec.getSalesModelTypeCode();
		this.seriesCode = mtoSpec.getSeriesCode();
		this.seriesDescription = mtoSpec.getSeriesDescription();		
	}

	public FrameSpec deriveFrameSpec(){
		FrameSpec frameSpec = new FrameSpec();
		frameSpec.setBodyAndTransTypeCode(bodyAndTransTypeCode);
		frameSpec.setBodyAndTransTypeDesc(bodyAndTransTypeDesc);
		frameSpec.setBoundaryMarkRequired(boundaryMarkRequired);
		frameSpec.setEngineMto(engineMTO);
		frameSpec.setAltEngineMto(altEngineMTO);
		frameSpec.setEnginePlantCode(plantCodeEngine);
		frameSpec.setExtColorCode(extColorCode);
		frameSpec.setExtColorDescription(extColorDescription);
		frameSpec.setFrameNoPrefix(frameNoPrefix);
		frameSpec.setGradeCode(gradeCode);
		frameSpec.setIntColorCode(intColorCode);
		frameSpec.setIntColorDescription(intColorDescription);
		frameSpec.setModelCode(modelCode);
		frameSpec.setModelOptionCode(modelOptionCode);
		frameSpec.setModelTypeCode(modelTypeCode);
		frameSpec.setModelYearCode(modelYearCode); 
		frameSpec.setModelYearDescription(modelYearDescription);
		frameSpec.setModelDescription(modelDescription);
		frameSpec.setShortModelDescription(shortModelDescription);
		frameSpec.setPlantCodeGpcs(plantCodeFrame);
		frameSpec.setProductSpecCode(productSpecCode);
		frameSpec.setPrototypeCode(prototypeCode);
		frameSpec.setSalesExtColorCode(salesExtColorCode);
		frameSpec.setSalesIntColorCode(salesIntColorCode);
		frameSpec.setSalesModelCode(salesModelCode);
		frameSpec.setSalesModelTypeCode(salesModelTypeCode);
		frameSpec.setSeriesCode(seriesCode);
		frameSpec.setSeriesDescription(seriesDescription);
		if(StringUtils.isNotBlank(commonSalesModelCode))
			frameSpec.setCommonSalesModelCode(commonSalesModelCode);
		if(StringUtils.isNotBlank(commonSalesModelTypeCode))
			frameSpec.setCommonSalesModelTypeCode(commonSalesModelTypeCode);
		
		return frameSpec;
	}
	
	public FrameMTOCMasterSpec deriveFrameMTOCMasterSpec() {
		FrameMTOCMasterSpec frameMTOCMasterSpec = new FrameMTOCMasterSpec();
		frameMTOCMasterSpec.setId(deriveID());
		frameMTOCMasterSpec.setBodyAndTransTypeCode(bodyAndTransTypeCode);
		frameMTOCMasterSpec.setBodyAndTransTypeDesc(bodyAndTransTypeDesc);
		frameMTOCMasterSpec.setEngineModelCode(engineModelCode);
		frameMTOCMasterSpec.setEngineModelTypeCode(engineModelTypeCode);
		frameMTOCMasterSpec.setEngineModelYearCode(engineModelYearCode);
		frameMTOCMasterSpec.setEngineOptionCode(engineOptionCode);
		frameMTOCMasterSpec.setExtColorDescription(extColorDescription);
		frameMTOCMasterSpec.setFE(fE);
		frameMTOCMasterSpec.setFrameNoPrefix(frameNoPrefix);
		frameMTOCMasterSpec.setGradeCode(gradeCode);
		frameMTOCMasterSpec.setIntColorDescription(intColorDescription);
		frameMTOCMasterSpec.setModelYearDescription(modelYearDescription);
		frameMTOCMasterSpec.setPlantCodeEngine(plantCodeEngine);
		frameMTOCMasterSpec.setPrototypeCode(prototypeCode);
		frameMTOCMasterSpec.setSalesExtColorCode(salesExtColorCode);
		frameMTOCMasterSpec.setSalesIntColorCode(salesIntColorCode);
		frameMTOCMasterSpec.setSalesModelCode(salesModelCode);
		frameMTOCMasterSpec.setSalesModelOptionCode(salesModelOptionCode);
		frameMTOCMasterSpec.setSalesModelTypeCode(salesModelTypeCode);
		frameMTOCMasterSpec.setSeriesCode(seriesCode);
		frameMTOCMasterSpec.setSeriesDescription(seriesDescription);
		frameMTOCMasterSpec.setCommonSalesModelCode(commonSalesModelCode);
		frameMTOCMasterSpec.setCommonSalesModelTypeCode(commonSalesModelTypeCode);
		return frameMTOCMasterSpec;
	}

	private FrameMTOCMasterSpecId deriveID() {
		FrameMTOCMasterSpecId frameMTOCMasterSpecId = new FrameMTOCMasterSpecId();
		frameMTOCMasterSpecId.setExtColorCode(extColorCode);
		frameMTOCMasterSpecId.setIntColorCode(intColorCode);
		frameMTOCMasterSpecId.setModelCode(modelCode);
		frameMTOCMasterSpecId.setModelTypeCode(modelTypeCode);
		frameMTOCMasterSpecId.setModelYearCode(modelYearCode);
		frameMTOCMasterSpecId.setModelOptionCode(modelOptionCode);
		frameMTOCMasterSpecId.setPlantCodeFrame(plantCodeFrame);
		return frameMTOCMasterSpecId;
	}
}


