package com.honda.galc.script;

/**
 * 
 * <h3>AssignmentOperator</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AssignmentOperator description </p>
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
 * @author Paul Chou
 * Apr 11, 2011
 *
 */


public enum AssignmentOperator {
	AS("=")/* AS */, 
	AA("+=")/*AS Additive*/, 
	SA("-=") /*AS Subtraction*/,
	MA("*=")/*AS Multiplication*/, 
	DA("/=")/*AS Devision*/, 
	RA("%=")/*AS Remainder*/,
	BAA("&=")/*AS Bitwise AND*/, 
	BOEA("^=")/*AS Bitwise OR Exclusive*/, 
	BOIA("|=")/* AS Bitwise OR inclusive*/, 
	LSA("<<=")/*AS Signed Left Shift*/, 
	RSA(">>=")/*AS Signed Right Shift*/, 
	URSA(">>>=")/* AS Unsigned Right Shift*/;
	
	private String operator;
	AssignmentOperator(String operator){
		this.operator = operator;
	}

	public String getOperator(){
		return operator;
	}
}
