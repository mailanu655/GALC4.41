
package com.honda.galc.common.exception;

/**
 * 
 * <h3>InvalidExcelFileException Class description</h3>
 * <p> InvalidExcelFileException description </p>
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
 * @author Shambhu Singh<br>
 * April 28, 2019
 *
 *
 */
public class InvalidExcelFileException extends SystemException{

	private static final long serialVersionUID = 1L;
	
	public InvalidExcelFileException(String aMessage) {
		super(aMessage);
	}
	
	public InvalidExcelFileException(String aMessage, Throwable aDetail) {
		super(aMessage, aDetail);
	}
}
