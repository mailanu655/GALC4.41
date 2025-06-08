package com.honda.galc.notification;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.service.NotificationExecutionService;
import com.honda.galc.util.ReflectionUtils;

public class ServerSideNotificationProducer extends NotificationProducer{

    private NotificationSubscriberDao notificationSubscriberDao;

    public ServerSideNotificationProducer(String name) {
        super(name);
    }
    
    public  <T extends INotificationSubscriber> ServerSideNotificationProducer(String name,List<T> subscribers) {
        super(name,subscribers);
    }
    
    public NotificationSubscriberDao getNotificationSubscriberDao() {
        return notificationSubscriberDao;
    }

    public void setNotificationSubscriberDao(NotificationSubscriberDao notificationSubscriberDao) {
        this.notificationSubscriberDao = notificationSubscriberDao;
    }
    
    @Override
    public void unsubscribe(ISubscriber subscriber) {
        super.unsubscribe(subscriber);
        notificationSubscriberDao.remove((NotificationSubscriber)subscriber);
    }
    
    @Override
    public void publish(NotificationRequest request, ISubscriber subscriber) {
    	if(subscriber.getClientHostName().startsWith("http")) {
    		HttpServiceProvider.getService(subscriber.getClientHostName(), NotificationExecutionService.class)
    			.execute(request);
    	}else if(!StringUtils.isEmpty(subscriber.getClientHostName()))
    		super.publish(request, subscriber);
    	else {
    		// server side subscriber 
    		// handler class is the bean name
    		Object bean = ApplicationContextProvider.getBean(request.getNotificationHandlerClass());
    		if(bean != null) {
    			ReflectionUtils.invoke(bean, request.getCommand(), request.getParams());
    		}
    	}
	
	}
}
