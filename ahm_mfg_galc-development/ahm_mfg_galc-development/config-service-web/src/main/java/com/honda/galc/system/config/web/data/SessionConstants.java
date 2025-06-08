/*
 * Created on Jan 31, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.honda.galc.system.config.web.data;

/**
 * <h3></h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
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
 * <TD>Jan 31, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public interface SessionConstants {
	
	/**
	 * This session key refers to a list of Site Node objects
	 * which is used to build the process navigation tree.
	 */
	public static final String SITE_NODES = "SITE_NODES";

    /**
     * This is the key to the stored list of ApplicationMenuDataNode objects. 
     */
    public static final String SESSION_MENU_DATA = "SESSION_MENU_DATA";
    
}
