package com.honda.galc.service;
import org.apache.commons.lang.StringUtils;

import static com.honda.galc.service.recipe.RecipeErrorLevel.EXCEPTION;
import static com.honda.galc.service.recipe.RecipeErrorLevel.NORMAL;
import static com.honda.galc.service.recipe.RecipeErrorLevel.WARNING;

public enum PartSerialNumberErrorCode  implements IErrorCode{

		NORMAL_REPLY("01", "Part Serial Number:# OK ", NORMAL),
		INVALID_REF("02", "Invalid Part Serial Number:# ", EXCEPTION),
		DUPLICATE_REF("06", "Duplicate part, already installed on:# ", WARNING),
		INVALID_PART("10", "Unexpected part name:# ", WARNING),
		NO_RULE("11", "Missing Lot Control Rule", EXCEPTION),
		MASK_CHECK_FAILED("12", "Failed serial number validation for:# ", WARNING),
		MISSING_SUB_PROODUCT_TYPE("13", "Subproduct Type expected, but is null or blank", EXCEPTION),
		PART_CHECK_FAILED("14", "Part check failed the following checks:# ", WARNING),
		PART_CHECK_ERROR("15", "Error occurred during part checks", EXCEPTION),
		INVALID_RULE("16", "Invalid Lot Control Rule", EXCEPTION),
		INSTALL_PP_MISSING("17", "No Install process point map property is configured for process point:# ", EXCEPTION),
		ON_HOLD("18", "Sub Product is on hold", WARNING),
		INVALID_LINE("19", "Sub Product came from unexpected Line", WARNING);
	
		private String code;
		private String description;
		private int severity;
		  			
		private PartSerialNumberErrorCode(String code, String description, int severity){
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
		
		public static PartSerialNumberErrorCode fromCode(String code){
			for(PartSerialNumberErrorCode errCode : values()){
				if(errCode.getCode().equals(code))
					return errCode;
			}
			return null; 
		}
		
		public String getMessage(String msg){
			
			return (!StringUtils.isEmpty(msg) && getDescription().contains("#")) ? 
					getDescription().replace("#", msg) : getDescription();
		}
	}
