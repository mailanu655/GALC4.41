package com.honda.galc.client.datacollection.state;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Action</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public enum Action {
	NONE, /* No action.*/
	INIT, /* State initialization */
	RECEIVED, /* Received data */
	OK, /* Verification OK */
	NG, /* Failed verification */
	MISSING, /* Missing part */
	COMPLETE, /* Completed work on current state e.g. before transit to another state. 
	             Could be used to sign for Abort Job and set total torque status etc. */
	ABORT, /* Abort the operation on current state, for example, max measurement attempts exceeded*/
	SKIP_PART, /* User action to skip part */
	SKIP_PRODUCT, /* User action to skip Engine/Car */
	NO_ACTION, /* Play sound indicating no user action required for a product */
	CANCEL, /* User action to cancel operation */
	REJECT,
	ERROR, /* To notify client errors */
	MESSAGE,  /* To broadcasting message */ 
	REPAIR,
	OK_WAIT, /* Verification OK but wait for another signal to perform update operation */
	BYPASS, /* Manual action */
	AUTO	/* Online action */
	
}
