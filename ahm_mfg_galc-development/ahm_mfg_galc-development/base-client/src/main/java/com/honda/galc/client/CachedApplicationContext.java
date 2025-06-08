package com.honda.galc.client;


import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.net.ServiceMonitor.isServerAvailable;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ApplicationEnvService;

/**
 * 
 * <h3>ApplicationContext</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
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
 * @author Jeffray Huang
 * Oct 3, 2009
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class CachedApplicationContext extends ApplicationContext{
	
	private PersistentCache cache = new PersistentCache("ApplicationContext");
	
	public static ApplicationContext create(Arguments args) {
		CachedApplicationContext appContext = new CachedApplicationContext(args);
		appContext.load(); 
		applicationContext = appContext;
		return applicationContext;
	}
	
	
	public CachedApplicationContext(Arguments args) {
		super(args);
	}
	
	private void load() {
		if(!isServerAvailable()){
			getLogger().error("Could not load ApplicationContext due to server is not available");
		}else {
			getLogger().info("Loading Application Context...");
			loadBuildLabel();
			loadProductTypeData();
			loadTerminal();
			loadApplicationMenus();
			loadApplicationByTerminals();
			loadNotificationProviders();
			cache.flush();
			getLogger().info("ApplicationContext is loaded");
		}
		
		if(!cache.isLoaded()) {
			String message = "Application Context persistence cache is not loaded! " ;
			throw new SystemException(message);
		}
		
	}
	
	private void loadNotificationProviders() {
 
		try{
        	 List<Notification> notificationCategories = fetchNotificationProviders();
        	 cache.put("notificationCategories",notificationCategories);
        }catch(Exception e) {
            getLogger().error("Cannot load \"Terminal\" data from server. Server is not available! Using cache data");
        }
        
    }
	
	public List<Notification> getNotificationCategories() {
	    return cache.getList("notificationCategories", Notification.class);
	}
	
	public Notification getNotification(String name) {
	    for(Notification notification : getNotificationCategories()) {
	        if(notification.getNotificationClass().trim().equals(name)) return notification;
	    }
	    return null;
	}


	private void loadTerminal() {
		
		Terminal terminal = null;
		try{
			terminal = fetchTerminal();
			if(terminal != null) setApplicationId(terminal.getLocatedProcessPointId());
		}catch(Throwable e) {
            getLogger().error("Cannot load \"Terminal\" data from server. Server is not available! Using cache data");
		    return;
		}
		
		if(terminal == null) {
			// incorrect terminal name
			String message = "Failed to get terminal data for terminal \"" + getHostName() + "\". Please check the terminal name";
			throw new SystemException(message);
		}
		cache.put("Terminal", terminal);
	}
	
	private void loadBuildLabel(){
		String buildLabel = "Undefined";
		try{
			buildLabel = fetchBuildLabel();
		}catch(Throwable e) {
            getLogger().error("Cannot retrieve build label data from server. Server is not available! Using cache data");
		    return;
		}
		cache.put("BuildLabel",buildLabel);
	}
	
	private String fetchBuildLabel() {
		return getService(ApplicationEnvService.class).getBuildLevel();
	}
	
	private void loadProductTypeData() {
		
		List<ProductTypeData> productTypeDataList = null;
		try{
			productTypeDataList = fetchProductTypeData();
		}catch(Throwable e) {
            getLogger().error("Cannot load \"Product Type Data\" data from server. Server is not available! Using cache data");
		    return;
		}
		cache.put("ProductTypeData", productTypeDataList);
	}
	
	
	private void loadApplicationMenus() {
		
	    try{

			List<ApplicationMenuEntry> menus = fetchApplicationMenus();
			cache.put("ApplicationMenus", menus);

	    }catch(Exception e) {
	        getLogger().error("Cannot load \"Application Menu\" data from server. Server is not available! Using cache data");
	    }
		
	}
	
	private void loadApplicationByTerminals() {
		try{
			List<ApplicationByTerminal> applicationByTerminals = fetchApplicationByTerminals();
		    cache.put("ApplicationByTerminals",applicationByTerminals);
		}catch(Exception e) {
		    getLogger().error("Cannot load \"Application by Terminal\" data from server. Server is not available! Using cache data");
		}
	}
	
	
 	public Terminal getTerminal() {
	    return cache.get("Terminal", Terminal.class);
	}
	
 	public String getServerBuildLabel(){
 		return cache.get("BuildLabel",String.class);
 	}
 	
	private List<ApplicationByTerminal> getApplicationByTerminals() {
	    return cache.getList("ApplicationByTerminals", ApplicationByTerminal.class);
	}
	
	public List<ApplicationMenuEntry> getApplicationMenus() {
	    return cache.getList("ApplicationMenus",ApplicationMenuEntry.class);
	}
	
	public List<Application> getApplications() {
	    return cache.getList("Applications",Application.class);
	}
	
	@Override
	public List<ProductTypeData> getProductTypeDataList() {
		return cache.getList("ProductTypeData",ProductTypeData.class);
	}
	

	
	public Application getApplication(String appId) {
		for(ApplicationByTerminal app: getApplicationByTerminals()) {
			if (app.getId().getApplicationId().trim().equals(appId.trim())){
				Application application =  app.getApplication();
				replaceScreenClass(application);
				return application;
			}
		}
		
		return null;
	}
	
	public Application getDefaultApplication() {
		for(ApplicationByTerminal appByTerminal : getApplicationByTerminals()) {
			if(appByTerminal.getDefaultApplicationFlag() == 1){
				return getApplication(appByTerminal.getId().getApplicationId());
			}
		}
		return null;
	}


	
	


    
 }
