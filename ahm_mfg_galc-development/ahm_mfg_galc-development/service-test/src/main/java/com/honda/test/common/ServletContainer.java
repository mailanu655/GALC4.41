package com.honda.test.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import com.honda.galc.web.HttpDeviceHandler;
import com.honda.galc.web.HttpServiceHandler;

import javax.servlet.http.HttpServlet;

/**
 * 
 * 
 * <h3>ServletContainer Class description</h3>
 * <p> ServletContainer description </p>
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
 * @author hcm_adm_008925<br>
 * Jul 30, 2018
 *
 *
 */

public class ServletContainer  {
	public static Server server = null;
	
	public static void start(int port)  {
		if(server != null) return;
	    server = new Server(port);
	    
	    Log.setLog(new StdErrLog());
	    
	    ContextHandlerCollection contexts = new ContextHandlerCollection();
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
	    context.setContextPath("/");
	    HttpServiceHandler serviceHandler = new HttpServiceHandler();
	    context.addServlet(new ServletHolder(serviceHandler),"/BaseWeb/HttpServiceHandler");
	    
	    HttpDeviceHandler deviceHandler = new HttpDeviceHandler();
	    context.addServlet(new ServletHolder(deviceHandler),"/BaseWeb/HttpDeviceHandler");
	    
	    contexts.addHandler(context);
	    
	    server.setHandler(contexts);
	    
	    Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					server.start();
					server.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	    
	    thread.start();
        
	}
	
	public static void stop() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
