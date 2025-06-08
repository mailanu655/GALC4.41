package com.honda.galc.test.device;

import com.honda.galc.data.DataContainer;


/**
 * <h3>DeviceSimulator</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * 
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
 * <TD>Paul Chou</TD>
 * <TD>Feb 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial version</TD>
 * </TR>
 * </TABLE>
 */
public interface ITestListener {
	public DataContainer waitForData(long milliSeconds);
}
