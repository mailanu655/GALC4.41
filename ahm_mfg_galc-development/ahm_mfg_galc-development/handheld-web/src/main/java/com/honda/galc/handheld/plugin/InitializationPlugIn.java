package com.honda.galc.handheld.plugin;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.data.ApplicationContextProvider;

public class InitializationPlugIn implements PlugIn {

	private static final String HANDHELD = "Handheld GALC";

	public void destroy() {
	}

	public void init(ActionServlet actionServlet, ModuleConfig moduleConfig)
			throws ServletException {
	    
	    ApplicationContextProvider.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(actionServlet.getServletContext())); 
	    
	    // allow log4j to load the log4j.property dynamically based on the machine host name or preferred suffix
	    ServerSideLoggerConfig.configLog4j();

	    initLogContext();
	    
	    Logger.getLogger(HANDHELD).info("Application context and logger are initialized");
	}
	
	private void initLogContext(){
	    LogContext context = LogContext.getContext();
	    context.setApplicationName(HANDHELD);
	    getLogger().info("Current Log Level for Handhled GALC: " + context.getApplicationLogLevel());
	}
	
	public static Logger getLogger() {
		return Logger.getLogger(HANDHELD);
	}

	public static void info(String userId, String message) {
		getLogger().info(userId == null
				? message 
				: String.format("User: %1s - %2s", userId, message));
	}

	public static void info(String message) {
		info(null, message);
	}

	public static void error(String userId, String message) {
		getLogger().error(userId == null
				? message 
				: String.format("User: %1s - %2s", userId, message));
	}
	
	public static void error(String userId, String message, Throwable exception) {
		getLogger().error(exception, userId == null
				? message 
				: String.format("User: %1s - %2s", userId, message));
	}

	public static void error(String message) {
		error(null, message);
	}
}
