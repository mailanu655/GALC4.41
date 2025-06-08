package com.honda.galc.notification;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.service.INotificationServiceProvider;

public class ServerSideNotificationProvider implements INotificationServiceProvider{
    
    @Autowired
    private NotificationSubscriberDao notificationSubscriberDao;
   
    private List<NotificationProducer> producers = new ArrayList<NotificationProducer>();
    
    public ServerSideNotificationProvider(){
        
    }
    
    public void addProducer(String name) {
        if(hasProducer(name)) return;
        producers.add(new NotificationProducer(name));
    }
    
    public boolean hasProducer(String name){
        return getProducer(name) != null;
    }
    
    public ServerSideNotificationProducer getProducer(String name) {
    	return getProducer(name, "");
    }
    
    public ServerSideNotificationProducer getProducer(String name, String producerPP) {
        List<NotificationSubscriber> subscribers = notificationSubscriberDao.findAllByNotificationClass(name);
        
        if  (producerPP == null) {
        	producerPP = "";		
        }
        
        ArrayList<NotificationSubscriber> filteredSubscribers = filterSubscribers(subscribers, producerPP);
        if(filteredSubscribers.isEmpty()) return null;
        return new ServerSideNotificationProducer(name, filteredSubscribers);
    }

    /**
     * returns a list of subscribers that are unique based on the IP & port.  
     * A subscriber is added if it meets the following criteria.
     * If the subscription type is INCLUDE 
     *    the provider should be 'ANY' (OR)
     *    the provider needs to match the producer's process point.
     * If the subscription type is EXCLUDE
     *    the provider should NOT be 'ANY' (AND)
     *    the provider should NOT match the producer's process point.
     * @param subscribers
     * @param producerPP
     * @return
     */
	private ArrayList<NotificationSubscriber> filterSubscribers(List<NotificationSubscriber> subscribers, String producerPP) {
		HashMap<String, NotificationSubscriber> subscribersMap = new HashMap<String, NotificationSubscriber>();
        for(NotificationSubscriber subscriber: subscribers) {
        	String clientId = subscriber.getId().getClientIp()+subscriber.getId().getClientPort();
        	switch(subscriber.getSubscriptionType()) {

        	case INCLUDE:
        		if (subscriber.getId().getProvider().equals(NotificationSubscriber.ANY_PROVIDER) ||
        				subscriber.getId().getProvider().equals(producerPP)) {
        			addSubscriber(subscribersMap, subscriber, clientId);
        		}
        		break;

        	case EXCLUDE:
        		addSubscriber(subscribersMap, subscriber, clientId);
        		if (subscriber.getId().getProvider().equals(NotificationSubscriber.ANY_PROVIDER) ||
        				subscriber.getId().getProvider().equals(producerPP)) {
        			removeSubscriber(subscribersMap, clientId);
        		}
        		break;

        	default:
        		break;
        	}
        }
		return new ArrayList<NotificationSubscriber>(subscribersMap.values());
	}

	private void removeSubscriber(HashMap<String, NotificationSubscriber> subscribersMap,	String clientId) {
		if (subscribersMap.containsKey(clientId)) {
			subscribersMap.remove(clientId);
		}
	}

	private void addSubscriber(HashMap<String, NotificationSubscriber> subscribersMap, NotificationSubscriber subscriber, String clientId) {
		if (!subscribersMap.containsKey(clientId)) {
			subscribersMap.put(clientId, subscriber);
		}
	}
    
    public  <T extends INotificationService> T getNotificationService(Class<T> serviceClass)  {
    	return getNotificationService(serviceClass, "");
    }

    @SuppressWarnings("unchecked")
	public <T extends INotificationService> T getNotificationService(Class<T> serviceClass, String producerPP) {
        ServerSideNotificationProducer producer = getProducer(serviceClass.getSimpleName(), producerPP);
        if(producer != null)
        	producer.setNotificationSubscriberDao(notificationSubscriberDao);
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, 
                        new NotificationInvocationHandler(serviceClass.getName(),producer));
	}
}
