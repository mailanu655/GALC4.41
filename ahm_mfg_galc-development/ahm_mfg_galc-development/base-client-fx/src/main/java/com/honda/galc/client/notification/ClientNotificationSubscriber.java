package com.honda.galc.client.notification;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import static com.honda.galc.common.logging.Logger.getLogger;
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.net.TcpEndpoint;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.notification.service.ISubscriptionService;

import static com.honda.galc.service.ServiceFactory.getService;
import static com.honda.galc.service.ServiceFactory.getDao;

public class ClientNotificationSubscriber {
    
    private ApplicationContext context;
    
    private List<Notification> notifications = new ArrayList<Notification>();
    
    private HeartbeatProcess heartbeatProcess;
    
    
    public ClientNotificationSubscriber(ApplicationContext context) {
        this.context = context;
        startHeartbeatProcess();
    }
    
    public void subscribe(String notificationName,INotificationService notificationService) {
        if(getNotification(notificationName) != null) {
            // already suscribered
            return;
        }
        
        Notification notification = context.getNotification(notificationName);
        if(notification == null) {
            // category does not exist
            return;
        }
        
        notifications.add(notification);
        
        httpSubscribe(notification);
        if(notification.getNotificationProviders().size() > 0) socketSubscribe(notification);
    }
    
    
    private void socketSubscribe(Notification notification) {
      for(NotificationProvider provider : notification.getNotificationProviders()) {
          socketSubscribe(provider);
      }
    }

    private boolean socketSubscribe(NotificationProvider provider) {
                                                               
        boolean flag = false;
        try{
            flag = getSubscriptionService(provider).subscribe(                
                       provider.getId().getNotificationClass(),
                       context.getLocalHostIp(),
                       context.getTerminal().getPort(),
                       "",
                       context.getHostName());
        }catch(RuntimeException e) {
            getLogger().error("failed to subscribe to server. " + e.getMessage());
            provider.setConnected(false);
            return false;
        }
        if(flag) getLogger().info("subscribed to notification service \"", 
                                provider.getId().getNotificationClass(), "\" at host ",
                                provider.getId().getHostIp(), ":",
                                String.valueOf(provider.getId().getHostPort()));
        provider.setConnected(flag);
        return flag;
        
    }
    
    private ISubscriptionService getSubscriptionService(NotificationProvider provider) {
    	
    	TcpEndpoint endPoint = new TcpEndpoint(provider.getHostName(),
                provider.getId().getHostIp(),
                provider.getId().getHostPort());

    	return  getService(endPoint,ISubscriptionService.class);
    	
    }
    
    public void unsubscribe(String notificationName,INotificationService notificationService) {
    	
    	 Notification notification = getNotification(notificationName);
    	 if(notification == null) return;
    	 unsubscribe(notification); 
    	 notifications.remove(notification);
    }
    
    public void socketUnsubscribe(Notification notification) {
      for(NotificationProvider provider : notification.getNotificationProviders()) {
          socketUnsubscribe(provider);
      }
    }
    
    private boolean socketUnsubscribe(NotificationProvider provider) {
    	boolean flag = false;
        try{
            flag = getSubscriptionService(provider).unsubscribe(                
                       provider.getId().getNotificationClass(),
                       context.getLocalHostIp(),
                       context.getTerminal().getPort());
        }catch(RuntimeException e) {
            getLogger().error("failed to unsubscribe to server. " + e.getMessage());
            return false;
        }
        if(flag) getLogger().info("unsubscribed to notification service \"", 
                                provider.getId().getNotificationClass(), "\" at host ",
                                provider.getId().getHostIp(), ":",
                                String.valueOf(provider.getId().getHostPort()));
        provider.setConnected(flag);
        return flag;
    }
    
    private void httpSubscribe(Notification notification) {
        if(notification.isClientOnly()) return; 
        try{
            getDao(NotificationSubscriberDao.class).subscribe(
                            notification.getNotificationClass(),
                            context.getLocalHostIp(),
                            context.getTerminal().getPort(),
                            "",
                            context.getHostName());
        }catch(BaseException e) {
            getLogger().error("failed to subscribe to server." + e.getMessage());
            notification.setConnected(false);
            return;
        }
        notification.setConnected(true);
        getLogger().info("subscribed to notification service \"" + notification.getNotificationClass() + "\" at http server");
    }
    
    private void httpUnsubscribe(Notification notification) {
        if(notification.isClientOnly()) return; 
        try{
            getDao(NotificationSubscriberDao.class).unsubscribe(
                            notification.getNotificationClass(),
                            context.getLocalHostIp(),
                            context.getTerminal().getPort());
        }catch(BaseException e) {
            getLogger().error("failed to unsubscribe to server." + e.getMessage());
            return;
        }
        getLogger().info("unsubscribed to notification service \"" + notification.getNotificationClass() + "\" at http server");
        
    }
    
    private void startHeartbeatProcess() {
        heartbeatProcess = new HeartbeatProcess();
        heartbeatProcess.setDaemon(true);
        heartbeatProcess.start();
        getLogger().info("subscription heartbeat process started");
    }

    private Notification getNotification(String notificationClass) {
            
        for(Notification notification : notifications) {
            if(notification.getNotificationClass().trim().equals(notificationClass)) return notification;
        }
        return null;
        
    }
    
    private void sendHeartBeat(){
    	boolean sentHttp = false;
    	
    	for(Notification notification : notifications) {
            if(!notification.isClientOnly() && !sentHttp){
            	httpSendHeartbeat(notification);
            	sentHttp = true;
            }
            for(NotificationProvider provider : notification.getNotificationProviders()) {
                socketSendHeartbeat(provider);
            }
        }
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
       if(!notification.isConnected()) {
           httpSubscribe(notification);
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
        
        if(!provider.isConnected()) {
            socketSubscribe(provider);
        }
        
    }
    
    private void unsubscribe(Notification notification) {
    	if(!notification.isClientOnly()) httpUnsubscribe(notification);
        for(NotificationProvider provider : notification.getNotificationProviders()) {
            socketUnsubscribe(provider);
        }
    }
    
    private void unsubscribeAll() {
    	for(Notification notification : notifications) {
            unsubscribe(notification);
        }
    	notifications.clear();
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
    	unsubscribeAll();
    }
  
}
