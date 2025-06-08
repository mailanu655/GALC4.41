package com.honda.galc.device.simulator.client;

import com.honda.galc.data.DataContainer;


/**
 * <h3>IEiSender</h3>
 * <h4> The interface must implement by EI Sender class </h4>
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
 * <TD>Oct. 24, 2008</TD>
 * <TD> 0.0.1 </TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public interface IEiSender {
	public DataContainer send(DataContainer senderDc);
}