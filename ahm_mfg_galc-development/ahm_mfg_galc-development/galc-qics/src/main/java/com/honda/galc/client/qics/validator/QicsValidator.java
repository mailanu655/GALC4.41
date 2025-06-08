package com.honda.galc.client.qics.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * <h3>QicsValidator Class description</h3>
 * <p> QicsValidator description </p>
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
 * Apr 25, 2012
 *
 *
 */
public class QicsValidator {
	private static final String REPAIR_TIME_MSG = "Repair Time is invalid";
	private static final String REPAIR_METHOD_MSG = "Repair Method is invalid";
	private static final String ASSOCITATE_NUMBER_MSG = "Associate Number is invalid";
	private static final String REPAIR_PROBLEM_MSG = "Actual Problem is invalid";
	private static final String COMMENT_MSG = "Comment is invalid";

	public static final int MAX_INPUT_ASSOCIATE_NUMBER_LENGTH = 11;

	public static List<String> validateRepairTime(Object value) {
		if(value == null || NumberUtils.toInt(value.toString(), 0) == 0)
			return getMessages(REPAIR_TIME_MSG);
		else return getEmptyMessages();
	}
	
	public static List<String> validateProductNumber(Object value) {
		return getEmptyMessages();
	}
	
	public static List<String> validateRepairMethod(Object value) {
		return validate(value,REPAIR_METHOD_MSG);
	}
	
	public static List<String> validateRepairProblem(Object value) {
		return validate(value,REPAIR_PROBLEM_MSG);
	}
	
	public static List<String> validateAssociateNumber(Object value) {
		return validate(value,ASSOCITATE_NUMBER_MSG);
	}
	
	private static List<String> validate(Object value, String errorMsg) {
		if(value == null || value.toString().trim().length()  == 0)
			return getMessages(errorMsg);
		else return getEmptyMessages();
	}
	
	private static List<String> getEmptyMessages() {
		return new ArrayList<String>();
	}
	
	private static List<String> getMessages(String errorMsg) {
		List<String> messages = getEmptyMessages();
		messages.add(errorMsg);
		return messages;
	}

	public static List<String> validateComment(Object comment) {		
		return validate(comment,COMMENT_MSG);
	}

}
