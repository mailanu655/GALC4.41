
package com.honda.galc.service.printing;

import java.io.OutputStream;

import com.honda.galc.data.DataContainer;

/**
 * <h3>Generic Printer Sender Interface</h3>
 * <h4>Description</h4>
 * Design contract for all senders to printer<br/>
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
 * <TD>R.Lasenko</TD>
 * <TD>Dec 07, 2007</TD>
 * <TD>@RL011</TD>
 * <TD>&nbsp;</TD>
 * <TD>Initial Creation</TD>
 * </TR>
 * </TABLE>
 */
public interface IPrinterDataAssembler {

	/**
	 * Accepts input as a DataContainer and sends output to printer
	 * 
	 * @param dataContainer - input DataContainer
	 * @throws BroadcastException
	 */
    public void	writeOutputFile(OutputStream os, DataContainer dc);

}
