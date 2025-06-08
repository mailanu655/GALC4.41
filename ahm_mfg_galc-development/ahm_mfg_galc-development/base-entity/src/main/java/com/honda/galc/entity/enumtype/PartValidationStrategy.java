package com.honda.galc.entity.enumtype;

/**
 * 
 * <h3>PartValidationStrategy</h3>
 * <h3> enum description</h3>
 * <h4> Description </h4>
 * <p> PartValidationStrategy description </p>
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
 * @author L&T Infotech
 * Aug 28, 2017
 *
 */

public enum PartValidationStrategy {
	AT_LABEL_CHECK("antiTheftLabelCheck"), 
	CHECK_DIGIT_PART_SERIAL_NUMBER("isCheckDigitValid"), 
	MQ_NGT_MESSAGE("sendNgtMqMessage"), 
	PRODUCT_ID_VALIDATION("isPartSnMatchProductId"); 

	private String methodName; 

	private PartValidationStrategy(String methodName) { 
		this.methodName = methodName; 
	} 

	public String getMethodName() { 
		return methodName;                                                  
	} 

}
