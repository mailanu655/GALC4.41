package com.honda.galc.service.recipe;

import static com.honda.galc.service.recipe.RecipeErrorLevel.EXCEPTION;
import static com.honda.galc.service.recipe.RecipeErrorLevel.NORMAL;
import static com.honda.galc.service.recipe.RecipeErrorLevel.WARNING;

public enum WETrackingErrorCode {

	Next_Lot_Normal("01", "OK, No Problems ", NORMAL), //this is the normal case
	Invalid("03", "Invalid KD Lot No. Request Ref#", EXCEPTION),
	No_Next_Lot("30", "No Next KD Lot No. For Ref#", EXCEPTION),
	Duplicate_Request("31", "Already Requested KD Lot No. Ref# ", EXCEPTION),
	Missing_Build_Attributes("32","Build Attributes missing for Ref#",EXCEPTION),
	
	Product_Complete_Normal("51", "OK, No Problems ", NORMAL), //this is the normal case
	Product_Exist("52", "Product already exists", EXCEPTION),
	Invalid_Lot_Number("53", "Invalid KD Lot No. Request Ref#", EXCEPTION),
	Machine_Type_Mismatch("54", "Machine type mismatch", EXCEPTION),
	Expected_Lot_Skipped("55", "KD Lot No. Result was not expected KD Lot No.", WARNING),
	Missing_PreProduction_lot("56", "Missing PreProduction Lot.", EXCEPTION);

	private String code;
	private String description;
	private int severity;
	  			

	private WETrackingErrorCode(String code, String description, int severity){
		this.code = code;
		this.description = description;
		this.severity = severity;
	}

	
	//=== getter & setters ===
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getSeverity() {
		return severity;
	}


	public void setSeverity(int severity) {
		this.severity = severity;
	}
	
	public boolean isWarning(){
		return getSeverity() == WARNING;
	}
	
	public static WETrackingErrorCode fromCode(String code){
		for(WETrackingErrorCode errCode : values()){
			if(errCode.getCode().equals(code))
				return errCode;
		}
		
		return null; 
	}
}
