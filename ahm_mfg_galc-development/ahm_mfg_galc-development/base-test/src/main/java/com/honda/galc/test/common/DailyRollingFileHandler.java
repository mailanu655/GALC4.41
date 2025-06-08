package com.honda.galc.test.common;

import java.io.IOException;

/**
 * 
 * <h3>Daily Rolling File Handler</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>The daily file rolling handler is a subclass of TimeRollingFileHandler.
 * The difference is that it creates 1 log file each day. In log file
 * name, there is no "__HH_MM_SS_XXX" section. </p>
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
 * <TD>Guang Yang</TD>
 * <TD>May 6, 2008</TD>
 * <TD>Ver 1.0</TD>
 * <TD>@GY20080506</TD>
 * <TD>Added Tivoli Logging</TD>
 * </TR>
 * </TABLE>
 */
public class DailyRollingFileHandler extends TimeRollingFileHandler {

	public DailyRollingFileHandler(String baseFileName, int rollHour) throws IOException {
		super(baseFileName, rollHour);
	}

	public String buildFileName() {
		String fileName = super.buildFileName();
		return fileName.substring(0, fileName.length() - 18) + ".log";
	}

}
