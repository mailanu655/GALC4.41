package com.honda.galc.service;

import java.lang.reflect.Proxy;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.TcpEndpoint;
import com.honda.galc.notification.service.INotificationService;

public class ServiceFactory {
	
	public static final String SERVICE_PROVIDER = "ServiceProvider";
	public static final String NOTIFICATION_SERVICE_PROVIDER = "NotificationServiceProvider";

	public  static <T extends IDaoService<?,?>> T getDao(Class<T> iDao) {
		
		return getServiceProvider().getDao(iDao);
	}
	
	public  static <T extends IService> T getService(Class<T> iService) {
        
        return getServiceProvider().getService(iService);
    }
	
	public  static <T extends IDaoService<?, ?>> T getDao(Class<T> iDao,String sessionCookieValue) {
		
		return getServiceProvider().getDao(iDao,sessionCookieValue);
	}
	
	public  static <T extends IService> T getService(Class<T> iService,String sessionCookieValue) {
        
        return getServiceProvider().getService(iService,sessionCookieValue);
        
    }
	
	public static String createSession() {
		return getServiceProvider().createSession();
	}
	
	public static void destroySession(String sessionCookieValue) {
		getServiceProvider().destroySession(sessionCookieValue);
	}
	
	public static boolean isServerAvailable() {
		return getServiceProvider().isServerAvailable();
	}
	
	public static boolean isServerAvailable(String ip, int port) {
		return new TcpEndpoint(ip,port).connect();
	}
	
	private static IServiceProvider getServiceProvider() {
		return (IServiceProvider)ApplicationContextProvider.getBean(SERVICE_PROVIDER);
	}
	
	@SuppressWarnings("unchecked")
    public static <T extends IService> T getService(String ip, int port,Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
                        new SocketServiceInvocationHandler(ip,port,service.getName()));
    }
	
	@SuppressWarnings("unchecked")
    public static <T extends IService> T getService(TcpEndpoint endPoint,Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
                        new SocketServiceInvocationHandler(endPoint,service.getName()));
    }
	
    public static <T extends INotificationService> T getNotificationService(Class<T> serviceClass) {
        INotificationServiceProvider serviceProvider = (INotificationServiceProvider)ApplicationContextProvider.getBean(NOTIFICATION_SERVICE_PROVIDER);
        return serviceProvider.getNotificationService(serviceClass, "");
    }
    
    public static <T extends INotificationService> T getNotificationService(Class<T> serviceClass, String producerPP) {
        INotificationServiceProvider serviceProvider = (INotificationServiceProvider)ApplicationContextProvider.getBean(NOTIFICATION_SERVICE_PROVIDER);
        return serviceProvider.getNotificationService(serviceClass, producerPP);
    }
    
    public static boolean isServerSide() {
    	return getServiceProvider().isServerSide();
    }
}
