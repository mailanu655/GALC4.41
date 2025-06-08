package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;


/**
 * The persistent class for the GAL158TBX database table.
 * 
 */
@Entity
@Table(name="GAL158TBX")
public class FrameMTOCMasterSpec implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@EmbeddedId
	private FrameMTOCMasterSpecId id;

	@Column(name="BODY_AND_TRANS_TYPE_CODE")
	private String bodyAndTransTypeCode;

	@Column(name="BODY_AND_TRANS_TYPE_DESC")
	private String bodyAndTransTypeDesc;

	@Column(name="ENGINE_MODEL_CODE")
	private String engineModelCode;

	@Column(name="ENGINE_MODEL_TYPE_CODE")
	private String engineModelTypeCode;

	@Column(name="ENGINE_MODEL_YEAR_CODE")
	private String engineModelYearCode;

	@Column(name="ENGINE_OPTION_CODE")
	private String engineOptionCode;

	@Column(name="EXT_COLOR_DESCRIPTION")
	private String extColorDescription;

	@Column(name="F_E")
	private String fE;

	@Column(name="FRAME_NO_PREFIX")
	private String frameNoPrefix;

	@Column(name="GRADE_CODE")
	private String gradeCode;

	@Column(name="INT_COLOR_DESCRIPTION")
	private String intColorDescription;

	@Column(name="MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;

	@Column(name="PLANT_CODE_ENGINE")
	private String plantCodeEngine;

	@Column(name="PROTOTYPE_CODE")
	private String prototypeCode;

	@Column(name="SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;

	@Column(name="SALES_INT_COLOR_CODE")
	private String salesIntColorCode;

	@Column(name="SALES_MODEL_CODE")
	private String salesModelCode;

	@Column(name="SALES_MODEL_OPTION_CODE")
	private String salesModelOptionCode;

	@Column(name="SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@Column(name="SERIES_CODE")
	private String seriesCode;

	@Column(name="SERIES_DESCRIPTION")
	private String seriesDescription;
	
	@Column(name="COMMON_SALES_MODEL_CODE")
	private String commonSalesModelCode;
	
	@Column(name="COMMON_SALES_MODEL_TYPE_CODE")
	private String commonSalesModelTypeCode;


	public FrameMTOCMasterSpec() {
	}

	public FrameMTOCMasterSpecId getId() {
		return this.id;
	}

	public void setId(FrameMTOCMasterSpecId id) {
		this.id = id;
	}

	public String getBodyAndTransTypeCode() {
		return StringUtils.trim(this.bodyAndTransTypeCode);
	}

	public void setBodyAndTransTypeCode(String bodyAndTransTypeCode) {
		this.bodyAndTransTypeCode = bodyAndTransTypeCode;
	}

	public String getBodyAndTransTypeDesc() {
		return StringUtils.trim(this.bodyAndTransTypeDesc);
	}

	public void setBodyAndTransTypeDesc(String bodyAndTransTypeDesc) {
		this.bodyAndTransTypeDesc = bodyAndTransTypeDesc;
	}

	public String getEngineModelCode() {
		return StringUtils.trim(this.engineModelCode);
	}

	public void setEngineModelCode(String engineModelCode) {
		this.engineModelCode = engineModelCode;
	}

	public String getEngineModelTypeCode() {
		return StringUtils.stripEnd(this.engineModelTypeCode, null);
	}

	public void setEngineModelTypeCode(String engineModelTypeCode) {
		this.engineModelTypeCode = engineModelTypeCode;
	}

	public String getEngineModelYearCode() {
		return StringUtils.trim(this.engineModelYearCode);
	}

	public void setEngineModelYearCode(String engineModelYearCode) {
		this.engineModelYearCode = engineModelYearCode;
	}

	public String getEngineOptionCode() {
		return StringUtils.trim(this.engineOptionCode);
	}

	public void setEngineOptionCode(String engineOptionCode) {
		this.engineOptionCode = engineOptionCode;
	}

	public String getExtColorDescription() {
		return StringUtils.trim(this.extColorDescription);
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	public String getFE() {
		return StringUtils.trim(this.fE);
	}

	public void setFE(String fE) {
		this.fE = fE;
	}

	public String getFrameNoPrefix() {
		return StringUtils.trim(this.frameNoPrefix);
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
	}

	public String getGradeCode() {
		return StringUtils.trim(this.gradeCode);
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getIntColorDescription() {
		return StringUtils.trim(this.intColorDescription);
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getModelYearDescription() {
		return StringUtils.trim(this.modelYearDescription);
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getPlantCodeEngine() {
		return StringUtils.trim(this.plantCodeEngine);
	}

	public void setPlantCodeEngine(String plantCodeEngine) {
		this.plantCodeEngine = plantCodeEngine;
	}

	public String getPrototypeCode() {
		return StringUtils.trim(this.prototypeCode);
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getSalesExtColorCode() {
		return StringUtils.trim(this.salesExtColorCode);
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return StringUtils.trim(this.salesIntColorCode);
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getSalesModelCode() {
		return StringUtils.trim(this.salesModelCode);
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelOptionCode() {
		return StringUtils.trim(this.salesModelOptionCode);
	}

	public void setSalesModelOptionCode(String salesModelOptionCode) {
		this.salesModelOptionCode = salesModelOptionCode;
	}

	public String getSalesModelTypeCode() {
		return StringUtils.trim(this.salesModelTypeCode);
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	public String getSeriesCode() {
		return StringUtils.trim(this.seriesCode);
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public String getSeriesDescription() {
		return StringUtils.trim(this.seriesDescription);
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
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

	@Override
	public String toString(){
		return StringUtil.toString(
				getId().getPlantCodeFrame(), 
				getId().getModelYearCode(), 
				getId().getModelCode(), 
				getId().getModelTypeCode(), 
				getId().getModelOptionCode(), 
				getId().getExtColorCode(), 
				getId().getIntColorCode()
				);
	}

}