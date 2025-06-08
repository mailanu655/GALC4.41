package com.honda.galc.system.oif.svc.common;

/**
 * <h3>IOifService</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * The contract for OIF services
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@???</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
public interface IOifService {
	/**
	 * Close resources
	 */
	public void close();

	/**
	 * Commit and close resources
	 */
	public void closeAndCommit();

	/**
	 * Rollback and close resources
	 */
	public void closeAndRollback();

	/**
	 * Log mesage
	 * 
	 * @param msgId - message ID
	 * @param logMethod - source method
	 * @param aMessage - user message
	 */
	public void log(String msgId, String logMethod, String aMessage);

	/**
	 * Log error message
	 * 
	 * @param msgId - message ID
	 * @param logMethod - source method
	 * @param aMessage - user message
	 */
	public void logError(String msgId, String method, String aMessage);

	/**
	 * Checks if debug is set
	 * 
	 * @return debug state
	 */
	public boolean isDebug();
}
