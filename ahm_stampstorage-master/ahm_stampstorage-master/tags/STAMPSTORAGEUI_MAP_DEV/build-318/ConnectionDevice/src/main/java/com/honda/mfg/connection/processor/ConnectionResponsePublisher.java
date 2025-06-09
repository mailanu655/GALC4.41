package com.honda.mfg.connection.processor;
/**
 * User: Michael Grecol
 * Date: 08/26/14
 * This class reads from the queue and publishes to the event bus. This frees up the connecton reader to continue to read while the event bus processes.
 * 
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.schedule.Scheduler;

public class ConnectionResponsePublisher implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionResponsePublisher.class);
	private BlockingQueue<Object> messageQueue;
	public ConnectionResponsePublisher(String threadName){
		messageQueue= new LinkedBlockingQueue <Object>();
		 new Scheduler(this, threadName);
	}
	public void run() {
		while(true){
			 Object response;
			try {
				LOG.debug("Waiting For message from ConnectionResponseReader");
				response = messageQueue.take();
		        LOG.debug("Publishing message:  " + response);
		        EventBus.publish(response);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e){
				LOG.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public void put (Object o){
		try {
			messageQueue.put(o);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
