package com.honda.galc.common.exception;

/**
 * 
 * <h3>LoginException Class description</h3>
 * <p> LoginException description </p>
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
 * May 9, 2011
 *
 *
 */
public class LoginException extends SystemException{

	public LoginException(String messageID) {
		super(messageID);
	}
	
	public LoginException(String messageID, Throwable detail) {
		super(messageID, detail);
	}

	private static final long serialVersionUID = 1L;

}
