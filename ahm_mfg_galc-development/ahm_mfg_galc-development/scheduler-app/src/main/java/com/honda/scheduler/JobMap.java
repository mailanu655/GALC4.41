package com.honda.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JobMap : A registry of running jobs and their message queues. The HttpJobInvoker registers a job and message queue when a job is
 * started and waits on the queue for notification.   When GALC completes execution it will send a message to the JobNotificationServlet which
 * in turn forwards the message to the particular job which causes the HttpJobInvoker to complete.
 *  
 * @author      Suriya Sena
 * Date         4/6/2016
 */


public class JobMap {

	private static final Logger log = LogManager.getLogger(JobMap.class.getName());

	private ConcurrentHashMap<String, BlockingQueue<String>> activeJobMap = new ConcurrentHashMap<String, BlockingQueue<String>>();

	public void add(String jobId) {
		BlockingQueue<String> notificationQueue = new ArrayBlockingQueue<String>(1);
		activeJobMap.put(jobId, notificationQueue);
	}
	
	public void remove(String jobId) {
		activeJobMap.remove(jobId);
	}


	public void notify(String jobId, String status) {
		BlockingQueue<String> notificationQueue = activeJobMap.get(jobId);
		if (notificationQueue == null) {
			log.error(String.format("Failed to get notificationQueue for JobID [%s]!!!", jobId));
		} else {
			notificationQueue.add(status);
		}
	}
	
	public String wait(String jobId) {
		String status = null;
		BlockingQueue<String> notificationQueue = activeJobMap.get(jobId);
		if (notificationQueue == null) {
			log.error(String.format("Failed to get notificationQueue for JobID [%s]!!!", jobId));
		} else {
			try {
				status = notificationQueue.take();
			} catch (InterruptedException e) {
				log.error(String.format("Failed to wait for JobID [%s]!!!", jobId),e);
			}
		}
		return status;
	}

}
