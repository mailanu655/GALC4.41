package com.honda.scheduler;




import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.honda.web.ServletContainer;


/**
 * AppLauncher : is the main application class which
 * loads the ScheduleManger and ServletContainer sub components.
 * 
 * The application properties such as database connection strings
 * are defined in the application.xml and application.properties files.
 * Paramters defined in the application.properties file can be
 * overridden at runtime by either setting environment variables or
 * by using the -Dpropertyname-value. See documentation for
 * org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 *  
 * @author      Suriya Sena
 * Date         3/18/2016
 */

public class AppLauncher {
	
	

	public static void main(String[] args) throws Exception {
	  	 String configurationFile = "application.xml";
		 ApplicationContext ctx =  new ClassPathXmlApplicationContext(configurationFile);
	
		 startScheduler(ctx);
		 startWebGui(ctx);
	}
	
	
	public static void startScheduler(ApplicationContext ctx) {
		 SchedulerManager mgr= (SchedulerManager) ctx.getBean("SchedulerManager");
		 mgr.startScheduler();
	}
	
	public static void startWebGui(ApplicationContext ctx) throws Exception {
		 ServletContainer gui = (ServletContainer) ctx.getBean("ServletContainer");
		 gui.start();
	}

}

