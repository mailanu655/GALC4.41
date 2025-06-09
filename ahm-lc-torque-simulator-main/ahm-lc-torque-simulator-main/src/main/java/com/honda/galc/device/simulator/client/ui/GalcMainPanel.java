package com.honda.galc.device.simulator.client.ui;


/**
 * <h3>Class description</h3>
 * This class can be used as the super class of client applications.
 * It defines some general behaviors that could be used by 
 * most applications.
 * <h4>Description</h4>
 * Subclasses of this class implements actions defined by
 * the menu XML files. For example, in the XML file there
 * is a menu item definition <br><br>
 * 
 *   &lt;menuitem name="About" action="aboutApplication"/>  <br><br>
 *   
 * When users click on this menu item, the method
 * aboutApplication() defined by this class or it subclasses
 * will be called. 
  
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Jun 22, 2007</TD>
 * <TD>1.0</TD>
 * <TD>20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * <br>
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcMainPanel extends GalcPanel {

	private static final long serialVersionUID = 1L;
	private String applicationInfo;
	
	public GalcMainPanel() {
		super();
		setApplicationInfo("HCM GALC Application.");
	}

/**
 * Display a popup dialog with the information about the application.
 *
 */	
	public void aboutApplication() {
		showMessageDialog(getApplicationInfo()); 
	}
	
/**
 * Exit current application.
 *
 */
	public void exitSystem() {
		getWindow().exitApplication();
	}

/**
 * Get information about current application.
 * @return A string describes current application.
 */	
	public String getApplicationInfo() {
		return applicationInfo;
	}

	public void setApplicationInfo(String applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
}
