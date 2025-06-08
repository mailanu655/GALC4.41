package com.honda.galc.webstart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.WebStartBuildDao;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.dao.conf.WebStartDefaultBuildDao;
import com.honda.galc.entity.conf.WebStartBuild;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.conf.WebStartDefaultBuild;
import com.honda.galc.property.WebStartPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class WebStartConfiguration {

	private static final String WEB_START = "Web Start";
	private static final String LEGACY = "LEGACY";
	private static Logger logger;
	private static WebStartConfiguration instance = new WebStartConfiguration();
	
	private WebStartConfiguration() {}
	
	public static WebStartConfiguration getInstance() {
		return  instance;
	}
	
	public WsClientConfig getClientConfig(String ipAddress, boolean isLinux) {
			final String logMethod = "getClientConfig():";
			WsClientConfig wsClientConfig = null;
			Date currTime = new Date();
			WebStartClient client = getWebStartClient(ipAddress);
				
			if (client != null) {
				getLogger().info(logMethod, "Client is: " + client.getHostName());
				String tTypeName = WebStartUtility.getTerminalType(client.getHostName()).getName();
				wsClientConfig = new WsClientConfig(ipAddress, client, tTypeName, currTime);
				setupBuild(wsClientConfig);
				setupHeapSize(wsClientConfig);
				configStartup(wsClientConfig, isLinux);
			} else {
				getLogger().info(logMethod, "Client not found");
			}
			return wsClientConfig;
		}
	  	
	  	private  WebStartClient getWebStartClient(String ipAddress) {
	  		WebStartClient client = null; 
	  		WebStartClientDao dao = ServiceFactory.getDao(WebStartClientDao.class);
	  		client = dao.findByKey(ipAddress);
	  		if(client == null) {
	  			client = ServiceFactory.getDao(WebStartClientDao.class).findByKey(WebStartConstants.DEFAULT_IP_ADDRESS);
	  		}
	  		return client;
	  	}
	  	
		private Logger getLogger() {
			if(logger == null) logger = Logger.getLogger(WEB_START);
			return logger;
		}

	    private void setupBuild(WsClientConfig wsClientConfig) {
	    	WebStartClient client = wsClientConfig.getWsClient();
	    	
			String cellName = WebStartUtility.getCellName();
			if(!wsClientConfig.getTerminalTypeName().equals(LEGACY)) {
				cellName +="_New";
				//setup build, default build
				PropertyService.refreshComponentProperties("ClientLoaderServlet");
				WebStartPropertyBean propertyBean = PropertyService.getPropertyBean(WebStartPropertyBean.class, "ClientLoaderServlet");

				if (propertyBean.isSetupDefaultBuild()) 
					WebStartBuildUtility.setupBuild(cellName, wsClientConfig);
			}
	    	String buildId = client.getBuildId();
			
			if (buildId != null) {
				if (buildId.equals(WebStartConstants.DEFAULT_BUILD_ID)) {
					wsClientConfig.setDefaultBuid(true);
					WebStartDefaultBuild defaultBuild = ServiceFactory.getDao(WebStartDefaultBuildDao.class).findByKey(cellName);
					if(defaultBuild == null) 
						throw new TaskException("Default build is not defined for cell " + cellName);
					buildId = defaultBuild.getDefaultBuildId();
					getLogger().info("Using cell name " + cellName + " to get the default build: " + buildId);
				} else {
					wsClientConfig.setDefaultBuid(false);					
				}
				
				WebStartBuild targetBuild = ServiceFactory.getDao(WebStartBuildDao.class).findByKey(buildId);
				
				if (targetBuild == null) {
					getLogger().warn("Setup Build", "No build data for: " + buildId);
				} else {				
					wsClientConfig.setBuild(targetBuild);
					
					// Find out Last-Modified attribute					
					Date modified = client.getUpdateTimestamp();
					if(modified == null) 
						modified = new Date();
					wsClientConfig.setLastModified(modified);
				}
			}
	    }

	    private void setupHeapSize(WsClientConfig wsClientConfig) {
	    	
	    	WebStartClient client = wsClientConfig.getWsClient();
	    	
	    	WebStartPropertyBean propertyBean = PropertyService.getPropertyBean(WebStartPropertyBean.class, "ClientLoaderServlet");
	    	
	    	wsClientConfig.setInitialHeapSize(propertyBean.getDefaultInitialHeapSize()+"m");
	    	wsClientConfig.setMaxHeapSize(propertyBean.getDefaultMaxHeapSize()+"m");
	    	
	    	String initialHeapSize = PropertyService.getProperty(client.getHostName(), "INITIAL_HEAP_SIZE");
	    	if(!StringUtils.isEmpty(initialHeapSize)) wsClientConfig.setInitialHeapSize(initialHeapSize+"m");
	    	String maxHeapSize = PropertyService.getProperty(client.getHostName(), "MAX_HEAP_SIZE");
	    	if(!StringUtils.isEmpty(maxHeapSize)) wsClientConfig.setMaxHeapSize(maxHeapSize+"m");
	    }
		
	    private void configStartup(WsClientConfig wsClientConfig, boolean isLinux) {
    		List<String> nativeJarFiles = new ArrayList<String>();
    		List<String> jarFiles = new ArrayList<String>();
    		
	    	WebStartPropertyBean propertyBean = PropertyService.getPropertyBean(WebStartPropertyBean.class, "ClientLoaderServlet");
    		wsClientConfig.setLookAndFeel(propertyBean.getLookAndFeel());
    		
    		if (!wsClientConfig.getTerminalTypeName().equals(LEGACY)) {
    			wsClientConfig.setUseCache(PropertyService.getPropertyBoolean(wsClientConfig.getWsClient().getHostName(), "USE CACHE", false));
    			wsClientConfig.setNewApp(true);
    		}

    		wsClientConfig.setMainClass(getProperty(wsClientConfig.getTerminalTypeName() + "_MAIN_CLASS"));
    		wsClientConfig.setDispacherURL(getProperty(wsClientConfig.getTerminalTypeName() + "_DISPATCHER_URL"));  
    		jarFiles = getJarFiles(wsClientConfig.getTerminalTypeName() + "_JARS", wsClientConfig.getBuild().getBuildId());
 	
        	setJnlpArgs(wsClientConfig, propertyBean, isLinux);
	    	processNativeJars(nativeJarFiles, jarFiles);
	    	wsClientConfig.setJars(jarFiles);
	       	wsClientConfig.setNativeJars(nativeJarFiles);   
	    }
	    	    
	    /**
	     * add JNLP args
	     * 
	     * @param wsClientConfig
	     * @param client
	     * @param propertyBean
	     * @param isLinux
	     */
		private void setJnlpArgs(WsClientConfig wsClientConfig, WebStartPropertyBean propertyBean, boolean isLinux) {
	    	List<String> args = new ArrayList<String>();
	    	
	    	WebStartClient client = wsClientConfig.getWsClient();
	    	if(!wsClientConfig.getTerminalTypeName().equals(LEGACY)) {
				if (isLinux){
					args.add(propertyBean.getAlcDirLinux());
				}else{
					args.add(propertyBean.getAlcDirWindows());
				}
        		
			}
			args.add(wsClientConfig.getDispacherURL());
        	args.add(client.getHostName());
        	args.add(wsClientConfig.getLookAndFeel());
        	
        	if(!wsClientConfig.getTerminalTypeName().equals(LEGACY)) {
        		args.add(Boolean.toString(wsClientConfig.isUseCache()));
        	}
        	
  		    if(!WebStartConstants.DEFAULT_IP_ADDRESS.equalsIgnoreCase(client.getIpAddress())
					&& isClientInLightSecurityGroup(client.getHostName())) {
  			   args.add("-user=" + client.getHostName());
  			   args.add("-pwd=" + client.getHostName());
		    }
  		    
        	wsClientConfig.setArgs(args);
		}
		
		private boolean isClientInLightSecurityGroup(String hostName) {
			String securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", hostName);
			if(securityFlag == null) securityFlag = PropertyService.getProperty("LIGHT_CLIENT_SECURITY", "*");
			return (securityFlag != null && Boolean.parseBoolean(securityFlag));
		}

	    /**
	     * identify native jars, add them to nativeJarFiles and remove them from the jarFiles list 
	     * 
	     * @param nativeJarFiles
	     * @param jarFiles
	     */
		private void processNativeJars(List<String> nativeJarFiles, List<String> jarFiles) {
			for (String jarElem : jarFiles) {
	    		String[] parts = jarElem.split(":", 2);
	    		if(parts.length > 1 && parts[0].equalsIgnoreCase("N")) {
	    			// It's native library - lets process it
	    			nativeJarFiles.add(parts[1]);
	    		}
	    	}
	    	
	    	for (String nativeJarElem : nativeJarFiles){
	    		jarFiles.remove("N:"+nativeJarElem);
	    	}
		}
		
	    
		private  List<String> getJarFiles(String jarFilePrefix, String buildId) {
			List<String> jarFiles = new ArrayList<String>();
			for(int i = 1;; i++) {
				String keyName = jarFilePrefix + String.format("_%02d", i);
				String jarString = PropertyService.getProperty("ClientLoaderServlet", keyName);
				if(jarString == null) break;
				jarString = jarString.replaceAll("@buildNum@", "-"+buildId);
				String[] jarElems = jarString.split("\\s*,\\s*");
				for (String jarElem : jarElems) {
					jarFiles.add(jarElem);
				}
			}
			return jarFiles;
		}
		
		private String getProperty(String propertyKey) {
			return PropertyService.getProperty("ClientLoaderServlet", propertyKey);
		}
}