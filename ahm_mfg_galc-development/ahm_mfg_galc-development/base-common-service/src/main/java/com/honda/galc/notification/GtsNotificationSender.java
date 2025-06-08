package com.honda.galc.notification;

import java.lang.reflect.Proxy;

import com.honda.galc.notification.NotificationInvocationHandler;
import com.honda.galc.notification.service.IGtsNotificationService;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.service.INotificationServiceProvider;

/**
 * 
 * 
 * <h3>GtsNotificationSender Class description</h3>
 * <p> GtsNotificationSender description </p>
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
 * Jun 9, 2015
 *
 *
 */
public class GtsNotificationSender implements INotificationServiceProvider{
    
	private String areaName;			
    public GtsNotificationSender(String areaName){
        this.areaName = areaName;
    }
    
    @SuppressWarnings("unchecked")
    public  <T extends INotificationService> T getNotificationService(Class<T> serviceClass)  {
        GtsNotificationProducer producer = new GtsNotificationProducer(areaName);
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, 
                        new NotificationInvocationHandler(serviceClass.getName(),producer));
    }

	public <T extends INotificationService> T getNotificationService(Class<T> serviceClass, String producerPP) {
		return getNotificationService(serviceClass);
	}
    
    public static <T extends INotificationService> T getNotificationService(String trackingArea,Class<T> serviceClass) {
    	GtsNotificationSender sender = new GtsNotificationSender(trackingArea);
    	return sender.getNotificationService(serviceClass);
    }
    
    public static IGtsNotificationService getNotificationService(String areaName) {
    	return getNotificationService(areaName, IGtsNotificationService.class);
    }
}
