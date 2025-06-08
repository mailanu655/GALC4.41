package com.honda.galc.service.on;

import static com.honda.galc.service.recipe.RecipeErrorLevel.EXCEPTION;
import static com.honda.galc.service.recipe.RecipeErrorLevel.NORMAL;
import static com.honda.galc.service.recipe.RecipeErrorLevel.WARNING;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>OnErrorCode</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OnErrorCode description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jun 7, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 7, 2017
 */
public enum OnErrorCode {

	Normal_Reply("01", "Product Id:# OK ", NORMAL), //this is the normal case
	Invalid_Ref("02", "Invalid Product Id:# ", EXCEPTION),
	Invalid_Product("03", "Product Id:# is not valid for the lot",EXCEPTION),
	Processed_Ref("04", "Product Id:# already processed ",WARNING),
	Duplicate_Ref("06", "Duplicate Product Id:# ",WARNING),
	No_Spec_Code("09", "No Product Spec Defined ",EXCEPTION),
	System_Err("99", "Unknown System error for Ref# ", EXCEPTION);
	
	private String code;
	private String description;
	private int severity;
	  			

	private OnErrorCode(String code, String description, int severity){
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
	
	public static OnErrorCode fromCode(String code){
		for(OnErrorCode errCode : values()){
			if(errCode.getCode().equals(code))
				return errCode;
		}
		
		return null; 
	}
	
	public String getMessage(String productId){
		
		return (StringUtils.isEmpty(productId) && getDescription().contains("#")) ? 
				getDescription().replace("#", productId) : getDescription();
	}


}
