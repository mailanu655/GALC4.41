package com.honda.galc.oif.dto;

import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpecId;
import com.honda.galc.util.GPCSData;

/*
*Plant_Code_FRAME |K|C|0|4|U|
*Model_Year_Code  |k|C|4|1|U|
*Model_Code       |K|C|5|3|U|
*Model_Type_Code  |K|C|8|3|U|
*Model_Option_Code|K|C|11|3|U|
*Ext_Color_Code   |K|C|14|10|U|
*Int_Color_Code   |K|C|24|2|U|
*Effective_Date   |K|C|26|8|U|
*Price_Type       |F|C|34|1|U|
*Currency         |F|C|35|3|U|
*Price            |F|C|38|16|U|
*Quotation_No     |F|C|54|6|U|
*/

//Additional fields not in DailyDepartmentSchedule
public class FrameMTOCPriceMasterSpecDTO { 
	
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
	
	@GPCSData("EXT_COLOR_CODE")
	private String extColorCode;
	
	@GPCSData("INT_COLOR_CODE")
	private String intColorCode;
		
	@GPCSData("EFFECTIVE_DATE")
	private String effectiveDate;
	
	@GPCSData("PRICE_TYPE")
	private String priceType;
	
	@GPCSData("CURRENCY")
	private String currency;
	
	@GPCSData("PRICE")
	private String price;
	
	@GPCSData("QUOTATION_NO")
	private String quotationNo;

	
	
	public FrameMTOCPriceMasterSpecDTO() {
	}

// Copy Constructor
	public FrameMTOCPriceMasterSpecDTO(FrameMTOCPriceMasterSpecDTO mtoSpec) {
		this.plantCodeFrame = mtoSpec.getPlantCodeFrame();
		this.modelYearCode = mtoSpec.getModelYearCode();
		this.modelCode = mtoSpec.getModelCode();
		this.modelTypeCode = mtoSpec.getModelTypeCode();
		this.modelOptionCode = mtoSpec.getModelOptionCode();
		this.extColorCode = mtoSpec.getExtColorCode();
		this.intColorCode = mtoSpec.getIntColorCode();
		this.effectiveDate = mtoSpec.getEffecticeDate();
		this.priceType = mtoSpec.getPriceType();
		this.currency = mtoSpec.getCurrency();
		this.price = mtoSpec.getPrice();
		this.quotationNo = mtoSpec.getQuotationNo();
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

	public String getEffecticeDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}
	
	public FrameMTOCPriceMasterSpec deriveFrameMTOCPriceMasterSpec() {
		FrameMTOCPriceMasterSpec frameMTOCPriceMasterSpec = new FrameMTOCPriceMasterSpec(); 
		frameMTOCPriceMasterSpec.setId(deriveID());
		frameMTOCPriceMasterSpec.setCurrency(currency);
		frameMTOCPriceMasterSpec.setPrice(price);
		frameMTOCPriceMasterSpec.setPriceType(priceType);
		frameMTOCPriceMasterSpec.setQuotationNo(quotationNo);
		return frameMTOCPriceMasterSpec;
	}

	public FrameMTOCPriceMasterSpecId deriveID(){
		FrameMTOCPriceMasterSpecId frameMTOCPriceMasterSpecId = new FrameMTOCPriceMasterSpecId();
		frameMTOCPriceMasterSpecId.setPlantCodeFrame(plantCodeFrame);
		frameMTOCPriceMasterSpecId.setModelYearCode(modelYearCode);
		frameMTOCPriceMasterSpecId.setModelCode(modelCode);
		frameMTOCPriceMasterSpecId.setModelTypeCode(modelTypeCode);
		frameMTOCPriceMasterSpecId.setModelOptionCode(modelOptionCode);
		frameMTOCPriceMasterSpecId.setExtColorCode(extColorCode);
		frameMTOCPriceMasterSpecId.setIntColorCode(intColorCode);
		frameMTOCPriceMasterSpecId.setEffectiveDate(effectiveDate);
		return frameMTOCPriceMasterSpecId;
	}
}
