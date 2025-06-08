package com.honda.scheduler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;


/**
 * HTTPInvokerJob : This class represents the Job to be run
 * by the scheduler. It will initiate the named GALC task on the remote server
 * via a call Rest Service and wait for a complete notification
 *  
 * @author      Suriya Sena
 * Date         3/17/2016
 */



public class HTTPInvokerJob  implements Job {
	
	public static final String TASKNAME_KEY ="task";
	public static final String REPLY_TO_URL="reply_to_url";
	public static final String SERVER_KEY ="server_url";
	public static final String USER="user";
	public static final String TIMEOUT_SECS="timeout_secs";
	private static final String agent = HTTPInvokerJob.class.getName();
	private static final Logger log = LogManager.getLogger(HTTPInvokerJob.class.getName());

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
        String task = (String) ctx.getJobDetail().getJobDataMap().get(TASKNAME_KEY);
        String serverURL = (String) ctx.getJobDetail().getJobDataMap().get(SERVER_KEY);
        String replyToURL = (String) ctx.getJobDetail().getJobDataMap().get(REPLY_TO_URL);
        Integer timeout = (Integer) ctx.getJobDetail().getJobDataMap().get(TIMEOUT_SECS);
        String user = (String) ctx.getJobDetail().getJobDataMap().get(USER);
        JobMap jobMap=getJobMap(ctx);
        
        long startTime = System.currentTimeMillis();
        String jobId =	UUID.randomUUID().toString();
        
		jobMap.add(jobId);
		if (invokeJob(task,jobId,serverURL,replyToURL,timeout, user)) {
	        String status = jobMap.wait(jobId);	        
			log.info(String.format("JobId[%s][%s] completed status [%s].",jobId,task,status));
		}
		jobMap.remove(jobId);
		log.info(String.format("JobId[%s][%s] execution time %ds.",jobId,task,(System.currentTimeMillis()-startTime)/1000));
	}
	
	private boolean invokeJob(String task, String jobId, String serverURL, String replyToURL, int timeout, String user) {
        HttpClient httpClient=null;
        
    	try {
    		httpClient=new  HttpClient();
        	httpClient.start();
    		ContentResponse response = httpClient.newRequest(serverURL)
    		        .method(HttpMethod.POST)
    		        .followRedirects(false)
    		        .timeout(timeout, TimeUnit.SECONDS)
    		        .content((new BytesContentProvider(createJsonPayload(task,jobId,replyToURL, user))), "text/json")
    		        .agent(agent)
    		        .send();
			log.info(String.format("JobId[%s][%s] execution request issued.",jobId,task));
		} catch (Exception e) {
			log.error(String.format("JobId[%s][%s] execution failed.",jobId,task), e);
			return false;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.stop();
				} catch (Exception e) {
					log.error("Error stopping HttpClient",e);
				}
			}
		}
		return true;
	}

	private JobMap getJobMap(JobExecutionContext ctx) {
		try {
			SchedulerContext schedulerCtx = ctx.getScheduler().getContext();
			return (JobMap) schedulerCtx.get(SchedulerManager.JOB_MAP);
		} catch (SchedulerException e) {
			log.error("Failed to get scheduler context ",e);
		}
		return null;
	}
	
	private byte[] createJsonPayload(String taskName, String jobId, String replyToAddr, String user) {
		String payload = String.format("[ { \"java.lang.String\" : \"%s\" }, {\"java.util.Map\": {\"userId\": \"%s\" }}, { \"java.lang.String\"  : \"%s\" } ,{\"java.lang.String\" :\"%s\"} ]", taskName, user , jobId, replyToAddr);
		return payload.getBytes();
	}
	
	

}
