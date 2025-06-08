package com.honda.galc.common.exception;

/**
 * 
 * <h3>InvalidSheetException Class description</h3>
 * <p> InvalidSheetException description </p>
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
 * @author Hemant Kumar<br>
 * March 28, 2019
 *
 *
 */
public class InvalidSheetException extends SystemException{

	private static final long serialVersionUID = 1L;
	
	public InvalidSheetException(String aMessage) {
		super(aMessage);
	}
	
	public InvalidSheetException(String aMessage, Throwable aDetail) {
		super(aMessage, aDetail);
	}
}
