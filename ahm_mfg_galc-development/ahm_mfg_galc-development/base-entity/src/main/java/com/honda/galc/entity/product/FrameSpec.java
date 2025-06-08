package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;

/**
 * 
 * <h3>FrameSpec Class description</h3>
 * <p> FrameSpec description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 9, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL144TBX")
public class FrameSpec extends ProductSpec {

	@Id
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name="EXT_COLOR_CODE")
	private String extColorCode;

	@Column(name="INT_COLOR_CODE")
	private String intColorCode;

	@Column(name="EXT_COLOR_DESCRIPTION")
	private String extColorDescription;

	@Column(name="INT_COLOR_DESCRIPTION")
	private String intColorDescription;

	@Column(name="PLANT_CODE_GPCS")
	private String plantCodeGpcs;

	@Column(name="ENGINE_PLANT_CODE")
	private String enginePlantCode;

	@Column(name="ENGINE_MTO")
	private String engineMto;

	@Column(name="SALES_MODEL_CODE")
	private String salesModelCode;

	@Column(name="SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@Column(name="SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;

	@Column(name="SALES_INT_COLOR_CODE")
	private String salesIntColorCode;

	@Column(name="PROTOTYPE_CODE")
	private String prototypeCode;

	@Column(name="FRAME_NO_PREFIX")
	private String frameNoPrefix;

	@Column(name="SERIES_CODE")
	private String seriesCode;

	@Column(name="SERIES_DESCRIPTION")
	private String seriesDescription;

	@Column(name="GRADE_CODE")
	private String gradeCode;

	@Column(name="BODY_AND_TRANS_TYPE_CODE")
	private String bodyAndTransTypeCode;

	@Column(name="BODY_AND_TRANS_TYPE_DESC")
	private String bodyAndTransTypeDesc;
	
	@Column(name="BOUNDARY_MARK_REQUIRED")
	private String boundaryMarkRequired;
	
	@Column(name="ALT_ENGINE_MTO")
	private String altEngineMto;
	
	@Column(name="MODEL_DESCRIPTION")
	private String modelDescription;
	
	@Column(name="SHORT_MODEL_DESCRIPTION")
	private String shortModelDescription;
	
	@Column(name="COMMON_SALES_MODEL_CODE")
	private String commonSalesModelCode;
	
	@Column(name="COMMON_SALES_MODEL_TYPE_CODE")
	private String commonSalesModelTypeCode;

	private static final long serialVersionUID = 1L;

	public FrameSpec() {
		super();
	}
	
	public String getId() {
		return getProductSpecCode();
	}
	
	@Override
    public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
    
    @Override
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
    
    @PrintAttribute
	public String getExtColorCode() {
		return StringUtils.trim(this.extColorCode);
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	@PrintAttribute
	public String getIntColorCode() {
		return StringUtils.trim(this.intColorCode);
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public String getExtColorDescription() {
		return StringUtils.trim(this.extColorDescription);
	}

	public void setExtColorDescription(String extColorDescription) {
		this.extColorDescription = extColorDescription;
	}

	@PrintAttribute
	public String getIntColorDescription() {
		return StringUtils.trim(this.intColorDescription);
	}

	public void setIntColorDescription(String intColorDescription) {
		this.intColorDescription = intColorDescription;
	}

	public String getPlantCodeGpcs() {
		return StringUtils.trim(this.plantCodeGpcs);
	}

	public void setPlantCodeGpcs(String plantCodeGpcs) {
		this.plantCodeGpcs = plantCodeGpcs;
	}

	public String getEnginePlantCode() {
		return StringUtils.trim(this.enginePlantCode);
	}

	public void setEnginePlantCode(String enginePlantCode) {
		this.enginePlantCode = enginePlantCode;
	}
	
	@PrintAttribute
	public String getEngineMto() {
		return StringUtils.trim(this.engineMto);
	}

	public void setEngineMto(String engineMto) {
		this.engineMto = engineMto;
	}

	@PrintAttribute
	public String getSalesModelCode() {
		return StringUtils.trim(this.salesModelCode);
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	@PrintAttribute
	public String getSalesModelTypeCode() {
		return StringUtils.trim(this.salesModelTypeCode);
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}

	@PrintAttribute
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

	public String getPrototypeCode() {
		return StringUtils.trim(this.prototypeCode);
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getFrameNoPrefix() {
		return StringUtils.trim(this.frameNoPrefix);
	}

	public void setFrameNoPrefix(String frameNoPrefix) {
		this.frameNoPrefix = frameNoPrefix;
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

	@PrintAttribute
	public String getGradeCode() {
		return StringUtils.trim(this.gradeCode);
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getBodyAndTransTypeCode() {
		return StringUtils.trim(this.bodyAndTransTypeCode);
	}

	public void setBodyAndTransTypeCode(String bodyAndTransTypeCode) {
		this.bodyAndTransTypeCode = bodyAndTransTypeCode;
	}

	@PrintAttribute
	public String getBodyAndTransTypeDesc() {
		return StringUtils.trim(this.bodyAndTransTypeDesc);
	}

	public void setBoundaryMarkRequired(String boundaryMarkRequired) {
		this.boundaryMarkRequired = boundaryMarkRequired;
	}

	public String getBoundaryMarkRequired() {
		return this.boundaryMarkRequired == null ? "" : this.boundaryMarkRequired;
	}

	public void setBodyAndTransTypeDesc(String bodyAndTransTypeDesc) {
		this.bodyAndTransTypeDesc = bodyAndTransTypeDesc;
	}

	public int getProductNoPrefixLength() {
		return frameNoPrefix == null ? 0 : frameNoPrefix.length();
	}
	
	@Override
	public String toString() {
		return toString(productSpecCode);
	}
	
	@PrintAttribute
	public String getAltEngineMto() {
		 return StringUtils.trim(this.altEngineMto);
	}

	public void setAltEngineMto(String altEngineMto) {
		this.altEngineMto = altEngineMto;
	}

	@PrintAttribute
	public String getModelDescription() {
		return StringUtils.trim(this.modelDescription);
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	@PrintAttribute
	public String getShortModelDescription() {
		return StringUtils.trim(this.shortModelDescription);
	}

	public void setShortModelDescription(String shortModelDescription) {
		this.shortModelDescription = shortModelDescription;
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

}
