package com.honda.galc.service.broadcast;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ReflectionUtils;


/**
 * 
 * <h3>NotificationServiceBroadcast Class description</h3>
 * <p> NotificationServiceBroadcast description </p>
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
 * Aug 28, 2013
 *
 *
 */
public class NotificationServiceBroadcast extends AbstractBroadcast{

	public NotificationServiceBroadcast(BroadcastDestination destination,
			String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public DataContainer send(DataContainer dc) {
		
		try{
			sendNotification(dc);
		}catch(Exception ex){
			DataContainerUtil.error(logger, dc, ex, "Could not send notfication");
		}
		return dc;
		
	}
	
	@SuppressWarnings("unchecked")
	private void sendNotification(DataContainer dc) {
		
		String notificationName = destination.getDestinationId();
		if(StringUtils.isEmpty(notificationName)){
			DataContainerUtil.error(logger, dc, "notification name is empty");
			return;
		}
		
		String notificationClass = "com.honda.galc.notification.service." + notificationName;
		Class<INotificationService> clazz = null;
		try {
			clazz = (Class<INotificationService>)Class.forName(notificationClass);
		} catch (ClassNotFoundException e) {
			DataContainerUtil.error(logger, dc, e, "notification name " + notificationName  + " is not a valid notification");
			return;
		}
		
		Method[] methods = clazz.getDeclaredMethods();
		if(methods.length == 0) {
			DataContainerUtil.error(logger, dc, "No method defined for Notification class " + notificationName);
			return;
		}
		
		// TODO only takes the first one, what if there are multiple methods
		// in the interface?
		Method method = methods[0];
		List<String> attributes = DataContainerUtil.getAttributes(dc);
		
    	try {
    		 method = ReflectionUtils.getInterfaceMethod(clazz.getDeclaredMethods(), method.getName(), attributes.toArray());
    	} catch(NoSuchMethodException me) {		
			DataContainerUtil.error(logger, dc, me, "Method name " + method.getName()  + " is not a valid ");
			return;
    	}
		
		logger.info("notification service name : " + notificationName + "->" + method.getName());
		
		INotificationService notificationService = ServiceFactory.getNotificationService(clazz, processPointId);
		
		Object[] objects = getNotificationData(method);
		
		ReflectionUtils.invoke(notificationService, method.getName(), objects);
		
		logger.info("Successuly sent notification data " + Arrays.toString(objects));
		
	}
	
	private Object[] getNotificationData(Method method) {
		Object[] objects = new Object[method.getParameterTypes().length];
		List<String> attributes = DataContainerUtil.getAttributes(dc);
		
		int i = 0;
		for(Class<?> argumentClazz: method.getParameterTypes()) {
			String key = attributes.get(i);
			Object value = dc.get(key);
			Object result = DataContainerUtil.convert(value, argumentClazz);
			objects[i] = result;
			i++;
		}
		
		return objects;
	}

}
