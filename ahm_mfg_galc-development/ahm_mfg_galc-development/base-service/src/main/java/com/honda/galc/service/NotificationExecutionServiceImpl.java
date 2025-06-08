package com.honda.galc.service;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.util.ReflectionUtils;

public class NotificationExecutionServiceImpl implements NotificationExecutionService{

	@Override
	public void execute(NotificationRequest request) {
   		// server side subscriber 
		// handler class is the bean name
		Object bean = ApplicationContextProvider.getBean(request.getNotificationHandlerClass());
		if(bean != null) {
			ReflectionUtils.invoke(bean, request.getCommand(), request.getParams());
		}
		
	}

}
