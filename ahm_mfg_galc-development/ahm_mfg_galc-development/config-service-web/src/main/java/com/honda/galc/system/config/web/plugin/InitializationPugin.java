package com.honda.galc.system.config.web.plugin;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.service.property.PropertyService;

public class InitializationPugin implements PlugIn {

	private static final String CONFIG_WEB = "WebConfig";
	
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init(ActionServlet actionServlet, ModuleConfig moduleConfig)
			throws ServletException {
	    
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(actionServlet.getServletContext());
	    ApplicationContextProvider.setApplicationContext(ctx); 
	    
	    // allow log4j to load the log4j.property dynamically based on the machine host name or preferred suffix
	    ServerSideLoggerConfig.configLog4j();

	    initLogContext();
	    
	    Logger.getLogger(CONFIG_WEB).info("Application context and logger are initialized");
	}
	
	private void initLogContext(){
	    LogContext context = LogContext.getContext();
	    context.setApplicationName(CONFIG_WEB);
	    String level = PropertyService.getProperty(CONFIG_WEB, LogLevel.LOG_LEVEL, LogLevel.DATABASE.name());
	    context.setApplicationLogLevel(LogLevel.getLogLevel(level));
	    System.out.println("Current Log Level for Web configurator : " + context.getApplicationLogLevel());
	}
	
	public static Logger getLogger() {
		return Logger.getLogger(CONFIG_WEB);
	}


}
