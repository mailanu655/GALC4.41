package com.honda.galc.device.simulator.client.ui;

import javax.swing.JPanel;

/**
 * <h3>Class description</h3>
 * This class can be used as the super class of client panels.
 * It defines some general behaviors that could be used by 
 * other applications.
 * <h4>Description</h4>
 * 
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
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private GalcController controller;

    public GalcPanel() {
		super();
		initializeController();
	}

/**
 * Initialize the controller. Subclasses can overwrite this method 
 * to use a controller other than GalcController. But the controller
 * must be a subclass of GalcController.
 *
 */
	public void initializeController() {
		controller = new GalcController();
	}
    
/**
 * Get top level GalcWindow.
 * @return GalcWindow for current application.
 */
    public GalcWindow getWindow() {
		return (GalcWindow) getTopLevelAncestor();
	}

/**
 * Display aMessage in status bar of GalcWindow.
 * @param aMessage A string message to be displayed.
 * @param aType Type of the message.
 */
    public void displayMessage(String aMessage, int aType) {
		getWindow().displayMessage(aMessage, aType);
  	}

/**
 * Display normal message aMessage in status bar.
 * @param aMessage The message string.
 */
    public void displayMessage(String aMessage) {
		getWindow().displayMessage(aMessage);
	}

/**
 * Display a warning message in status bar. The message will be displayed 
 * with blinking yellow background.
 * @param aMessage The message string.
 */
    public void displayWarning(String aMessage) {
		getWindow().displayWarning(aMessage);
	}

/**
 * Display an error message in status bar. The message will be displayed 
 * with blinking red background.
 * @param aMessage The message string.
 */	public void displayError(String aMessage) {
		getWindow().displayError(aMessage);
	}

/**
 * Display a message string in a popup window and allow users to choose
 * Yes or No options.
 * @param message The message to be displayed.
 * @return an integer indicating the option selected by the user. 
 */
 	public int showConfirmDialog(String message) {
		return controller.showConfirmDialog(message);
	}

/**
 * Display a message string in a popup window as an error message.
 * @param message The message to be displayed.
 */
 	public void showErrorDialog(String message) {
 		controller.showErrorDialog(message);
	}

/**
 * Display a message string in a popup window.
 * @param message The string to be displayed.
 */ 	
	public void showMessageDialog(String message) {
		controller.showMessageDialog(message);
	}

/**
 * Display a message string in a popup window as warning message.
 * @param message The string to be displayed.
 */
	public void showWarningDialog(String message) {
		controller.showWarningDialog(message);
	}

	public int exitApplication() {
		return 0;
	}
	
	public GalcController getController() {
 		return controller;
 	}

 	public void setController(GalcController controller) {
 		this.controller = controller;
 	}
}
