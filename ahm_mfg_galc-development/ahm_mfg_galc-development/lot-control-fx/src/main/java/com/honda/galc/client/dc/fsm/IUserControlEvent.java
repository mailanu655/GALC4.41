package com.honda.galc.client.dc.fsm;
/**
 * <h3>IUserControlEvent</h3>
 * <h4>
 * State Machine interface for User control events.
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
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public interface IUserControlEvent {
	void skipPart();
	void skipCurrentInput();
	void skipProduct();
	void cancel();
	
	/**
	 * Classic style reject
	 *
	 */
	void reject();
	
	/**
	 * Default style reject 
	 *
	 */
	void reject1();

	void continueDelay();

}
