package com.honda.galc.client;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.buildlevel.BuildLevelUtility;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.dao.conf.NotificationDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.net.SocketRequestDispatcher;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AbstractPropertyConfig;
import com.honda.galc.util.GalcStation;

/**
 * 
 * <h3>ApplicationContext Class description</h3>
 * <p> ApplicationContext description </p>
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
 * Aug 2, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public abstract class ApplicationContext {
	
	private Arguments arguments;
	
	private InetAddress localAddress;
	
	private String applicationId;
	
	private String userId;
	
	protected static ApplicationContext applicationContext;
	
	private SocketRequestDispatcher requestDispatcher = null;
	

	/**
	 * this map is used to redirect the original application to new applications
	 * this is useful when we rewrite the team leader app with a different class to replace the 
	 * old app. In this case, you don't have to change original configurations of the original app
	 */
	protected static Map<String,String> mappedScreenClasses = new HashMap<String, String>(){

		private static final long serialVersionUID = 1L;

		{
			put("EngineFiringMaintenanceFrame","com.honda.galc.client.teamleader.EngineFiringMaintenanceView");
			put("ManualDcMcOnOffView","com.honda.galc.client.teamleader.ManualDcMcOnOffView");
			put("DataRecoveryFrame","com.honda.galc.client.teamleader.recovery.frame.DataRecoveryFrame");
			put("QicsFrame","com.honda.galc.client.qics.view.frame.QicsFrame");
			put("QicsLotControlFrame","com.honda.galc.client.qics.view.frame.QicsLotControlFrame");
			put("QicsLpdcFrame","com.honda.galc.client.qics.view.frame.QicsLpdcFrame");
			put("QicsMaintenanceFrame","com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame");
		}
	};
	
	/**
	 * returns a valid WebStartClient object representing
	 * current headless/headed terminal 
	 * 
	 * @return
	 */
	public WebStartClient getWebStartClient() {
		WebStartClient client = null;
		String hostName = GalcStation.getHostName(this.getTerminalId(), getLoggerName());
		try {
			if (isHeadless()) {
				// Headless terminals will need to be looked up by terminal ID (instead of asset name)
				// and assetName should be used as column location
				client = getWebStartClientDao().findByKey(this.getTerminalId());

				// use the first 20 characters of the asset name if its longer than 20 chars - column max length is 20
				client.setColumnLocation(hostName.substring(0, Math.min(hostName.length(), 20)));	
			} else {
				client = getWebStartClientDao().findByKey(hostName);
				if(client == null) {
					String localIp = GalcStation.getLocalIpAddress(this.getTerminalId(), getLoggerName());
					client =  getWebStartClientDao().findByKey(localIp);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, String.format("Unable to retrieve Webstart Client information for %s/%s ", 
					(this != null ? StringUtils.trimToEmpty(this.getTerminalId()) : ""), hostName));
		}
		
		return client;
	}
	
	/**
	 * this map is used to redirect the original application to new applications
	 * this is useful when we rewrite the team leader app with a different class to replace the 
	 * old app. In this case, you don't have to change original configurations of the original app
	 */
	protected static Map<String,String> mappedApps = new HashMap<String, String>(){
		
		private static final long serialVersionUID = 1L;

		{
			put("AEManuLotCtrRpr","ManLotCtrRpr");
			
		}
	};
	
	public ApplicationContext(Arguments args) {
		this.arguments = args;
	    this.localAddress = fetchLocalHostAddress();
	}
	
	public ApplicationContext(Arguments args, String applicationId) {
		this(args);
		this.applicationId = applicationId;
	}
	
	public String getHostName(){
		return arguments.getHostName();
	}
	
	public Arguments getArguments() {
		return this.arguments;
	}
	
	public void setArguments(Arguments args) {
		this.arguments = args;
	}
	
	public String getLocalHostIp() {
	    return localAddress == null ? "" : localAddress.getHostAddress();
	}
	
	public String getLocalHostName() {
	    return localAddress == null ? "" : localAddress.getHostName();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
 	public abstract Terminal getTerminal();
 	
	public String getTerminalDescription() {
		return getTerminal().getTerminalDescription();
	}
 	
 	public abstract String getServerBuildLabel();
 	
	public String getClientBuildLabel() {
		return BuildLevelUtility.getBuildComment();
	}
	
	/**
	 * This function uses the Network Interface API to get an address in
	 * both Windows and Linux. InetAddress looks at the /etc/hosts file
	 * in linux, which only contains 127.0.0.1.
	 * @return Returns the IPv4 address of the Client Host
	 */
	private InetAddress fetchLocalHostAddress(){
		NetworkInterface iface = null;
		try{
		for(Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();){
    		iface = (NetworkInterface)ifaces.nextElement();
    		InetAddress ia = null;
    		for(Enumeration<InetAddress> ips = iface.getInetAddresses();ips.hasMoreElements();){
    			ia = (InetAddress)ips.nextElement();
    			if(!ia.isLoopbackAddress() && (!ia.isLinkLocalAddress()) && (ia instanceof Inet4Address)){
    				return ia;
    			}
    		}
    	}
		}catch(Exception e){
			getLogger().error(e, "Unable to get Host Address. Please Contact IS");
		}
        return null;
	}
	
	public static ApplicationContext getInstance() {
	    return applicationContext;
	}
	
	public static void setInstance(ApplicationContext appContext) {
	    applicationContext = appContext;
	}
	
 	protected void startRequestDispatcher() {
		
		if(getTerminal().getPort() <= 0) {
			String message = "Incorrect terminal receiving socket port number \"" +  getTerminal().getPort() + "\"";
			throw new SystemException(message);
		}
		this.requestDispatcher = new SocketRequestDispatcher(getTerminal().getPort(),this);
		try {
			requestDispatcher.accept();
		} catch (Exception e) {
			String message = "Failed to start receiving socket at \"" + getTerminal().getPort() + "\" due to " + e.getMessage();
			throw new SystemException(message);
		}
		
		getLogger().info("terminal receiving socket started at port number: " + getTerminal().getPort());
			
	}
 	
 	
 	public abstract Application getDefaultApplication();
 	
 	
	public abstract Application getApplication(String appId);
	
	public abstract List<ApplicationMenuEntry> getApplicationMenus();
	
	public abstract Notification getNotification(String name);
	
	public abstract List<ProductTypeData> getProductTypeDataList();
	
	public boolean isDefaultApplication(String appId) {
		Application app = getDefaultApplication();
		return app != null && app.getApplicationId().equals(appId);
		
	}
	
	
	public SocketRequestDispatcher getRequestDispatcher() {
		return requestDispatcher;
	}
	
	public void setRequestDispatcher(SocketRequestDispatcher requestDispatcher) {
		this.requestDispatcher = requestDispatcher;
	}
	
	public String getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = StringUtils.trimToEmpty(applicationId);
	}
	
	public String getProcessPointId() {
		if (applicationId == null || applicationId.trim().equals(""))
			return StringUtils.trimToEmpty(getTerminal().getLocatedProcessPointId());
		
		return applicationId;
	}
	
	public ProcessPoint getProcessPoint() {
		return getTerminal().getProcessPoint();
	}
	
	public String getTerminalId() {
		return getTerminal().getHostName();
	}
	
	public String getLoggerName() {
		return getApplicationId() == null ? "default" :getApplicationId() + getApplicationPropertyBean().getNewLogSuffix();
	}
	
	public Logger getLogger() {
		return Logger.getLogger(getLoggerName());
	}
	
	public ApplicationPropertyBean getApplicationPropertyBean() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, getProcessPointId());
	}
	
	public ProductTypeData getProductTypeData(){
		String productType = getApplicationPropertyBean().getProductType();
		return getProductTypeData(productType);
	}
	
	public ProductTypeData getProductTypeData(String productType){
		for(ProductTypeData productTypeData : getProductTypeDataList()) {
			if(productType.equals(productTypeData.getProductTypeName())) return productTypeData;
		}
		throw new SystemException("Product Type " + productType + " is not defined");
	}
	
	protected List<ApplicationMenuEntry> fetchApplicationMenus() {
		
		List<ApplicationMenuEntry> menus = new ArrayList<ApplicationMenuEntry>(); 
        menus = getDao(ApplicationMenuDao.class).findAllByClientId(getHostName());
		for(ApplicationMenuEntry menuEntry : menus) {
			menuEntry.setLeafNode(isLeafNode(menuEntry, menus));
			if(mappedApps.containsKey(menuEntry.getNodeName()))
				menuEntry.setNodeName(mappedApps.get(menuEntry.getNodeName()));
		}

		return menus;
		
	}
	
	private boolean isLeafNode(ApplicationMenuEntry menuEntry, List<ApplicationMenuEntry> menus) {
		
		for(ApplicationMenuEntry menu : menus) {
			if (menuEntry.getId().getNodeNumber() == menu.getParentNodeNumber()) return false;
		}
		return true;
	}
	
	public List<ApplicationByTerminal> fetchApplicationByTerminals() {
		
		List<ApplicationByTerminal> appTerminals =  getDao(ApplicationByTerminalDao.class).findAllByTerminal(getHostName());
		
		for(ApplicationByTerminal appTerminal : appTerminals) {
			
			if(mappedApps.containsKey(appTerminal.getApplicationId())){
				Application application = getDao(ApplicationDao.class).findByKey(appTerminal.getApplicationId());
				appTerminal.setApplication(application);
			}
		}
		
		return appTerminals;
		
	}
	
	protected 	List<Notification> fetchNotificationProviders() {
        return getDao(NotificationDao.class).findAll();
	}
	
	protected Terminal fetchTerminal() { 
		return getDao(TerminalDao.class).findByKey(getHostName());
	}
	
	protected List<ProductTypeData> fetchProductTypeData() {
		return getDao(ProductTypeDao.class).findAll();
	}
	
	public Notification fetchNotification(String name) {
	    return getDao(NotificationDao.class).findByKey(name);
	}
	
	public void close() {
		this.requestDispatcher.close();
	}
	
	protected boolean replaceScreenClass(Application application) {
		
		if(application == null) return false;
		String screenClass = application.getScreenClass();
		if(StringUtils.isEmpty(screenClass)) return false;
		String[] names = screenClass.split("\\.");
		String className = names[names.length -1];

		if(mappedScreenClasses.containsKey(className)){
			application.setScreenClass(mappedScreenClasses.get(className));
			return true;
		}
		
		return false;
		
	}
	
	
	class BuildProperties extends AbstractPropertyConfig {
	   public BuildProperties(String configResourcePath) {
		   super(configResourcePath);
	   }
	   
	   @Override
	   protected String getPreferedSuffix() {
		    SystemPropertyBean systemProperties = PropertyService.getPropertyBean(SystemPropertyBean.class);
		    return systemProperties.getSiteName().toLowerCase() + systemProperties.getAssemblyLineId();
	   }
	}
	
	private WebStartClientDao getWebStartClientDao() {
		return ServiceFactory.getDao(WebStartClientDao.class);
	}
	
	private boolean isHeadless() {
		return this.getArguments().isHeadless();
	}
	
}
