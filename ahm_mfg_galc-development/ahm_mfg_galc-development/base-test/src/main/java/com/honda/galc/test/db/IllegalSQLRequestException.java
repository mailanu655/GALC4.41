package com.honda.galc.test.db;

import com.honda.galc.common.exception.SystemException;


/**
 * <h3>IllegalSQLRequest is thrown when SQL skelton can not convert actual SQL.</h3>
 * <h4>Description</h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>T.Shimode</TD>
 * <TD>(2001/02/06 14:51:09)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/11/02 14:00:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 18, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006.1</TD>
 * <TD>Add serialization ID</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author T.Shimode
 */

public class IllegalSQLRequestException extends SystemException {
	public     static final long serialVersionUID = 4687241776655921430L;  // @JM006.1

/**
 * Constractor
 * @param aMesssage Message ID of Logger.
 */
public IllegalSQLRequestException(String aMessage) {
	super(aMessage);
}
/**
 * Constractor
 * <P>
 * @param aMesssage Message ID of Logger.
 * @param aDetail Throwable
 */
public IllegalSQLRequestException(String aMessage, Throwable aDetail) {
	super(aMessage, aDetail);
}
}
