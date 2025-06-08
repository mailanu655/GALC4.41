package com.honda.galc.service;

import static com.honda.galc.service.recipe.RecipeErrorLevel.EXCEPTION;
import static com.honda.galc.service.recipe.RecipeErrorLevel.NORMAL;
import static com.honda.galc.service.recipe.RecipeErrorLevel.WARNING;

import org.apache.commons.lang.StringUtils;

public enum ServiceErrorCode implements IErrorCode{

	NORMAL_REPLY("01", "Product Id:# OK ", NORMAL),
	INVALID_REF("02", "Invalid Product Id:# ", EXCEPTION),
	INVALID_PRODUCT("03", "Product Id:# is not valid for the lot",EXCEPTION),
	PROCESSED_REF("04", "Product Id:# already processed ",WARNING),
	INVALID_PP("05", "Invalid Process Point Id:# ", EXCEPTION),
	DUPLICATE_REF("06", "Duplicate Product Id:# ",WARNING),
	MISSING_DC_DATA("07", "Missing or invalid required data in data container", EXCEPTION),
	INCORRECT_SPEC_CODE("08", "Spec Code does not match expected Spec Code ",EXCEPTION),
	NO_SPEC_CODE("09", "No Product Spec Defined ",EXCEPTION),
	INITIALIZATION_ERROR("98", "Error occurred during service initialization", EXCEPTION),
	SYSTEM_ERR("99", "Unknown System error for Ref# ", EXCEPTION);
	
	private String code;
	private String description;
	private int severity;
	  			

	private ServiceErrorCode(String code, String description, int severity){
		this.code = code;
		this.description = description;
		this.severity = severity;
	}

	
	//=== getters ===
	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public int getSeverity() {
		return severity;
	}
	
	public boolean isWarning(){
		return getSeverity() == WARNING;
	}
	
	public static ServiceErrorCode fromCode(String code){
		for(ServiceErrorCode errCode : values()){
			if(errCode.getCode().equals(code))
				return errCode;
		}
		
		return null; 
	}
	
	public String getMessage(String productId){
		
		return (!StringUtils.isEmpty(productId) && getDescription().contains("#")) ? 
				getDescription().replace("#", productId) : getDescription();
	}
}
