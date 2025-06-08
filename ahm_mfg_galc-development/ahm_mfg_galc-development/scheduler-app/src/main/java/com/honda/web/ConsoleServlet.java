package com.honda.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.honda.scheduler.SchedulerManager;

/**
 * ConsoleServlet : This servlet provides a simple management gui to
 * interact with the scheduler via a browser.
 *  
 * @author      Suriya Sena
 * Date         3/18/2016
 */

public class ConsoleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ConsoleServlet.class.getName());
	private static final String START_CMD  = "/start";
	private static final String STOP_CMD   = "/stop";
	private static final String SYNC_CMD = "/sync";
	private static final String SHORT_STATUS_CMD = "/stat"; 
	private static final String LONG_STATUS_CMD = "/status";
	private static final String RUN_CMD = "/run";
	private static final String TASK_PARAM = "task";
	private static final String USAGE_CMD = "/help";
	
	
	private SchedulerManager schedulerManager;
	private String USAGE;
	
	public void init() {
		
		String ctx =""; //FIXME
	
		USAGE=String.format("\n\nhttp:/<host>:<port>/%s/[%s|%s|%s|%s|%s|%s]%n%n" +
				"%2$-10.10s\tLoad schedule from the database and start the scheduler%n" +
				"%3$-10.10s\tStop the scheduler%n" +
				"%4$-10.10s\tSync the scheduler with GALC database. Add new, remove deleted, update changed shedules%n" +
				"%5$-10.10s\tShow full scheduler status%n" +
				"%6$-10.10s\tOne line schedule status, for use by monitoring applications%n" +
				"%7$s?%8$s=name\tRun the named task immediatly%n"  +
				"%9$-10.10s\tShow this usage information%n" 
				,ctx,START_CMD,STOP_CMD,SYNC_CMD,LONG_STATUS_CMD,SHORT_STATUS_CMD,RUN_CMD,TASK_PARAM,USAGE_CMD);
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String method = request.getPathInfo();
    	String methodResult=USAGE;
    	
    	
    	if (method == null || method.compareToIgnoreCase(LONG_STATUS_CMD)==0  ) {
    		methodResult = longStatusSchedulerManager();
    	} else  if (method.compareToIgnoreCase(START_CMD)==0) {
    		methodResult = startStatusScheduleManager();
    	} else if (method.compareToIgnoreCase(STOP_CMD)==0) {
    		methodResult = stopScheduleManager();
    	} else if (method.compareToIgnoreCase(SYNC_CMD)==0) {
    		methodResult = reloadScheduleManager();
    	} else if (method.compareToIgnoreCase(LONG_STATUS_CMD)==0) {
    		methodResult = longStatusSchedulerManager();
    	} else if (method.compareToIgnoreCase(SHORT_STATUS_CMD)==0) {
    		methodResult = shortStatusScheduleManager();
    	} else if (method.compareToIgnoreCase(RUN_CMD) == 0) {
    		String taskname =request.getParameter(TASK_PARAM);
    		if (taskname != null && taskname.length() != 0 ) { 
        		methodResult = runNow(taskname);
    		}
    	} 
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String result = String.format("<pre>%s</pre>",methodResult);
        response.getWriter().println(result);
    }
    
    public String startStatusScheduleManager() {
    	String initialState = schedulerManager.getShortStatus();
    	schedulerManager.startScheduler();
    	return String.format("\n%-10.10s[%s]\n%-10.10s[%s]\n","start",initialState,"->",schedulerManager.getShortStatus());
    }
    
    public String stopScheduleManager() {
    	String initialState = schedulerManager.getShortStatus();
    	schedulerManager.stopScheduler();
    	return String.format("\n%-10.10s[%s]\n%-10.10s[%s]\n","stop",initialState,"->",schedulerManager.getShortStatus());
    }

    public String reloadScheduleManager() {
    	return schedulerManager.syncScheduler();
    }

    public String longStatusSchedulerManager() {
    	return schedulerManager.getStatus();
    }

    public String shortStatusScheduleManager() {
    	return schedulerManager.getShortStatus();
    }
    
    public String runNow(String taskname) {
    	boolean result=schedulerManager.scheduleImmediateTask(taskname);
    	return String.format("%s %s\n",taskname, result ? "scheduled successfully": "failed to schedule");
    }
    
	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}

}



