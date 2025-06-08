package com.honda.galc.client;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.headless.IHeadlessMain;
import com.honda.galc.client.logging.ClientSideLoggerConfig;
import com.honda.galc.client.simulation.SimulationProcessor;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.LogServerNotification;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.net.HttpRequestInvoker;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.LoggerConfigPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
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
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ClientMain {

	private volatile Arguments arguments = null;

	private static ClientMain clientMain;

	private ApplicationContext defaultAppContext;

	private Map<String,ApplicationContext> appContexts = new HashMap<String,ApplicationContext>();

	private IAccessControlManager accessControlManager;

	private DeviceDataConverter deviceDataConverter;
	
	private InactivityListener inactivityListener;

	Map<String,MainWindow> windows = new HashMap<String,MainWindow>();

	private int clientInactivityTimeoutMillis;

	public static void main(String[] params) {
		String usageDetail = new String("\nUsage: ClientMainClass application_root server_url [host_name] [look&feel]"); 
		System.out.println("Starting GALC Application...");
		System.out.print("Please wait...\n");
		System.out.println("\n**** Arguments List ****");

		for(int i=0;i<params.length;i++)
			System.out.println("arg["+i+"] = "+params[i]);

		Arguments arguments = Arguments.create(Arrays.asList(params));
		if(arguments == null) { 
			System.out.println("Parameters Missing!!"+usageDetail);
			System.exit(0);
		}
		if(arguments.getHostName() == null) System.out.println(" Unable to get host name");
		clientMain = new ClientMain(arguments);
		clientMain.startClient();
	}
	
	public ClientMain(Arguments arguments) {
		this.arguments = arguments;
		CacheFactory.setPath(arguments.getCachePath());
		
		HttpServiceProvider.setUrl(arguments.getServerURL());
		
		loadApplicationConfig();
		
		initApplicationContext();
		
		initApplicationLog(arguments);
		
		int connectionTimeout = defaultAppContext.getApplicationPropertyBean().getHttpConnectionTimeout();
		HttpRequestInvoker.setConnectionTimeout(connectionTimeout);
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
		
		ServiceMonitor.getInstance().startMonitorHttpService();//monitor network connection
		initAccessControlManager();
	}
	private void initApplicationLog(Arguments arguments) {
		
		String logAppid = arguments.getHostName();
		if( isLogNameUsePPID() &&
				null != defaultAppContext.getApplicationId() &&
				defaultAppContext.getApplicationId().length() > 0 ) 
			logAppid = defaultAppContext.getApplicationId();

		LogContext.getContext().setApplicationName(logAppid+getNewLogSuffix(arguments.getHostName()));
		LogContext.getContext().setClientName(ApplicationContext.getInstance().getTerminalId());
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

	public void startClient() {
		initApplicationContext();
		defaultAppContext.startRequestDispatcher();
		registerNotificationServices();

		if(defaultAppContext.getApplicationPropertyBean().isClientMonitorEnabled())
			ClientMonitor.getInstance().start();

		if (arguments.isHeadless()) {
			startHeadlessApplications();
		} else {
			if (defaultAppContext.getDefaultApplication()  == null) {
				MessageDialog.showError("Terminating application! \n No default application configured for this terminal");
				exitApplication(1);
			}
			LoginStatus loginStatus = login();
			if(loginStatus == null || loginStatus != LoginStatus.OK)  exitApplication(0);

			getLogger().info("User " + accessControlManager.getUserName() + " has logged in" );
			defaultAppContext.setUserId(accessControlManager.getUserName());
			// Replace "Start null" title with "Start : Initializing Application" title when Application Description is empty
			if (defaultAppContext.getDefaultApplication() == null || defaultAppContext.getDefaultApplication()
					.getApplicationDescription() == null
					|| defaultAppContext.getDefaultApplication()
							.getApplicationDescription().trim().length() == 0) {
				startProgressClient("Start : Initializing Application");
			} else {
				startProgressClient("Start "
						+ defaultAppContext.getDefaultApplication()
								.getApplicationDescription());
			}
			EventBus.publish(new ProgressEvent(10, "Start to init device ..."));
			deviceDataConverter = DeviceDataConverter.createInstance(defaultAppContext.getProcessPointId());

			registerClientIpaddress();
			
			startDeviceManager();
			
			startDefaultApp();	
			
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						startInactivityListener();
					}
				});
			}catch(Exception e){
				getLogger().emergency(e, "Failed to register the InactivityListener.");
			}		
				
			EventBus.publish(new ProgressEvent(100, "Default Application started "));
		}
	}

	/**
	 * Start the inactivity listener.
	 */
	private void startInactivityListener() {
		ApplicationPropertyBean appBean = getApplicationContext()
				.getApplicationPropertyBean();
		clientInactivityTimeoutMillis = Integer.parseInt(appBean
				.getGalcClientTimeout()) * 60 * 1000;
		if (clientInactivityTimeoutMillis > 0) {
			if (inactivityListener == null) {
				inactivityListener = new InactivityListener(
						clientInactivityTimeoutMillis);
			}
			inactivityListener.start();
		}
	}

	/**
	 * Stop inactivity listener.
	 */
	private void stopInactivityListener() {
		if (inactivityListener != null) {
			inactivityListener.stop();
		}
	}

	private void registerNotificationServices() {
		defaultAppContext.getRequestDispatcher().registerNotifcationService("SimulationProcessor", new SimulationProcessor());
		defaultAppContext.getRequestDispatcher().registerNotifcationService("LogServerNotification",new LogServerNotification());
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
			LoginStatus loginStatus = accessControlManager.login(arguments.getHostName(),arguments.getUserName(), arguments.getPassword());
			if(loginStatus == LoginStatus.OK || loginStatus == LoginStatus.LIGHT_SECURITY_OK) return LoginStatus.OK;
			if(JOptionPane.showConfirmDialog(null, loginStatus.getMessage() + "\nDo you want to login as a different user?" ,
					"login as user : " + arguments.getUserName() + "\n", JOptionPane.YES_NO_OPTION) 
					!= JOptionPane.YES_OPTION) return null;

		}
		return LoginDialog.login(null);
	}

	public void switchUser(MainWindow window) {
		switchUser(window,true, true, true);
	}
	
	public void switchUser(MainWindow window, boolean allowExit, boolean allowCancel) {
		switchUser(window, allowExit, allowCancel, true);
	}
	
	public void switchUser(MainWindow window, boolean allowExit, boolean allowCancel, boolean enableChangePsswrd) {
		LoginStatus loginStatus = LoginDialog.login(window,allowExit, allowCancel, enableChangePsswrd);
		if(loginStatus != LoginStatus.OK) return;
		defaultAppContext.setUserId(accessControlManager.getUserName());
		getLogger().info("Switched to user " + accessControlManager.getUserName());
	}

	public void startDefaultApp() {
		if (!accessControlManager.isAccessPermitted()) {
			MessageDialog.showError("Terminating application! \nYou have no access permission of default application of this terminal");
			exitApplication(1);
		}

		Application application = defaultAppContext.getDefaultApplication();
		getLogger().info("launching GALC client application...");
		if (application.getApplicationId() != null)
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
	@SuppressWarnings("unchecked")
	public void launchApplication(Application application) {

		String applicationClass = (application != null)? application.getScreenClass() : "com.honda.galc.client.ui.DefaultWindow";
		try {
			Class[] constructParamCls = new Class[2];
			constructParamCls[0] = ApplicationContext.class;
			constructParamCls[1] = Application.class;
			Object[] constructParamObj = new Object[2];
			constructParamObj[0] = getApplicationContext(application);
			constructParamObj[1] = application;

			Class<?>	clientScreenClass = Class.forName(applicationClass);
			Constructor clientScreenConstructor = clientScreenClass.getConstructor(constructParamCls);

			MainWindow clientScreen = (MainWindow)clientScreenConstructor.newInstance(constructParamObj);
			
			//if client that is being launched is a modal, disable all other windows (replicate modal functionality)
			if (clientScreen.getApplicationPropertyBean().isModal()) {
				for (MainWindow window : windows.values()) {
					window.setEnabled(false);
				}
			}
			
			clientScreen.setVisible(true);
			getLogger().info("Opened window " ,application.getApplicationId() + ":" + clientScreenClass.getSimpleName());
			clientScreen.repaint();

			windows.put(application.getApplicationId(), clientScreen);

		} catch (Exception e) {
			e.printStackTrace();
			Throwable throwable = e;
			if(e instanceof InvocationTargetException) throwable = e.getCause();
			String message = "Unable to open window "+applicationClass+" due to " + throwable.toString();
			throw new SystemException(message,throwable);
		} 
	}

	private ApplicationContext getApplicationContext(Application application) {
		if(appContexts.containsKey(application.getApplicationId())) {
			ApplicationContext.setInstance(appContexts.get(application.getApplicationId()));
			return appContexts.get(application.getApplicationId());
		} else {
			ApplicationContext newAppContext = NonCachedApplicationContext.create(arguments, application.getApplicationId());
			appContexts.put(application.getApplicationId(), newAppContext);
			newAppContext.setUserId(defaultAppContext.getUserId()==null?"":defaultAppContext.getUserId());
			return newAppContext;
		}
	}

	public void startApplication(String appId) {
		if(isApplicationLaunched(appId))
			return;

		Application application = defaultAppContext.getApplication(appId);

		if(application == null) {
			JOptionPane.showMessageDialog(null, "Application \"" + appId + "\" does not exist",	"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(!accessControlManager.isAccessPermitted(application.getScreenId())) {
			MessageDialog.showError(null, "You have no access permission of application \"" + application.getApplicationName() + "\"");
		}else {
			try{
				launchApplication(application);
			}catch(SystemException e) {
				MessageDialog.showError(null,e.getMessage(),"Error opening application");
			}
		}
	}

	private String getNewLogSuffix(String applicationName) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class,applicationName).getNewLogSuffix();
	}

	private boolean isApplicationLaunched(String appId) {
		MainWindow window = windows.get(appId);
		if(window != null){
			window.setVisible(true);
			window.toFront();
		}
		return window != null;
	}

	private LogLevel getApplicationLogLevel(String applicationName) {
		String property = PropertyService.getProperty(applicationName, LogLevel.LOG_LEVEL, LogRecord.defaultLevel.toString());
		return LogLevel.valueOf(property);
	}

	private void terminateApplication(String message){
		getLogger().error(message);
		String msg = "Terminating application! \n" + message;
		if(!arguments.isHeadless())
		{
			MessageDialog.showError( msg);
		}
		else
		{
			System.out.println(msg);
		}
		exitApplication(1);
	}

	public void close(MainWindow window) {
		if(window.getApplication() == null) return;
		String appId = window.getApplication().getApplicationId();

		if(windows.size() == 1) {
			if(MessageDialog.confirm(window, "Are you sure that you want to exit?")){
				window.cleanUp();
				exitApplication(0);
			}
		} else {
			if(windows.containsKey(appId)) windows.remove(appId);
			
			//if application that is being closed is a modal, enable all other windows (replicate closing a modal)
			if (window.getApplicationPropertyBean().isModal()) {
				for (MainWindow windowItem : windows.values()) {
					windowItem.setEnabled(true);
				}
			}
			
			window.cleanUp();
			window.dispose();
		}
	}

	private void basicCloseWindow(MainWindow window) {
		window.cleanUp();
		window.dispose();
	}

	public void exitApplication(int code) {
		stopInactivityListener();
		DeviceManager.getInstance().closeDevices();
		ClientMonitor.getInstance().deActivate();
		
		if(defaultAppContext != null) {
			defaultAppContext.close();
		}

		if (!arguments.isHeadless()) {
			closeWindows();
		}
		getLogger().info("application is terminated");
		System.exit(code);
	}

	private void closeWindows() {
		for(MainWindow window : windows.values()) 
			basicCloseWindow(window);

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
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
	}

	public static ClientMain getInstance() {
		return clientMain;
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

	public Map<String,MainWindow> getLaunchedWindows() {
		return windows;
	}
	
	public void registerClientIpaddress() {
		SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
		
		if (systemPropertyBean.isAutoRegisterIpaddr()) {
		   String ipAddress = defaultAppContext.getLocalHostIp();
		   String hostname = defaultAppContext.getHostName();
		   String clientname="";
		   try
		   {		      
		       clientname = InetAddress.getLocalHost().getHostName();
		      
				   TerminalDao terminalDao = ServiceFactory.getDao(TerminalDao.class);
				   Terminal terminal  = terminalDao.findByKey(hostname);
				   if(clientname.equalsIgnoreCase(hostname)|| (terminal != null && terminal.isAutoUpdateIpAddress())){
					   terminal.setIpAddress(ipAddress);
			           terminalDao.save(terminal);
					
					   getLogger().info(String.format("Terminal %s ipaddress updated to %s." ,hostname,ipAddress));
				   }
		   }
		   catch (UnknownHostException ex){
			   getLogger().info("Hostname can not be resolved");
		   }
		}
	}
	
	private WebStartClientDao getWebStartClientDao() {
		return ServiceFactory.getDao(WebStartClientDao.class);
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
