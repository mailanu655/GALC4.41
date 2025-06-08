package com.honda.galc.client.notification;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.notification.IProducer;
import com.honda.galc.notification.NotificationInvocationHandler;
import com.honda.galc.notification.NotificationProducer;
import com.honda.galc.notification.Subscriber;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.notification.service.ISubscriptionService;
import com.honda.galc.service.INotificationServiceProvider;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * 
 * <h3>ClientNotificationProvider Class description</h3>
 * <p> ClientNotificationProvider description </p>
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
 * @author Jeffray Huang<br>
 * Oct 29, 2015
 *
 *
 */
public class ClientNotificationProvider implements INotificationServiceProvider,ISubscriptionService{
    
    
    private List<NotificationProducer> producers = new ArrayList<NotificationProducer>();
    
    public ClientNotificationProvider(){
    }
    
    public static ClientNotificationProvider getInstance(){
        if(ApplicationContextProvider.containsBean(ServiceFactory.NOTIFICATION_SERVICE_PROVIDER))
            return (ClientNotificationProvider)ApplicationContextProvider.getBean(ServiceFactory.NOTIFICATION_SERVICE_PROVIDER);
        else return ApplicationContextProvider.RegisterBean(ServiceFactory.NOTIFICATION_SERVICE_PROVIDER, ClientNotificationProvider.class);
    }
    
    public NotificationProducer addProducer(String name) {
    	NotificationProducer producer; 
    	producer = findProducer(name);
    	if(producer != null) return producer;
    	producer = new NotificationProducer(name);
        producers.add(producer);
//        ApplicationContext.getInstance().getRequestDispatcher().reqisterSubscriptionService(this);
        return producer;
    }
    
    public NotificationProducer getProducer(String name) {
    	if(!hasProducer(name)) {
    		initProducers(name);
    	}
    	return findProducer(name);
    }
    
    public boolean hasProducer(String name){
        return findProducer(name) != null;
    }
    
    public NotificationProducer findProducer(String name) {
        for(NotificationProducer producer: producers) {
            if(producer.getName().equals(name)) return producer;
        }
        
        return null;
    }
    
    public boolean subscribe(String producerName,String clientHostName, int clientPort, String provider, String clientName) {
        
        IProducer producer = addProducer(producerName);
        producer.subscribe(new Subscriber(clientHostName,clientPort));
        Logger.getLogger().info("client " + clientHostName + ":" + clientPort + " subscribes to the notification service " + producerName);
        return true;
        
    }
    
    private void initProducers(String producerName) {
        List<NotificationSubscriber> subscribers = ServiceFactory.getDao(NotificationSubscriberDao.class).findAllByNotificationClass(producerName);
        for(NotificationSubscriber subscriber : subscribers) {
        	subscribe(producerName,subscriber.getClientHostName(),
        			subscriber.getClientPort(), subscriber.getId().getProvider(),
        			subscriber.getClientName());
        }
  
    }

    public boolean unsubscribe(String producerName, String clientHostName, int clientPort) {
        
        NotificationProducer producer = findProducer(producerName);
        if(producer == null) return false;
        return producer.unsubscribe(clientHostName, clientPort);
        
    }

    public boolean unsubscribe(String clientHostName, int clientPort) {
        boolean flag = false;
        for(NotificationProducer producer : producers) {
            if(producer.unsubscribe(clientHostName, clientPort)) flag = true;
        }
        
        return flag;
        
    }

    public boolean heartbeat(String producerName,String clientHostName, int clientPort) {
        
    	return contains(producerName,clientHostName,clientPort);
    	
    }
    
    private boolean contains(String producerName,String clientHostName,int clientPort) {
    	for(NotificationProducer producer : producers) {
    		if(producer.getName().equals(producerName) && producer.contains(clientHostName, clientPort)) return true;
    	}
    	return false;
    }

    @SuppressWarnings("unchecked")
    public  <T extends INotificationService> T getNotificationService(Class<T> serviceClass)  {
        NotificationProducer producer = getProducer(serviceClass.getSimpleName());
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, 
                        new NotificationInvocationHandler(serviceClass.getName(),producer));
    }

	@Override
	public <T extends INotificationService> T getNotificationService(Class<T> serviceClass, String producerPP) {
		return getNotificationService(serviceClass);
	}
}
