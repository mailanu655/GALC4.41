package com.honda.galc.client.notification;

import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.net.TcpEndpoint;
import com.honda.galc.notification.service.ISubscriptionService;

public class ClientNotificationSubscriber {

	private ApplicationContext context;

	private HeartbeatProcess heartbeatProcess;


	public ClientNotificationSubscriber(ApplicationContext context) {
		this.context = context;
		startHeartbeatProcess();
	}

	private ISubscriptionService getSubscriptionService(NotificationProvider provider) {

		TcpEndpoint endPoint = new TcpEndpoint(provider.getHostName(),
				provider.getId().getHostIp(),
				provider.getId().getHostPort());

		return  getService(endPoint,ISubscriptionService.class);

	}


	private void startHeartbeatProcess() {
		heartbeatProcess = new HeartbeatProcess();
		heartbeatProcess.setDaemon(true);
		heartbeatProcess.start();
		getLogger().info("subscription heartbeat process started");
	}

	private void sendHeartBeat(){
		boolean sentHttp = false;

		/*    	for(Notification notification : notifications) {
            if(!notification.isClientOnly() && !sentHttp){
            	httpSendHeartbeat(notification);
            	sentHttp = true;
            }
            for(NotificationProvider provider : notification.getNotificationProviders()) {
                socketSendHeartbeat(provider);
            }
        }*/
	}

	private void httpSendHeartbeat(Notification notification) {
		try{
			boolean flag = getDao(NotificationSubscriberDao.class).heartbeat(
					notification.getNotificationClass(),
					context.getLocalHostIp(),
					context.getTerminal().getPort());
			if(!flag) notification.setConnected(false);
		}catch(ServiceTimeoutException e) {
			getLogger().error("failed to send subscription heartbeat to server. " + e.getMessage());
			notification.setConnected(false);
			return;
		}
	}

	private void socketSendHeartbeat(NotificationProvider provider) {
		TcpEndpoint endPoint = new TcpEndpoint(provider.getHostName(),
				provider.getId().getHostIp(),
				provider.getId().getHostPort());

		ISubscriptionService subscriptionService = getService(endPoint,
				ISubscriptionService.class);

		try{
			boolean flag = subscriptionService.heartbeat(
					provider.getId().getNotificationClass(),
					context.getLocalHostIp(),
					context.getTerminal().getPort());
			if(!flag) provider.setConnected(false);
		}catch(ServiceTimeoutException e) {
			getLogger().error("failed to send subscription heartbeat to server. " + e.getMessage());
			provider.setConnected(false);
			return;
		}
	}

	private class HeartbeatProcess extends Thread {
		public void run() {
			try {

				while(true) {
					Thread.sleep(30000);
					sendHeartBeat();

				}
			}catch (InterruptedException e) {
				getLogger().info("subscription heartbeat process exited");    
			}
		}    
	}

	public void close() {
		if(heartbeatProcess != null) heartbeatProcess.interrupt();
	}

}
