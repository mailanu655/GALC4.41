package com.honda.galc.device.simulator.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <h3>Class description</h3>
 * GalcWindow can be used by standalone applications or regular
 * GALC clients. It has a menu bar, a status bar and a center panel.
 * <h4>Description</h4>
 * </p>
 * GalcMenuBar<br>
 *   This is the menubar for the application. It reads menu definitions
 *   from XML files (for now). More information about GalcMenuBar is 
 *   available in the class file.
 * </p>
 * <p>
 * GalcStatusBar<br>
 *   The status bar locats on the bottom of the window. It has 4 areas.
 *   The first area is reserved for user name. 
 *   The second area is for messages. 
 *   The third area is current date.
 *   The forth area is current time.
 * </p>
 * <p> 
 * The center panel<br>
 *   The center part of the window is the area for client applications.
 *   Usually it is a subclass of GalcMainPanel and it is added to the 
 *   window by addCenterPanel().
 *</p> 
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

public class GalcWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String WINDOW_TITLE = "Window Title";
    public static final String HAS_MENUBAR = "Has Menubar";
    public static final String HAS_STATUSBAR = "Has Statusbar";
    public static final String SHOW_NAME_FIELD = "Show Name Field";
    public static final String SHOW_MESSAGE_FIELD = "Show Message Field";
    public static final String SHOW_DATE_FIELD = "Show Date Field";
    public static final String SHOW_TIME_FIELD = "Show Time Field";
	public static final String WINDOW_WIDTH = "Window Width";
	public static final String WINDOW_HEIGHT = "Window Height";
	public static final String WINDOW_LOCATION_X = "Window Location X";
	public static final String WINDOW_LOCATION_Y = "Window Location Y";

	public static final String DEFAULT_WINDOW_TITLE = "GALC Window";
	public static final int DEFAULT_WINODW_WIDTH = 800;
	public static final int DEFAULT_WINDOW_HEIGHT = 600;
	public static final int DEFAULT_WINDOW_LOCATION_X = 20;
	public static final int DEFAULT_WINDOW_LOCATION_Y = 20;

	private GalcController controller;
    private GalcMenuBar menuBar;
	private JPanel mainPanel;
	private GalcPanel centerPanel;
	private GalcStatusBar statusBar;

	
	public GalcWindow () {
		super();
		centerPanel = createCenterPanel();
		initializeWindow();
	}

	public GalcWindow(GalcPanel aPanel)
	{
		super();
		centerPanel = aPanel;
		initializeWindow();
	}

/**
 * Initialize the window.
 *
 */
	private void initializeWindow() {
		controller = centerPanel.getController();
		if(hasMenubar()) {
			createMenuBar();
			setJMenuBar(menuBar);
		}
		setMainPanel(createMainPanel());
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);
		setTitle(windowTitle());
		addCenterPanel(centerPanel);
		if(controller.getBooleanProperty(HAS_STATUSBAR)) {
			addStatusBar(createStatusBar());
			statusBar.getNameField().setVisible(controller.getBooleanProperty(SHOW_NAME_FIELD));
			statusBar.getMessageField().setVisible(controller.getBooleanProperty(SHOW_MESSAGE_FIELD));
			statusBar.getDateField().setVisible(controller.getBooleanProperty(SHOW_DATE_FIELD));
			statusBar.getTimeField().setVisible(controller.getBooleanProperty(SHOW_TIME_FIELD));
		}
		setPreferredSize(new Dimension(windowWidth(), windowHeight()));
		setLocation(windowLocationX(), windowLocationY());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(java.awt.event.WindowEvent e) {
	            exitApplication();
	        }
	    });
	}
	
	public void createMenuBar() {
		menuBar = new GalcMenuBar(getController());
	}
	
	public void resetMenuBar(String uri) {
		if(hasMenubar()) menuBar.resetMenuBar(uri);
	}
	
	public GalcPanel getCenterPanel() {
		return centerPanel;
	}
	
	private void setCenterPanel(GalcPanel aPanel) {
		centerPanel = aPanel;
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	public void setMainPanel(JPanel aPanel)	{
		mainPanel = aPanel;
	}
	
	public GalcStatusBar getStatusBar() {
		return statusBar;
	}
	
	private void setStatusBar(GalcStatusBar aStatusBar) {
		statusBar = aStatusBar;
	}
	
	private JPanel createMainPanel() {
		JPanel aPanel = new JPanel();
		aPanel.setName("Main Panel");
		return aPanel;
	}
	
	private GalcPanel createCenterPanel() {
		GalcPanel aPanel = new GalcPanel();
		aPanel.setName("Center Panel");
		return aPanel;
	}
	
	private GalcStatusBar createStatusBar() {
		GalcStatusBar aStatusBar = new GalcStatusBar();
		aStatusBar.setName("Status Bar");
		return aStatusBar;
	}
	
/**
 * Add aPanel to the window as the center panel.
 * @param aPanel The panel to be added to the window.
 */
	public void addCenterPanel(GalcPanel aPanel) {
		mainPanel.add(aPanel, BorderLayout.CENTER);
	}
	
/**
 * Add aStatusBar to the window.
 * @param aStatusBar The status bar to be added.
 */
	public void addStatusBar(GalcStatusBar aStatusBar) {
		setStatusBar(aStatusBar);
		mainPanel.add(aStatusBar, BorderLayout.SOUTH);
	}

/**
 * Display normal message aMessage on status bar.
 * @param aMessage The message string.
 */
	public void displayMessage(String aMessage) {
		if(statusBar != null) statusBar.displayMessage(aMessage);
	}

/**
 * Display aMessage on status bar of GalcWindow.
 * @param aMessage A string message to be displayed.
 * @param aType Type of the message.
 */
	public void displayMessage(String aMessage, int aType) {
		if(statusBar != null) statusBar.displayMessage(aMessage, aType);
	}

/**
 * Display a warning message on status bar. The message will be displayed 
 * with blinking yellow background.
 * @param aMessage The message string.
 */
	public void displayWarning(String aMessage) {
		if(statusBar != null) statusBar.displayWarning(aMessage);
	}
	
/**
 * Display an error message on status bar. The message will be displayed 
 * with blinking red background.
 * @param aMessage The message string.
 */	
	public void displayError(String aMessage) {
		if(statusBar != null) statusBar.displayError(aMessage);
	}

/**
 * Diaplay user name on status bar name field.
 * @param aName The name to be displayed.
 */	
	public void displayName(String aName) {
		if(statusBar != null) statusBar.displayName(aName);
	}
	
/**
 * Exit current application.
 *
 */	
	public void exitApplication() {
		if(statusBar == null) {
			if((centerPanel.exitApplication() == 0)) System.exit(0);
		} else {
			if((centerPanel.exitApplication() == 0) && (statusBar.exitApplication() == 0)) System.exit(0);
		}
	}
	
/**
 * Show the window. 
 *
 */
	public void showWindow()
	{
		this.pack();
		this.setVisible(true);
	}


	public boolean hasMenubar() {
		return controller.getBooleanProperty(HAS_MENUBAR);
	}

	public String windowTitle() {
		String title = controller.getProperty(WINDOW_TITLE);
		return (title != null) ? title : DEFAULT_WINDOW_TITLE;
	}
	
	public int windowWidth() {
		return controller.getIntProperty(WINDOW_WIDTH, DEFAULT_WINODW_WIDTH);
	}

	public int windowHeight() {
		return controller.getIntProperty(WINDOW_HEIGHT, DEFAULT_WINDOW_HEIGHT);
	}
		
	public int windowLocationX() {
		return controller.getIntProperty(WINDOW_LOCATION_X, DEFAULT_WINDOW_LOCATION_X);
	}
		
	public int windowLocationY() {
		return controller.getIntProperty(WINDOW_LOCATION_Y, DEFAULT_WINDOW_LOCATION_Y);
	}
		
	public GalcController getController() {
		return controller;
	}

	public void setController(GalcController controller) {
		this.controller = controller;
	}
} 