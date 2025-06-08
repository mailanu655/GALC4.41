package com.honda.galc.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.ILogLevelNotification;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.GalcStation;

/**
 * 
 * <h3>MainWindow Class description</h3>
 * <p> MainWindow is main window class for all GALC clients. All Clients have to subclass MainWindow.
 * When launching the window, it automatically gets the data from Application Context and adds
 *  the application menu tree for the current terminal. It also subscribes LogLevelNotification 
 *  and when current terminal's loglevel is changed from outside applications, it updates right away. 
 *  This allows to change log level dynamically for troubleshooting </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 26, 2010
 *
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public abstract class MainWindow extends JFrame implements ActionListener, ILogLevelNotification {


	private static final long serialVersionUID = 1L;

	protected ApplicationContext applicationContext;

	protected Application application;

	private boolean statusPanelFlag = true;

	protected StatusMessagePanel statusMessagePanel = null;

	protected JPanel clientPanel = new JPanel();

	private JMenuItem switchUserMenuItem = new JMenuItem("Switch User");

	protected JMenuItem logoutMenuItem = new JMenuItem("Logout");

	private JMenuItem closeMenuItem = new JMenuItem("Close");

	private JMenuItem exitMenuItem = new JMenuItem("Exit");

	private JMenuItem logLevelMenuItem ;

	private JMenu systemMenu;

	private Logger logger;

	// delayed message call displayDelayedMessage to display
	private String delayedMessage;

	// delayed message call displayDelayedErrorMessage to display
	private String delayedErrorMessage; 


	public MainWindow(ApplicationContext appContext, Application application) {

		this(appContext,application,false);

	}

	/**
	 * 
	 * @param appContext
	 * @param statusPanelFlag
	 */

	public MainWindow(ApplicationContext appContext, Application application,boolean statusPanelFlag) {

		this.applicationContext = appContext;
		this.application = application;
		this.statusPanelFlag = statusPanelFlag;

		initComponents();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		logger = Logger.getLogger(getLoggerName());

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt) {
				close();
			}

			public void windowOpened(WindowEvent e) {
				notifyWindowOpened();
			}

			public void windowIconified(WindowEvent e) {
				isNotWindowMinimize();
				getLogger().gui("Window " + e.getComponent().getName() + " minimized");
			}

			public void windowDeiconified(WindowEvent e) {
				getLogger().gui("Window " + e.getComponent().getName() + " restored");
			}
		});

	}

	public static JFrame getFrame(){
		Component focusOwner = KeyboardFocusManager.
				getCurrentKeyboardFocusManager().
				getPermanentFocusOwner();
		if (focusOwner != null && focusOwner instanceof MainWindow) {
			return (JFrame)((MainWindow)focusOwner).getRootPane().getParent();
		}else return null;
	}

	public static MainWindow getFocusedMainWindow() {
		Component focusOwner = KeyboardFocusManager.
				getCurrentKeyboardFocusManager().
				getPermanentFocusOwner();
		if(focusOwner == null || !(focusOwner instanceof JComponent)) return null;
		Container container = ((JComponent)focusOwner).getTopLevelAncestor();
		if (container != null && container instanceof MainWindow) {
			return (MainWindow)container;
		}else return null;
	}

	/**
	 * create Application Menu Tree
	 * it wraps each ApplicationMenuEntry into an ApplicationMenuAction and the action performs
	 * the menu entry action to launch the corresponding applicaiton 
	 * @return a Menu tree
	 */
	protected JMenu createApplicationMenu() {

		JMenu applicationMenu = new JMenu("Application Menu");
		Map<Integer,JMenuItem> menuItems = new HashMap<Integer,JMenuItem>();
		applicationMenu.setName("Application Menu");

		for(ApplicationMenuEntry menuEntry : applicationContext.getApplicationMenus()){

			JMenuItem menuItem = createMenuItem(menuEntry);
			menuItems.put(menuEntry.getId().getNodeNumber(),menuItem);
			if(menuEntry.getParentNodeNumber() == 0) applicationMenu.add(menuItem);
			else {
				JMenuItem parentMenuItem = menuItems.get(menuEntry.getParentNodeNumber());
				if(parentMenuItem != null) parentMenuItem.add(menuItem);
			}

		}

		for(JMenuItem menuItem : menuItems.values()) {
			if(menuItem.getComponentCount() == 0) menuItem.addActionListener(this);
		}

		return applicationMenu;

	}

	protected JMenuItem createMenuItem(ApplicationMenuEntry menuEntry) {

		JMenuItem menuItem = null;
		if(menuEntry.isLeafNode()) {
			menuItem = new JMenuItem(new ApplicationMenuAction(menuEntry));
		}else menuItem = new JMenu(menuEntry.getNodeName());
		menuItem.setName(menuEntry.getNodeName());
		return menuItem;

	}



	protected void initComponents() {

		if(application == null)
			this.setTitle("GALC Client - " + applicationContext.getTerminalDescription());
		else this.setTitle("GALC Client - " + application.getApplicationId() + " : " + application.getWindowTitle());
		setName(application.getApplicationId());
		createMenuBars();

		this.getContentPane().add(initMainPanel());

		AnnotationProcessor.process(this);

	}

	public Logger getLogger() {
		return logger;
	}

	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setName("MainPanel");
		clientPanel.setLayout(new BorderLayout());
		mainPanel.add(clientPanel,BorderLayout.CENTER);
		if(this.statusPanelFlag || getApplicationPropertyBean().hasStatusMessagePanel()) {
			mainPanel.add(initStatusMessagePanel(),BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	protected JPanel initStatusMessagePanel() {
		statusMessagePanel = new StatusMessagePanel();
		statusMessagePanel.getStatusPanel().setUser(applicationContext.getUserId());
		return statusMessagePanel;
	}

	public void setClientPanel(Component aPanel) {
		clientPanel.add(aPanel,BorderLayout.CENTER);
	}

	public void clearClientPanel() {
		clientPanel.removeAll();
	}

	protected void createMenuBars() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setName("MainWindowMenuBar");
		menuBar.add(createSystemMenu());
		menuBar.add(createApplicationMenu());
		createCustomizedMenus(menuBar);
		menuBar.add(createAboutMenu());
		getRootPane().setJMenuBar(menuBar);
	}


	protected JMenu createSystemMenu() {
		systemMenu = new JMenu("System");
		switchUserMenuItem.setName("SwitchUserMenu");
		logoutMenuItem.setName("LogoutMenu");
		closeMenuItem.setName("CloseMenu");
		exitMenuItem.setName("ExitMenu");
		switchUserMenuItem.addActionListener(this);
		logoutMenuItem.addActionListener(this);
		closeMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		systemMenu.addActionListener(this);

		systemMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {            	
				enableDisableSystemMenuOptions();
			}
		});
		systemMenu.setName("SystemMenu");
		if(getApplicationPropertyBean().isSwitchUserSystemMenuEnabled())
			systemMenu.add(switchUserMenuItem);
		if(getApplicationPropertyBean().isLogoutSystemMenuEnabled())
			systemMenu.add(logoutMenuItem);
		systemMenu.add(closeMenuItem);
		systemMenu.add(exitMenuItem);
		return systemMenu;
	}

	protected JMenuItem getSwitchUserMenu() {
		return switchUserMenuItem;
	}

	protected JMenuItem getCloseMenu() {
		return closeMenuItem;
	}

	protected JMenuItem getExitMenu() {
		return exitMenuItem;
	}

	protected JMenu getSystemMenu() {
		return systemMenu;
	}

	public JMenuItem getLogoutMenuItem() {
		return logoutMenuItem;
	}

	protected JMenu createAboutMenu() {
		JMenu menu = new JMenu("About");
		menu.add(createAboutMenuItem("SERVER URL   : " + applicationContext.getArguments().getServerURL()));
		menu.add(createAboutMenuItem("SERVER BUILD : " + applicationContext.getServerBuildLabel()));
		menu.add(createAboutMenuItem("CLIENT BUILD : " + applicationContext.getClientBuildLabel()));
		menu.add(createAboutMenuItem("HOST NAME   : " + applicationContext.getHostName()));
		try {
			menu.add(createAboutMenuItem("ASSET       : " + GalcStation.getHostName()));
		} catch (Exception e) {
			getLogger().error(e, "Unable to get Client Hostname. Please Contact IS");
		}
		menu.add(createAboutMenuItem("USE CACHE   : " + (applicationContext.getArguments().isUseCache()?"Yes" : "No")));
		menu.add(createAboutMenuItem("LOCAL IP    : " + applicationContext.getLocalHostIp()));
		menu.add(createAboutMenuItem("LOCAL PORT  : " + applicationContext.getTerminal().getPort()));
		menu.add(createAboutMenuItem("ROOT DIR    : " + applicationContext.getArguments().getRootDir()));
		menu.add(createAboutMenuItem("JAVA        : " + System.getProperty("java.vendor") + " | " + System.getProperty("java.version") + " | " + System.getProperty("java.home")));
		menu.add(logLevelMenuItem = createAboutMenuItem("LOG LEVEL   : " + LogContext.getContext().getApplicationLogLevel()));
		return menu;
	}

	protected JMenuItem createAboutMenuItem(String text) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setFont(new Font("Lucida Sans Typewriter",Font.PLAIN,12));
		return menuItem;
	}

	/**
	 * Individual client can add client specific menu from here
	 * @param menuBar
	 */
	protected void createCustomizedMenus(JMenuBar menuBar) {

	}

	public StatusMessagePanel getStatusMessagePanel() {
		return this.statusMessagePanel;
	}

	public void setStatusMessagePanel(StatusMessagePanel statusPanel) {
		this.statusMessagePanel = statusPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == switchUserMenuItem) {
			getLogger().info("Switch user menu item clicked");
			switchUser();
		}
		else if(e.getSource() == closeMenuItem) {
			getLogger().info("Close menu item clicked");
			close();
		}
		else if(e.getSource() == exitMenuItem) {
			getLogger().info("Exit menu item clicked");
			exit();
		}
		else if(e.getSource() == logoutMenuItem) {
			getLogger().info("Logout menu item clicked");
			logout();
		}
	}

	protected  void enableDisableSystemMenuOptions()
	{
		getLogger().info("System menu item clicked");
	}

	public void clearMessage(){
		this.getStatusMessagePanel().setErrorMessageArea(null);
	}

	public void clearStatusMessage() {
		this.getStatusMessagePanel().setStatusMessage(null);
	}

	public void setErrorMessage(String errorMessage){
		this.getStatusMessagePanel().setErrorMessageArea(errorMessage);
	}

	public void displayException(Exception e) {
		if(e == null) this.clearMessage();
		else {
			this.setErrorMessage(e.getMessage() + " " + getStackTrace(e));
		}
	}

	public String getStatusMessage() {
		return this.getStatusMessagePanel().getStatusMessage();
	}

	public void setStatusMessage(String message) {
		this.getStatusMessagePanel().setStatusMessage(message);
	}

	public String getMessage() {
		return this.getStatusMessagePanel().getMessage();
	}

	public void setMessage(String message) {
		this.getStatusMessagePanel().setMessage(message);
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected void switchUser() {
		switchUser(true, true);
	}


	protected void logout() {
		switchUser(false, false);
	}
	public void switchUser(boolean allowExit, boolean cancelOption) {
		ClientMain.getInstance().switchUser(this,allowExit, cancelOption);
		if(statusMessagePanel != null) statusMessagePanel.getStatusPanel().setUser(applicationContext.getUserId());
	}

	public String getUserId() {
		return getApplicationContext().getUserId();
	}

	protected void close() {
		ClientMain.getInstance().close(this);
	}

	protected void notifyWindowOpened() {

	}

	public void cleanUp() {
	}

	protected void exit() {
		if(MessageDialog.confirm(this, "Are you sure that you want to exit?"))
			ClientMain.getInstance().exitApplication(0);
	}

	public void enableSystemMenu(boolean flag) {

		systemMenu.setEnabled(flag);

	}

	public void handleException (Exception e) {

		this.setErrorMessage(e.getMessage() + " " + getStackTrace(e));

	}

	@EventTopicSubscriber(topic="ILogLevelNotification")
	public void onLogLevelNotificationEvent(String event, Request request) {
		try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void execute(String componentId, String level){

		if(componentId.equals(applicationContext.getHostName())) {
			PropertyService.updateProperty(componentId, LogLevel.LOG_LEVEL, level);
			Logger.updateLogLevel(LogLevel.valueOf(level));
			LogContext.getContext().setApplicationLogLevel(LogLevel.valueOf(level));
			if(logLevelMenuItem != null )
				logLevelMenuItem.setText("LOG LEVEL   : " + LogContext.getContext().getApplicationLogLevel());
			Logger.getLogger().info("Log Level is updated to " + level);
		}else {
			if(Logger.hasLogger(componentId)) {
				Logger logger = Logger.getLogger(componentId);
				logger.setLogLevel(LogLevel.valueOf(level));
				PropertyService.updateProperty(componentId, LogLevel.LOG_LEVEL, level);
				Logger.getLogger().info("Log Level for category " + componentId + " is updated to " + level);
			}
		}
	}

	public String getApplicationProperty(String propertyName) {
		return PropertyService.getProperty(application.getApplicationId(), propertyName);
	}

	public String getLoggerName(){
		return application.getApplicationId() + getApplicationPropertyBean().getNewLogSuffix();
	}

	public ProductType getProductType() {
		return applicationContext.getProductTypeData().getProductType();
	}

	public ApplicationPropertyBean getApplicationPropertyBean() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, application.getApplicationId());
	}
	public String getClientHostName() {
		return applicationContext.getHostName();
	}

	public Cursor getWaitCursor() {
		return Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	}

	public Cursor getDefaultCursor() {
		return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	}

	public void setWaitCursor() {
		setCursor(getWaitCursor());
	}

	public void setDefaultCursor() {
		setCursor(getDefaultCursor());
	}

	protected String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	public void setDelayedMessage(String message) {
		this.delayedMessage = message;
	}

	public void setDelayedErrorMessage(String message){
		this.delayedErrorMessage = message;
	}

	public void setWarningMessage(String warningMessage){
		this.getStatusMessagePanel().setWarningMessageArea(warningMessage);
	}

	public boolean displayDelayedMessage() {
		boolean flag = false;
		if(delayedErrorMessage != null){
			setErrorMessage(delayedErrorMessage);
			flag = true;
		}else if(delayedMessage != null){
			setMessage(delayedMessage);
			flag = true;
		}
		delayedErrorMessage = null;
		delayedMessage = null;
		return flag;
	}

	public void switchUser(boolean allowExit, boolean cancelOption, boolean enableChangePsswd) {
		ClientMain.getInstance().switchUser(this,allowExit, cancelOption, enableChangePsswd);
		if(statusMessagePanel != null) statusMessagePanel.getStatusPanel().setUser(applicationContext.getUserId());
	}

	public void isNotWindowMinimize() {
		if(getApplicationPropertyBean().isAllowWindowResize()) {
			setExtendedState(Frame.NORMAL);
		}
	}
}
