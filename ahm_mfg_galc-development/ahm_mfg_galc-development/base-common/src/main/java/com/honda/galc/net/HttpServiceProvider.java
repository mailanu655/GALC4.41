package com.honda.galc.net;

import java.lang.reflect.Proxy;

import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IService;
import com.honda.galc.service.IServiceProvider;



/**
 * 
 * <h3>DaoProviderProxy</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DaoProviderProxy description </p>
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
 * @author Jeffray Huang
 * Oct 5, 2009
 *
 */
public class HttpServiceProvider implements IServiceProvider{ 

    public static String url = "http://localhost:9080/BaseWeb/HttpServiceHandler";
	
	public static void setUrl(String urlString) {
	    url = urlString;
	}
	
	@SuppressWarnings("unchecked")
	public  <T extends IDaoService> T getDao(Class<T> iDao) {
		
		return (T) Proxy.newProxyInstance(iDao.getClassLoader(), new Class[] {iDao}, 
				new HttpServiceInvocationHandler(url,iDao.getSimpleName()));
		
	}

    @SuppressWarnings("unchecked")
    public <T extends IService> T getService(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
                        new HttpServiceInvocationHandler(url,service.getSimpleName()));

    }

	public boolean isServerAvailable() {
		return ServiceMonitor.isServerAvailable();
	}

	public boolean isServerSide() {
		return false;
	}

	public String createSession() {
		
		return new HttpRequestInvoker(url).createSession();
		
	}

	public void destroySession(String sessionCookieValue) {
		
		new HttpRequestInvoker(url).destroySession(sessionCookieValue);
		
	}

	@SuppressWarnings("unchecked")
	public <T extends IDaoService> T getDao(Class<T> iDao,
			String sessionCookieValue) {
		
		return (T) Proxy.newProxyInstance(iDao.getClassLoader(), new Class[] {iDao}, 
				new HttpServiceInvocationHandler(url,iDao.getSimpleName(),sessionCookieValue));
		
	}

	@SuppressWarnings("unchecked")
    public <T extends IService> T getService(Class<T> service,
			String sessionCookieValue) {
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
                new HttpServiceInvocationHandler(url,service.getSimpleName(),sessionCookieValue));

	}

	@SuppressWarnings({"unchecked" })
	public static <T extends IService> T getService(String serverUrl,Class<T> service) {
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
				new HttpServiceInvocationHandler(serverUrl, service.getSimpleName()));
	}
	
	@SuppressWarnings({"unchecked" })
	public static <T extends IDaoService> T getDao(String serverUrl,Class<T> iDao) {
		return (T) Proxy.newProxyInstance(iDao.getClassLoader(), new Class[] {iDao}, 
				new HttpServiceInvocationHandler(serverUrl, iDao.getSimpleName()));
	}
}
