package com.honda.galc.service.sequence;


/**
 * <h3>Enum description</h3>
 * <h4>Description</h4>
 * Define the product sequence error code and error message for AF On Web service  
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 */
/**
 * 
 *    
 * @version 1.0
 * @author Zhiqiang Wang
 * @since July 02, 2014
 */
public enum ProductSequenceCheckErrorCode {

	NORMAL_REPLY("1", "The request was processed successfully."), //this is the normal case
	ERROR_PRODUCT_IN_INVALID_LINE("2", "Product in wrong Line."),
	ERROR_MULTIPLE_PREV_IN_PROCESS_PRODUCT("3", "Multiple previous in process products.");
	
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


	private String code;
	private String description;
	  			

	private ProductSequenceCheckErrorCode(String code, String description){
		this.code = code;
		this.description = description;
	}

	
}
