package com.honda.galc.client;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.headless.IHeadlessMain;
import com.honda.galc.client.logging.ClientSideLoggerConfig;
import com.honda.galc.client.simulation.SimulationProcessor;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.LoginDialogWithoutPassword;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.utils.ActivityEvent;
import com.honda.galc.client.utils.ActivityListener;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.ResultsCacheDispatcher;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.LoggerConfigPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>ClientMain</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientMain description </p>
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
 * @author Jeffray Huang
 * Oct 5, 2009
 * 
 * @author  Suriya Sena
 * Jan 17, 2014 - Migration to JavaFx 
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class ClientMainFx extends javafx.application.Application implements EventHandler<WindowEvent> {

	private volatile Arguments arguments = null;

	private static ClientMainFx clientMainFxInstance;

	private ApplicationContext defaultAppContext;

	private Map<String,ApplicationContext> appContexts = new HashMap<String,ApplicationContext>();

	private IAccessControlManager accessControlManager;

	private DeviceDataConverter deviceDataConverter;

	private Stage primaryStage = null;

	private static final String DEFAULT_WINDOW = "com.honda.galc.client.fx.DefaultWindow";

	Map<String,MainWindow> windows = new HashMap<String,MainWindow>();

	Map<String,Stage> stages = new HashMap<String, Stage>();

	private ApplicationPropertyBean appBean;

	int clientInactivityTimeoutMillis;
	private EventHandler<InputEvent>  activityEventHandler;
	private ActivityListener          activityListener;	

	public String currentApplicationId;
	private LoginStatus loginStatus;
	
	public static void main (String args[] ) {
		System.setProperty("prism.lcdtext", "false");
		launch(args);
	}

	public ClientMainFx() {
		clientMainFxInstance = this;
	}

	//set window title on separate windows
	public void setWindowTitle(String appId, String title) {
		if (stages.get(appId) != null) {
			stages.get(appId).setTitle(title);
		}
	}

	//set window title on primary window
	public void setWindowTitle(String title) {
		if (primaryStage != null) {
			primaryStage.setTitle(title);
		}
	}	

	@Override
	public void init() {

		String usageDetail = "\nUsage: ClientMainClass application_root server_url [host_name] [skin]";

		System.out.println("Starting GALC Application...");
		System.out.print("Please wait...\n");
		System.out.println("\n**** Arguments List ****");

		Parameters parameters = getParameters();
		for (String param : parameters.getRaw()) {
			System.out.printf("arg = %s\n",param);
		}

		arguments = Arguments.create(parameters.getRaw());
		if(arguments == null) { 
			System.out.println("Parameters Missing!!" + usageDetail);
			System.exit(0);
		}

		if(arguments.getHostName() == null) {
			System.out.println("Unable to get host name");
		}

		EventBusUtil.register(this);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		this.primaryStage.setOnCloseRequest(this);

		setScreenSize(primaryStage);

		CacheFactory.setPath(arguments.getCachePath());

		HttpServiceProvider.setUrl(arguments.getServerURL());

		loadApplicationConfig();

		initApplicationContext();

		initApplicationLog(arguments);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());

		ServiceMonitor.getInstance().startMonitorHttpService();		//monitor network connection
		initAccessControlManager();

		

		startClient();
	}

	//set screen size for separate windows
	private void setScreenSize(Stage stage) {
		Rectangle2D primarySceneBounds = Screen.getPrimary().getVisualBounds();
		stage.setMaxHeight(primarySceneBounds.getHeight());
		stage.setMaxWidth(primarySceneBounds.getWidth());
		stage.setHeight(primarySceneBounds.getHeight());
		stage.setWidth(primarySceneBounds.getWidth());
		stage.setMaximized(true);
	}


	private void initApplicationLog(Arguments arguments) {

		String logAppid = arguments.getHostName();
		if( isLogNameUsePPID() &&
				null != defaultAppContext.getApplicationId() &&
				defaultAppContext.getApplicationId().length() > 0 ) 
			logAppid = defaultAppContext.getApplicationId();

		LogContext.getContext().setApplicationName(logAppid+getNewLogSuffix(arguments.getHostName()));
		ClientSideLoggerConfig.configLog4j(logAppid);//configure with log4j properties

		LogLevel logLevel = getApplicationLogLevel(arguments.getHostName());//get log level from gal489tbx, overwrite log4j properties
		Logger.updateLogLevel(logLevel);

		LogContext.getContext().setApplicationLogLevel(logLevel);
		Logger.getLogger().setLogContext(LogContext.getContext());
	}

	private boolean isLogNameUsePPID() {
		LoggerConfigPropertyBean loggerConfigProperty = PropertyService.getPropertyBean(LoggerConfigPropertyBean.class,"LOGGER");
		return loggerConfigProperty.isUsePPID();
	}

	private void initApplicationContext() {
		try{
			defaultAppContext = arguments.isUseCache() ? CachedApplicationContext.create(arguments) : NonCachedApplicationContext.create(arguments);
		}catch(Exception e) {
			terminateApplication(e.getMessage());
		}
	}

	private void registerNotificationServices() {
		defaultAppContext.getRequestDispatcher().registerNotifcationService(SimulationProcessor.class.getSimpleName(), SimulationProcessor.getInstance());
		
	}
	
	public void startClient() {
		initApplicationContext();
		defaultAppContext.startRequestDispatcher();
		if(!PropertyService.getPropertyBean(LoggerConfigPropertyBean.class,"LOGGER").getClientPreferredSuffix().toUpperCase().contains("PROD"))
		{
			registerNotificationServices();
		}
		initResultsCacheDispatcher();
		this.appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, getApplicationContext().getApplicationId());

		if(defaultAppContext.getApplicationPropertyBean().isClientMonitorEnabled())
			ClientMonitor.getInstance().start();

		if (arguments.isHeadless()) {
			startHeadlessApplications();
		} else {
			if (!appBean.isAllowWindowResize() && isProcessTerminal()) {
				getLogger().info("Window resize and maximize disabled for process terminal");
				primaryStage.setResizable(false);
				if(PropertyService.getPropertyBean((ApplicationPropertyBean.class), getApplicationContext().getTerminalId()).isStageStyleDecorated())
					primaryStage.initStyle(StageStyle.DECORATED);
				else
					primaryStage.initStyle(StageStyle.UTILITY);
			}
			
			currentApplicationId = defaultAppContext.getDefaultApplication().getApplicationId();
			
			this.performLogin(appBean.isScannerActive()?ClientConstants.DEFAULT_LOGIN_WITH_SCANNER_CAPTION:ClientConstants.DEFAULT_LOGIN_CAPTION);
			getLogger().info("User " + accessControlManager.getUserName() + " has logged in");

			defaultAppContext.setUserId(accessControlManager.getUserName());
			defaultAppContext.setHighestAccessLevel(accessControlManager.getHighestAccessLevel(defaultAppContext.getDefaultApplication().getScreenId()));
			
			startProgressClient("Start " + defaultAppContext.getDefaultApplication().getApplicationDescription());

			EventBusUtil.publish(new ProgressEvent(10, "Start to init device ..."));
			deviceDataConverter = DeviceDataConverter.createInstance(defaultAppContext.getProcessPointId());

			startDeviceManager();
			startActivityListener();
			startDefaultApp();

			EventBusUtil.publish(new ProgressEvent(100,	"Default Application started "));
		}
	}

	private void initResultsCacheDispatcher() {
		Thread worker = new Thread() {
			public void run() {
				ResultsCacheDispatcher.init();	
			}
		};

		worker.setDaemon(false);
		worker.start();
	}

	public void performLogin(String header) {
		if (appBean.isManualLogin()) {
			LoginStatus loginStatus = login();
			if (loginStatus == null || loginStatus != LoginStatus.OK)
				exitApplication(0);

		} else {
			LoginStatus loginStatus = loginWithoutPassword(header);
			if (loginStatus == null || loginStatus != LoginStatus.OK)
				exitApplication(0);
		}
	}

	/**
	 * starts all headless applications
	 */
	private void startHeadlessApplications() {
		accessControlManager.login(arguments.getHostName(),arguments.getUserName(), arguments.getPassword());
		startDeviceManager();

		SortedArrayList<Application> applications = new SortedArrayList<Application>("getPreload");
		for (ApplicationByTerminal appByTerminal: defaultAppContext.fetchApplicationByTerminals()) {
			applications.add(appByTerminal.getApplication());
		}

		for (Application app: applications) {
			launchHeadlessApplication(app);
		}
	}

	public LoginStatus login() {
		if(arguments.getUserName() != null && arguments.getPassword() != null){
			loginStatus = accessControlManager.login(arguments.getHostName(),arguments.getUserName(), arguments.getPassword());

			if(loginStatus == LoginStatus.OK || loginStatus == LoginStatus.LIGHT_SECURITY_OK) {
				return LoginStatus.OK;
			}

			if (!MessageDialog.confirm(null,String.format("Username [%s] %s\nDo you want to login as a different user?", arguments.getUserName(),loginStatus.getMessage()))) {
				return null;
			}
		}
		return LoginDialog.login(getStage(defaultAppContext.getApplicationId()));
	}

	public LoginStatus loginWithoutPassword(String header) {
		loginStatus = null;
		if(appBean.isLoginWithoutPassword()) {
			if(arguments.getUserName() != null){
				loginStatus = accessControlManager.login_without_password(arguments.getUserName());
				if(loginStatus == LoginStatus.OK || loginStatus == LoginStatus.LIGHT_SECURITY_OK) {
					return LoginStatus.OK;
				}
				if (!MessageDialog.confirm(null,String.format("Username [%s] %s\nDo you want to login as a different user?", arguments.getUserName(),loginStatus.getMessage()))) {
					return null;
				}
			}
		} else {
			if(arguments.getUserName() != null && arguments.getPassword() != null){
				loginStatus = accessControlManager.login(arguments.getHostName(),arguments.getUserName(), arguments.getPassword());

				if(loginStatus == LoginStatus.OK || loginStatus == LoginStatus.LIGHT_SECURITY_OK) {
					return LoginStatus.OK;
				}

				if (!MessageDialog.confirm(null,String.format("Username [%s] %s\nDo you want to login as a different user?", arguments.getUserName(),loginStatus.getMessage()))) {
					return null;
				}
			}
		}
		return LoginDialogWithoutPassword.login(header);
	}

	public void startDefaultApp() {
		Application application = defaultAppContext.getDefaultApplication();
		getLogger().info("launching GALC client application...");
		defaultAppContext.setApplicationId(application.getApplicationId()); 
		appContexts.put(application.getApplicationId(), defaultAppContext);
		try {
			launchApplication(application);
		} catch (SystemException e) {
			terminateApplication(e.getMessage());
		}
	}

	/**
	 * launches headless application
	 * 
	 * @param headlessApplication
	 */
	public void launchHeadlessApplication(Application headlessApplication) {
		String headlessApplicationClass = (headlessApplication != null)? headlessApplication.getScreenClass() : "";
		try {
			Class<?> headlessMainClass = null;
			try {
				headlessMainClass = Thread.currentThread().getContextClassLoader().loadClass(headlessApplicationClass.trim());
			} catch (Exception ex){
				return;
			}

			IHeadlessMain headlessMain = (IHeadlessMain) headlessMainClass.newInstance();
			headlessMain.initialize(getApplicationContext(headlessApplication), headlessApplication);
			getLogger().info("Started headless application " , headlessApplication.getApplicationId() + " with: " + headlessMainClass.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
			Throwable throwable = e;
			if(e instanceof InvocationTargetException) throwable = e.getCause();
			String message = "Unable to start headless client "+ headlessApplicationClass + " due to " + throwable.toString();
			throw new SystemException(message,throwable);
		} 
	}

	/**
	 * launches headed application
	 * 
	 * @param application
	 */
	public void launchApplication(Application application) {
		launchApplication(application, false);
	}


	//allow application to be launched in new window
	public void launchApplication(Application application, boolean inNewWindow) {
		currentApplicationId = application.getApplicationId();
		String applicationClass = (application != null)? application.getScreenClass() : DEFAULT_WINDOW;
		try {
			Class<?>[] constructParamCls = new Class[2];
			constructParamCls[0] = ApplicationContext.class;
			constructParamCls[1] = Application.class;
			Object[] constructParamObj = new Object[2];
			constructParamObj[0] = getApplicationContext(application);
			constructParamObj[1] = application;

			Class<?> clientScreenClass = Class.forName(applicationClass);
			Constructor<?> clientScreenConstructor = clientScreenClass.getConstructor(constructParamCls);

			//add windows to stages map to set window title during MainWindow's construction
			Stage newStage = null;
			if (!inNewWindow) {
				stages.put(application.getApplicationId(), primaryStage);
			} else {
				newStage = new Stage();
				setScreenSize(newStage);	
				stages.put(application.getApplicationId(), newStage);
			}
			final MainWindow clientScreen = (MainWindow)clientScreenConstructor.newInstance(constructParamObj);
			Scene scene = new Scene(clientScreen);
			scene.getStylesheets().add(getStylesheetPath());

			if (!inNewWindow) {
				setScreenSize(primaryStage);
				primaryStage.setScene(scene);
				primaryStage.setAlwaysOnTop(true);
				primaryStage.show();
				primaryStage.setAlwaysOnTop(false);
				primaryStage.focusedProperty().addListener(createStageFocusChangeListener());
				addClickerSupport(primaryStage);
			} else {
				newStage.setScene(scene);
				newStage.show();
				// Fix : RGALCDEV-4549- calling close method on 'X' button click of window 
				newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent arg0) {
						close(clientScreen);
						
					}
				});
				
				newStage.focusedProperty().addListener(createStageFocusChangeListener());
				addClickerSupport(newStage);
			}

			getLogger().info("Opened window " ,application.getApplicationId() + ":" + clientScreenClass.getSimpleName());
			windows.put(application.getApplicationId(), clientScreen);

		} catch (Exception e) {
			e.printStackTrace();
			Throwable throwable = e;
			if(e instanceof InvocationTargetException) throwable = e.getCause();
			String message = "Unable to open window "+applicationClass+" due to " + throwable.toString();
			throw new SystemException(message,throwable);
		} 
	}
	
	private ChangeListener<Boolean> createStageFocusChangeListener() {
		return new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			{
				if (newPropertyValue) updateCurrentApplicationId(arg0);
			}
		};
	}
	
	private void updateCurrentApplicationId(ObservableValue<? extends Boolean> arg0) {
		ReadOnlyBooleanProperty readOnlyBooleanProperty = (ReadOnlyBooleanProperty) arg0;
    	Stage stage = (Stage)readOnlyBooleanProperty.getBean();
    	MainWindow window = (MainWindow) stage.getScene().getRoot();
    	
    	currentApplicationId = window.getApplicationContext().getApplicationId();
    	
	}


	private void addClickerSupport(Stage stage) {
		ClickerControl.getInstance().addKeyHandler(stage);
	}

	private ApplicationContext getApplicationContext(Application application) {
		if(appContexts.containsKey(application.getApplicationId()))
			return appContexts.get(application.getApplicationId());
		else{
			ApplicationContext newAppContext = NonCachedApplicationContext.create(arguments, application.getApplicationId());
			newAppContext.setHighestAccessLevel(accessControlManager.getHighestAccessLevel(application.getScreenId()));
			appContexts.put(application.getApplicationId(), newAppContext);
			newAppContext.setUserId(defaultAppContext.getUserId()==null?"":defaultAppContext.getUserId());
			return newAppContext;
		}
	}

	public void startApplication(String appId) {
		startApplication(appId, false);
	}

	//allow new application in new window
	public void startApplication(String appId, boolean inNewWindow) {
		if(isApplicationLaunched(appId))
			return;

		Application application = defaultAppContext.getApplication(appId);

		if(application == null) {
			MessageDialog.showError(null, String.format("Application '%s' does not exist!", appId));
			return;
		}

		if(!accessControlManager.isAccessPermitted(application.getScreenId())) {
			MessageDialog.showError(null, "You have no access permission of application \"" + application.getApplicationName() + "\"");
		}else {
			try{
				launchApplication(application, inNewWindow);
			}catch(SystemException e) {
				MessageDialog.showError(null,e.getMessage(),"Error opening application");
			}
		}
	}

	private String getNewLogSuffix(String applicationName) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class,applicationName).getNewLogSuffix();
	}

	private boolean isApplicationLaunched(String appId) {
		//bring the existing window to the front
		Stage stage = stages.get(appId);
		if(stage != null){
			stage.toFront();
		}
		return stage != null;
	}

	private LogLevel getApplicationLogLevel(String applicationName) {
		String property = PropertyService.getProperty(applicationName, LogLevel.LOG_LEVEL, LogRecord.defaultLevel.toString());
		return LogLevel.valueOf(property);
	}

	private void terminateApplication(String message){
		getLogger().error(message);
		String msg = "Terminating application! \n" + message;
		MessageDialog.showError( msg);
		exitApplication(1);
	}

	public void close(MainWindow window) {
		if(window.getApplication() == null) return;
		String appId = window.getApplication().getApplicationId();

		if(windows.size() == 1) {
			if(isExitConfirmed()){
				exitApplication(0);
			}
		} else {
			if(windows.containsKey(appId)) {
				windows.remove(appId);
			}
			if(stages.containsKey(appId)) {
				stages.get(appId).close();
				stages.remove(appId);
			}
		}
	}

	public void exitApplication(int code) {
		DeviceManager.getInstance().closeDevices();
		ClientMonitor.getInstance().deActivate();

		if(defaultAppContext != null) {
			defaultAppContext.close();
		}

		getLogger().check("Application is terminated");
		
		Platform.exit();
		System.exit(code);
	}

	private void closeWindows() {
		for(Stage stage : stages.values()) 
			stage.close();
		stages.clear();
		windows.clear();
	}

	public void startDeviceManager() {
		DeviceManager.getInstance().loadDevices(defaultAppContext.getHostName());
	}

	private void startProgressClient(String description) {
		ClientStartUpProgress progressClient = new ClientStartUpProgress(description);
		progressClient.start();
	}

	private void loadApplicationConfig() {
		ApplicationContextProvider.loadFromClassPathXml("applicationFx.xml");
	}

	public static ClientMainFx getInstance() {
		return clientMainFxInstance;
	}

	public DeviceDataConverter getDeviceDataConverter() {
		return deviceDataConverter;
	}

	public void setDeviceDataConverter(DeviceDataConverter deviceDataConverter) {
		this.deviceDataConverter = deviceDataConverter;
	}

	public IAccessControlManager getAccessControlManager() {
		return accessControlManager;
	}

	public void setAccessControlManager(IAccessControlManager accessControlManager) {
		this.accessControlManager = accessControlManager;
	}

	private void initAccessControlManager() {
		accessControlManager = arguments.isUseCache() ? new CachedAccessControlManager() : new AccessControlManager();
	}

	public ApplicationContext getApplicationContext() {
		return defaultAppContext;
	}

	public String getLocatedProcessPoint() {
		return defaultAppContext.getTerminal().getLocatedProcessPointId();
	}

	public boolean isProcessTerminal() {
		return !StringUtils.isEmpty(getLocatedProcessPoint());		
	}

	public boolean isExitConfirmed() {
		return MessageDialog.confirm(primaryStage, "Are you sure that you want to exit?");
	}

	public Map<String,MainWindow> getLaunchedWindows() {
		return windows;
	}

	@Override
	public void handle(WindowEvent windowEvent) {
		if (windowEvent.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
			if (isExitConfirmed()) {
				exitApplication(0);
			} else {
			  windowEvent.consume();
			}
		}
	}

	public String getStylesheetPath() { 
		String fmt="/resource/css/%s.css";
		String cssPath=String.format(fmt,"default");

		if (appBean != null) {
			cssPath = String.format(fmt, appBean.getClientCssFile());
		}

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(String.format("Unable to load stylesheet [%s]. Using default",cssPath));
			cssPath= String.format(fmt,"default");
		}
		return cssPath;
	}

	public Stage getStage() {
		return primaryStage;
	}

	//get stage by application ID
	public Stage getStage(String applicationID) {
		return stages.get(applicationID);
	}

	public void startActivityListener() {
		clientInactivityTimeoutMillis = Integer.parseInt(appBean.getGalcClientTimeout()) * 60 * 1000;
		if (clientInactivityTimeoutMillis > 0) {
			getActivityListener().start();
			primaryStage.addEventFilter(InputEvent.ANY, getActivityEventHandler());
		}
	}

	public void stopActivityListener() {
		getActivityListener().stop();
		primaryStage.removeEventFilter(InputEvent.ANY, getActivityEventHandler());
	}

	private ActivityListener getActivityListener() {
		if (activityListener == null) {
			activityListener = new ActivityListener(clientInactivityTimeoutMillis);
		}
		return activityListener;
	}

	private EventHandler<InputEvent> getActivityEventHandler() {
		if (activityEventHandler == null) {
			activityEventHandler = new EventHandler<InputEvent>() {
				@Override
				public void handle(InputEvent arg0) {
					EventBusUtil.publish(new ActivityEvent());
				}
			};
		}
		return activityEventHandler;
	}

	@Subscribe
	public void deadEventHandler(DeadEvent event) {
		getLogger().warn(String.format("Recieved an unhandled event. this is indication that there is a bug in the code. %s  from %s",
				event.getEvent().toString(), event.getSource().toString()));
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public LoginStatus switchUser(boolean allowExit, boolean allowCancel) {

		return LoginDialog.login(getPrimaryStage(), allowExit, allowCancel);
		
	}
	
	public void resetContext() {
		defaultAppContext.setUserId(accessControlManager.getUserName());
		getLogger().info("Switched to user " + accessControlManager.getUserName());
		
	}

	public String getCurrentApplicationId() {
		return currentApplicationId;
	}

	public boolean isLightSecurityLogin() {
		
		return loginStatus != null && loginStatus == LoginStatus.LIGHT_SECURITY_OK;
	}

	private class ExceptionHandler implements Thread.UncaughtExceptionHandler {
		public void uncaughtException(Thread thread, Throwable throwable) {
			try {
				getLogger().error(throwable, "Uncaught exception in thread \"" + thread.getName() + "\" " + throwable.toString());
				if (throwable instanceof Error) {
					terminateApplication(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				}
			} catch (Throwable t) {
				System.exit(1);
			}
		}
	}

}
