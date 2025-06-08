package com.honda.galc.service.recipe;

import static com.honda.galc.service.recipe.RecipeErrorLevel.EXCEPTION;
import static com.honda.galc.service.recipe.RecipeErrorLevel.NORMAL;
import static com.honda.galc.service.recipe.RecipeErrorLevel.WARNING;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>RecipeErrorCode</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeErrorCode description </p>
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
 * <TD>Jan 3, 2013</TD>
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
 * @since Jan 3, 2013
 */
public enum RecipeErrorCode {

	Normal_Reply("01", "Ref:# xxxxxxxxx - Next Ref# OK ", NORMAL), //this is the normal case
	Invalid_Ref("02", "Invalid Ref:# Request ", EXCEPTION),
	Skipped_Ref("03", "Skipped Ref:# ", WARNING),
	No_Recipe_Data("04", "No Recipe Data For Ref:# ", EXCEPTION),
	No_Next_Ref("05", "No Next Ref# For last Ref:# ", EXCEPTION),
	Duplicate_Ref("06", "Duplicate Ref:# Already written to PLC ",WARNING),
	Produced_Ref("07", "Produced Ref:# Already Produced Product ",WARNING),
	Mask_Oversize("08", "Part Mask oversize for Ref:# ",WARNING),
	No_Spec_Code("09", "No Product Spec Defined ",EXCEPTION),
	No_Af_On("10", "Not passed Af On", WARNING),
	
	Invalid_Pair("81","Invalid Pair Received from PLC ", WARNING),
	System_Err("99", "Unknown System error for Ref:# ", EXCEPTION),
	
	Rockwell_Device_NOT_FOUND("10","Can not find the Device",EXCEPTION),
	Rockwell_Normal_Reply("OK","Normal Code for Rockwell", NORMAL),
	Recipe_Download_Normal_Reply("OK","Normal Code for Recipe Download", NORMAL),
	Device_NOT_FOUND("10","Can not find the Device",EXCEPTION);
	private String code;
	private String description;
	private int severity;
	  			

	private RecipeErrorCode(String code, String description, int severity){
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
	
	public static RecipeErrorCode fromCode(String code){
		for(RecipeErrorCode errCode : values()){
			if(errCode.getCode().equals(code))
				return errCode;
		}
		
		return null; 
	}
	
	public String getDescription(String ref){
		return this.getDescription().replace("#", StringUtils.trimToEmpty(ref));
	}
}
