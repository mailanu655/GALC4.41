package com.honda.web;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import javax.servlet.http.HttpServlet;

/**
 * ServletContainer : Class to configure and start the Jetty embedded servlet container
 *  
 * @author      Suriya Sena
 * Date         3/18/2016
 */

public class ServletContainer  {

	private static final Logger log = LogManager.getLogger(ServletContainer.class.getName());
	private int port;
	private List<Map<String,Object>> servletConfigList;

	public void start() throws Exception {
		log.info(String.format("Starting on port [%d]",port));
	    Server server = new Server(port);
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
	    context.setContextPath("/");
	    server.setHandler(context);
	    
	    for (Map<String,Object> servletConfig : servletConfigList) {
	    	HttpServlet servlet = (HttpServlet)servletConfig.get("servlet");
	    	String contextName = (String)servletConfig.get("context");
		    context.addServlet(new ServletHolder(servlet),contextName);
		    
		    log.info("Starting..." + servlet.getClass().getName());
	    }

        server.start();
        server.join();
		log.info("Started successfully.");
	}

	
	public void setPort(int listenPort) {
		this.port = listenPort;
	}
	
	public void setServletConfigList(List<Map<String, Object>> servletConfigList) {
		this.servletConfigList = servletConfigList;
	}
}
