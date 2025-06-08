package com.honda.galc.notification;

import static com.honda.galc.common.logging.Logger.getLogger;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.task.TaskExecutor;

import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.net.NotificationRequest;

/**
 * Asyncronous bean
 * @author is08925
 *
 */
public class NotificationServiceExecutorImpl implements NotificationServiceExecutor{
	
	private TaskExecutor taskExecutor;
	
	public NotificationServiceExecutorImpl(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void invoke(IProducer producer, NotificationRequest request) {
		getLogger().debug("sending notification " + request + " to suscribers");
		
		
		for(ISubscriber subscriber : producer.getSubscribers()) {
     		
			NotificationRequest newRequest = cloneRequest(request);

			if (subscriber instanceof NotificationSubscriber) {
     			String notificationHandlerClass = ((NotificationSubscriber) subscriber).getNotificationHandlerClass();
     		    if (!StringUtils.isEmpty(notificationHandlerClass)) {
     		    	newRequest.setNotificationHandlerClass(notificationHandlerClass);
     			}
     		}
     		taskExecutor.execute(new NotificationTask(producer, subscriber, newRequest));
        }
	}
	
	private NotificationRequest cloneRequest(NotificationRequest request) {
		NotificationRequest newRequest = new NotificationRequest(request.getTargetClass(), request.getCommand(), request.getParams());
		newRequest.setHostName(request.getHostName());
		newRequest.setIp(request.getIp());
		newRequest.setPort(request.getPort());
		newRequest.setNotificationHandlerClass(request.getNotificationHandlerClass());
		return newRequest;
	}
	
	private class  NotificationTask implements Runnable {
		
		ISubscriber subscriber;
		NotificationRequest request;
		IProducer producer;
		
		NotificationTask(IProducer producer, ISubscriber subscriber, NotificationRequest request) {
			this.producer = producer;
			this.subscriber = subscriber;
			this.request = request;
		}
		
		public void run() {
			producer.publish(request, subscriber);
		}
	}
}
