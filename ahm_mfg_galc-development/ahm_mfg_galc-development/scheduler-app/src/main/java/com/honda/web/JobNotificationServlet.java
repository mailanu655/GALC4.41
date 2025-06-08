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
 * JobNotificationServlet : Handle job completion notifications from GALC
 * 
 * When a job completes GALC will send a notification for the jobid, the notification servlet
 * then forwards the notification to the running quartz job.
 *  
 * @author      Suriya Sena
 * Date         4/4/2016
 */

public class JobNotificationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(JobNotificationServlet.class.getName());
	private static final String NOTIFY_CMD  = "/notify";
	private static final String JOB_ID_PARAM = "id";
	private static final String JOB_STATUS_PARAM =  "status";
    private SchedulerManager schedulerManager;

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String method = request.getPathInfo();
    	
    	if (method == null || method.compareToIgnoreCase(NOTIFY_CMD)==0  ) {
    		handleNotification(request);
    	} 
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

	private void handleNotification(HttpServletRequest request) {
		String jobId =request.getParameter(JOB_ID_PARAM);
		String status =request.getParameter(JOB_STATUS_PARAM);
        schedulerManager.notify(jobId, status);		
		log.info(String.format("Jobid [%s] received notification status[%s]", jobId, status));
	}

	public void setSchedulerManager(SchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
	}
}



