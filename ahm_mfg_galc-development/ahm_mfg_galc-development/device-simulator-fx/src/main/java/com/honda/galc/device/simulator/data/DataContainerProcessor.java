package com.honda.galc.device.simulator.data;

import com.honda.galc.data.DataContainer;


/**
 * 
 * <h3>DataContainerProcessor</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is the interface a class must be implemented in order to 
 * be used as the processor of messages received by the DataContainerSocketServer.
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
 * <TD>martinek</TD>
 * <TD>Jul 20, 2007</TD>
 * <TD></TD>
 * <TD>@JM200724</TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public interface DataContainerProcessor {
	
	/**
	 * This method should be thread safe, as the processor will be used
	 * to handle concurrent requests.
	 * @param dc -  may be null if no results are returned
	 * @return
	 */
	public DataContainer processDataContainer(DataContainer dc);

}
