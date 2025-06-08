package com.honda.galc.client.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.RelaunchUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.notification.service.ILogLevelNotification;
import com.honda.galc.notification.service.IMassMessageNotification;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.GalcStation;
import com.sun.glass.ui.Screen;

/**
 * 
 * <h3>MainWindow Class description</h3>
 * <p>
 * MainWindow is main window class for all GALC clients. All Clients have to
 * subclass MainWindow. When launching the window, it automatically gets the
 * data from Application Context and adds the application menu tree for the
 * current terminal. It also subscribes LogLevelNotification and when current
 * terminal's loglevel is changed from outside applications, it updates right
 * away. This allows to change log level dynamically for troubleshooting
 * </p>
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
 *         Mar 26, 2010
 * 
 * @author Suriya Sena
 *         Jan 22, 2014 - JavaFx migration
 * 
 */

public class MainWindow extends BorderPane 
	implements EventHandler<javafx.event.ActionEvent>, ILogLevelNotification, IMassMessageNotification {

	private static final long serialVersionUID = 1L;

	protected ApplicationContext applicationContext;

	protected Application application;

	private boolean statusPanelFlag = true;

	protected StatusMessagePane statusMessagePane = null;

	protected BorderPane clientPane = new BorderPane();

	private MenuItem switchUserMenuItem = UiFactory.createMenuItem("Switch User");

	private MenuItem closeMenuItem = UiFactory.createMenuItem("Close");

	private MenuItem exitMenuItem = UiFactory.createMenuItem("Exit");
	
	private MenuItem logoutMenuItem = UiFactory.createMenuItem("Logout");

	private MenuItem logLevelMenuItem;

	private Menu systemMenu;
	
	protected MenuBar menuBar;

	private Logger logger;
	
	private MassMessagePane massMessagePane = null;
	

	// delayed message call displayDelayedMessage to display
	private String delayedMessage;

	// delayed message call displayDelayedErrorMessage to display
	private String delayedErrorMessage;
	

	public MainWindow(ApplicationContext appContext, Application application) {
		this(appContext, application, false);
	}

	public MainWindow(ApplicationContext appContext, Application application,boolean statusPanelFlag) {
		super();

		this.applicationContext = appContext;
		this.application = application;
		this.statusPanelFlag = statusPanelFlag;
		EventBusUtil.register(this);

		initComponents();

		logger = Logger.getLogger(getLoggerName());
	}

	public static MainWindow getFocusedMainWindow() {
		return null;
	}

	/**
	 * create Application Menu Tree it wraps each ApplicationMenuEntry into an
	 * ApplicationMenuAction and the action performs the menu entry action to
	 * launch the corresponding application
	 * 
	 * @return a Menu tree
	 */
	protected Menu createApplicationMenu() {

		Menu applicationMenu = UiFactory.createMenu("Application Menu");
		Map<Integer, MenuItem> menuItems = new HashMap<Integer, MenuItem>();
		applicationMenu.setId("Application Menu");

		for (ApplicationMenuEntry menuEntry : applicationContext.getApplicationMenus()) {
			MenuItem menuItem = createMenuItem(menuEntry);
			menuItems.put(menuEntry.getId().getNodeNumber(), menuItem);
			if (menuEntry.getParentNodeNumber() == 0) {
				applicationMenu.getItems().add(menuItem);
			} else {
				MenuItem parentMenuItem = menuItems.get(menuEntry.getParentNodeNumber());
				if (parentMenuItem != null && parentMenuItem instanceof Menu) {
					((Menu) parentMenuItem).getItems().add(menuItem);
				}
			}
		}

		// As far as I can tell this code has no effect
		// for(MenuItem menuItem : menuItems.values()) {
		// if(menuItem.getComponentCount() == 0)
		// menuItem.addActionListener(this);
		// }

		return applicationMenu;
	}

	protected MenuItem createMenuItem(ApplicationMenuEntry menuEntry) {
		MenuItem menuItem = null;
		if (menuEntry.isLeafNode()) {
			menuItem = UiFactory.createMenuItem(menuEntry.getApplicationName());
			menuItem.setOnAction(new ApplicationMenuEventHandler(menuEntry));
		} else {
			menuItem = UiFactory.createMenu(menuEntry.getNodeName());
		}
		menuItem.setId(menuEntry.getNodeName());
		return menuItem;
	}

	protected void initComponents() {
		if (application == null) {
			ClientMainFx.getInstance().setWindowTitle("GALC Client - " + applicationContext.getTerminalDescription());
		} else {
			ClientMainFx.getInstance().setWindowTitle(application.getApplicationId(), "GALC Client - " + application.getApplicationId() + " : " + application.getWindowTitle());
		}

		setId(application.getApplicationId());
		initMainPanel();
		getStage().iconifiedProperty().addListener(new ChangeListener<Boolean>() { // listener for window minimize/restore
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				getLogger().gui("Window " + getId() + (newValue ? " minimized" : " restored"));
			}
		});
		
		// Check GAL489TBX 'system_info' to show Mass Awareness Message widget
		SystemPropertyBean property = getApplicationPropertyBean();
		if(property != null && property.isMassAwarenessMessageEnabled()){
			if(applicationContext.getProcessPoint() != null)
				initMassMessagePane();
		}

	}

	public Logger getLogger() {
		return logger;
	}

	private void initMainPanel() {
		if (this.getId() == null) {
			this.setId("MainPanel");
		}
		this.setTop(createMenuBars());
		this.setCenter(clientPane);

		if (this.statusPanelFlag) {
			this.setBottom(initStatusMessagePane());
		}
	}

	protected Pane initStatusMessagePane() {
		statusMessagePane = new StatusMessagePane();
		statusMessagePane.getStatusPane().setUser(applicationContext.getUserId());
		return statusMessagePane;
	}

	public void setClientPane(Node node) {
		clientPane.setCenter(node);
	}

	public void clearClientPane() {
		clientPane.getChildren().clear();
	}

	protected MenuBar createMenuBars() {
		menuBar = UiFactory.createMenuBar();
		menuBar.setId("MainWindowMenuBar");
		menuBar.getMenus().add(createSystemMenu());
		menuBar.getMenus().add(createApplicationMenu());
		createCustomizedMenus(menuBar);
		menuBar.getMenus().add(createAboutMenu());
		return menuBar;
	}

	protected Menu createSystemMenu() {
		systemMenu = UiFactory.createMenu("System");
		switchUserMenuItem.setId("SwitchUserMenu");
		closeMenuItem.setId("CloseMenu");
		exitMenuItem.setId("ExitMenu");
		logoutMenuItem.setId("Logout");
		switchUserMenuItem.setOnAction(this);
		closeMenuItem.setOnAction(this);
		exitMenuItem.setOnAction(this);
		systemMenu.setOnAction(this);
		logoutMenuItem.setOnAction(this);
		
		systemMenu.setId("SystemMenu");
		systemMenu.getItems().addAll(switchUserMenuItem,logoutMenuItem, closeMenuItem,new SeparatorMenuItem(), exitMenuItem);
		createCustomizedMenu(systemMenu);
		return systemMenu;
	}

	protected MenuItem getSwitchUserMenu() {
		return switchUserMenuItem;
	}

	protected MenuItem getCloseMenu() {
		return closeMenuItem;
	}

	protected MenuItem getExitMenu() {
		return exitMenuItem;
	}

	protected Menu createAboutMenu() {
		Menu menuAbout = UiFactory.createMenu("About");
	
		GridPane gridpane = new GridPane();
		gridpane.setHgap(5);
		gridpane.setVgap(5);
		
        CustomMenuItem menuItem = UiFactory.createCustomMenuItem(gridpane);
		
        addAboutRow(gridpane,"SERVER URL",applicationContext.getArguments().getServerURL());
        addAboutRow(gridpane,"SERVER BUILD",applicationContext.getServerBuildLabel());
        addAboutRow(gridpane,"CLIENT BUILD",applicationContext.getClientBuildLabel());
        addAboutRow(gridpane,"HOST NAME",applicationContext.getHostName());
        
		try {
			addAboutRow(gridpane,"ASSET",GalcStation.getHostName());
		} catch (Exception e) {
			getLogger().error(e,"Unable to get Client Hostname. Please Contact IS");
		}
		
		addAboutRow(gridpane,"USE CACHE",applicationContext.getArguments().isUseCache() ? "Yes" : "No");
		addAboutRow(gridpane,"LOCAL IP",applicationContext.getLocalHostIp());
		addAboutRow(gridpane,"LOCAL PORT",Integer.toString(applicationContext.getTerminal().getPort()));
		addAboutRow(gridpane,"ROOT DIR",applicationContext.getArguments().getRootDir());
		addAboutRow(gridpane,"JAVA",String.format("%s | %s | %s",System.getProperty("java.vendor"),System.getProperty("java.version"),System.getProperty("java.home")));
		addAboutRow(gridpane,"LOG LEVEL",LogContext.getContext().getApplicationLogLevel().toString());
		menuAbout.getItems().add(menuItem);
		
		return menuAbout;
	}

	private void addAboutRow(GridPane gp, String caption, String value) {
		int rowIndex = 0; 
		if (gp.getChildren().size() > 0) {
			Node last = gp.getChildren().get(gp.getChildren().size()-1);
			rowIndex = GridPane.getRowIndex(last) + 1;
		}
		gp.addRow(rowIndex,UiFactory.createLabel(caption, caption),UiFactory.createLabel("colon", ":"), UiFactory.createLabel(value, value));
	}
	


	/**
	 * Individual client can add client specific menu from here
	 * 
	 * @param menuBar
	 */
	protected void createCustomizedMenus(MenuBar menuBar) {

	}

	/**
	 * Individual client can add client specific menuItem
	 * 
	 * @param menu
	 */
	protected void createCustomizedMenu(Menu menu) {
		String fontSize = getApplicationPropertyBean().getMenuFontSize();
		if ( null != fontSize && fontSize.length() > 0 ){
			ObservableList<MenuItem> menuItems = menu.getItems();
	        for(MenuItem item : menuItems){
	            item.setStyle("-fx-font-size: "+fontSize);
	        }
		}
	}
	public StatusMessagePane getStatusMessagePane() {
		return this.statusMessagePane;
	}

	public void setStatusMessagePanel(StatusMessagePane statusPane) {
		this.statusMessagePane = statusPane;
	}

	@Override
	public void handle(javafx.event.ActionEvent e) {
		if (e.getSource() == switchUserMenuItem) {
			switchUser();
		} else if (e.getSource() == closeMenuItem) {
			close();
		} else if (e.getSource() == exitMenuItem) {
			exit();
		}else if(e.getSource() == logoutMenuItem) {
			logout();
		}
		
		
		
	}

	public void clearMessage() {
		this.getStatusMessagePane().setErrorMessageArea(null);
	}

	public void clearStatusMessage() {
		this.getStatusMessagePane().setStatusMessage(null);
		this.getStatusMessagePane().clearErrorMessageArea();
	}

	public void clearStatusOnly() {
		this.getStatusMessagePane().clearOnlyIfStatus();
	}

	public void clearById(String newId) {
		this.getStatusMessagePane().clearById(newId);
	}

	public void setErrorMessage(String errorMessage, String id) {
		this.getStatusMessagePane().setErrorMessageArea(errorMessage, id);
	}

	public void setErrorMessage(String errorMessage) {
		this.getStatusMessagePane().setErrorMessageArea(errorMessage);
	}

	@Subscribe
	public void processEvent(StatusMessageEvent event) {
		String applicationId = event.getApplicationId();
		if (applicationId == null || this.application.getApplicationId().equals(applicationId)) {
			switch(event.getEventType()) {
				case INFO:
					setMessage(event.getMessage(),Color.YELLOWGREEN);
					break;
				case WARNING:
				case ERROR:
					setErrorMessage(event.getMessage());
					break;
				case CLEAR:
					clearStatusMessage();
					break;
				default:
			}
		}
	}
	
	public void displayException(Exception e) {
		if (e == null) {
			this.clearMessage();
		} else {
			this.setErrorMessage(e.getMessage() + " " + getStackTrace(e));
		}
	}

	public void setStatusMessage(String message) {
		this.getStatusMessagePane().setStatusMessage(message);
	}

	public void setMessage(String message) {
		this.getStatusMessagePane().setMessage(message);
	}
	
	public void setMessage(String message, Color color) {
		this.getStatusMessagePane().setMessage(message, color);
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


	public String getUserId() {
		return getApplicationContext().getUserId();
	}
	
	public void setUserId(String userId) {
		getApplicationContext().setUserId(userId);
		this.getStatusMessagePane().getStatusPane().setUser(userId);
		getLogger().info("Application " + this.getApplication().getApplicationId() + " switched to user " + userId);
	}

	protected void close() {
		ClientMainFx.getInstance().close(this);
	}

	protected void notifyWindowOpened() {

	}

	protected void exit() {
		if(MessageDialog.confirm(getStage(), "Are you sure that you want to exit?")) {
		  ClientMainFx.getInstance().exitApplication(0);
		}
	}

	public void enableSystemMenu(boolean flag) {
		systemMenu.setDisable(!flag);
	}

	public void handleException(Exception e) {
		this.setErrorMessage(e.getMessage() + " " + getStackTrace(e));
	}

	@Override
	public void execute(String componentId, String level) {

		if (componentId.equals(applicationContext.getHostName())) {
			PropertyService.updateProperty(componentId, LogLevel.LOG_LEVEL,level);
			Logger.updateLogLevel(LogLevel.valueOf(level));
			LogContext.getContext().setApplicationLogLevel(LogLevel.valueOf(level));
			if (logLevelMenuItem != null) {
				logLevelMenuItem.setText("LOG LEVEL   : " + LogContext.getContext().getApplicationLogLevel());
			}
			Logger.getLogger().info("Log Level is updated to " + level);
		} else {
			if (Logger.hasLogger(componentId)) {
				Logger logger = Logger.getLogger(componentId);
				logger.setLogLevel(LogLevel.valueOf(level));
				PropertyService.updateProperty(componentId, LogLevel.LOG_LEVEL,level);
				Logger.getLogger().info("Log Level for category " + componentId + " is updated to " + level);
			}
		}
	}

	public String getApplicationProperty(String propertyName) {
		return PropertyService.getProperty(application.getApplicationId(),propertyName);
	}

	public String getLoggerName() {
		return application.getApplicationId() + getApplicationPropertyBean().getNewLogSuffix();
	}

	public ProductType getProductType() {
		return applicationContext.getProductTypeData().getProductType();
	}

	public ApplicationPropertyBean getApplicationPropertyBean() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class,application.getApplicationId());
	}

	public String getClientHostName() {
		return applicationContext.getHostName();
	}

	public Cursor getWaitCursor() {
		return Cursor.WAIT;
	}

	public Cursor getDefaultCursor() {
		return Cursor.DEFAULT;
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

	public void setDelayedErrorMessage(String message) {
		this.delayedErrorMessage = message;
	}

	public boolean displayDelayedMessage() {
		boolean flag = false;
		if (delayedErrorMessage != null) {
			setErrorMessage(delayedErrorMessage);
			flag = true;
		} else if (delayedMessage != null) {
			setMessage(delayedMessage);
			flag = true;
		}
		delayedErrorMessage = null;
		delayedMessage = null;
		return flag;
	}
	
	public double getScreenWidth() {
	    return Screen.getMainScreen().getVisibleWidth();
	}
	
	public double getScreenHeight() {
		return Screen.getMainScreen().getVisibleHeight();
	}
	
	private void initMassMessagePane(){
		HBox utilityHBox = new HBox();
		utilityHBox.setId("utility-Box");
		VBox dateTimeVBox = new VBox();
		dateTimeVBox.setId("datetime-Box");
		VBox associateVBox = new VBox();
		associateVBox.setId("associate-Box");
		associateVBox.setAlignment(Pos.CENTER);
		associateVBox.setPrefWidth(80);
		
		utilityHBox.setPrefWidth(getScreenWidth());
		dateTimeVBox.setPrefWidth(80);
		dateTimeVBox.setAlignment(Pos.CENTER);
		utilityHBox.setStyle("-fx-border-color: #000000;-fx-border-width: 1px;");
		associateVBox.setStyle("-fx-border-color: #000000;-fx-border-width: 1px;");
		dateTimeVBox.setStyle("-fx-border-color: #000000;-fx-border-width: 1px;");
		StatusPane statusPane = statusMessagePane.getStatusPane();
		// date
		dateTimeVBox.getChildren().add(statusPane.getStatusContentPane().getCenter());
		// time
		dateTimeVBox.getChildren().add(statusPane.getStatusContentPane().getRight());
		//associate id
		Label associateLbl = (Label)statusPane.getLeft();
		associateLbl.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
		associateVBox.getChildren().add(0, associateLbl);
		
		massMessagePane = new MassMessagePane(utilityHBox.getPrefWidth()-160,utilityHBox.getHeight());
		utilityHBox.getChildren().add(associateVBox);
		utilityHBox.getChildren().add(massMessagePane);
		utilityHBox.getChildren().add(dateTimeVBox);
		statusMessagePane.getChildren().remove(statusPane);
		statusMessagePane.setBottom(utilityHBox);
		massMessagePane.setPlantName(applicationContext.getProcessPoint().getPlantName());
		massMessagePane.setDivisionId(applicationContext.getProcessPoint().getDivisionId());
		massMessagePane.setLineId(applicationContext.getProcessPoint().getLineId());
		massMessagePane.intialize();
	}
	
	@Override
	public void execute(String plantName, String divisionId, String lineId) {
		MassMessageFor massMsgFor = new MassMessageFor(plantName, divisionId, lineId);
		massMessagePane.getMessageQueue().offer(massMsgFor);
		massMessagePane.checkForLatestMessage();
	}

	public void logout() {
		if (isRestart()) {
			restart();
		} else {
			switchUser(false, false);	
		}
	}
	
	protected void switchUser() {
		if (isRestart()) {
			restart();
		} else {
			switchUser(true, true);	
		}
	}
	
	private void restart() {
		try {
			RelaunchUtil.restart();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean isRestart() {
		return getApplicationPropertyBean().isRestartOnSwitchUserOrLogout();
	}
	
	public void switchUser(boolean allowExit, boolean cancelOption) {
		
		LoginStatus loginStatus = ClientMainFx.getInstance().switchUser(allowExit, cancelOption);
		if(LoginStatus.OK == loginStatus) {
			String userId = ClientMainFx.getInstance().getAccessControlManager().getUserName();
			this.setUserId(userId);
		}
		
	}
	
	public Stage getStage() {
		return ClientMainFx.getInstance().getStage(getApplicationContext().getApplicationId());
	}
}
