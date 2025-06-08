package com.honda.galc.service;


import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.service.IServiceProvider;

/**
 * 
 * @author is08925
 *
 */
public class ServiceProvider implements IServiceProvider{ 

	
	@SuppressWarnings("unchecked")
	public  <T extends IDaoService> T getDao(Class<T> iDao) {
		
		return (T) ApplicationContextProvider.getBean(iDao.getSimpleName());
		
	}

    @SuppressWarnings("unchecked")
    public <T extends IService> T getService(Class<T> iService) {
        return (T) ApplicationContextProvider.getBean(iService.getSimpleName());
    }

	public boolean isServerAvailable() {
		return true;
	}

	public boolean isServerSide() {
		return true;
	}

	/**
	 * server side - no implementaion
	 */
	public String createSession() {
		return null;
	}
	
	/**
	 * server side - no implementaion
	 */
	public void destroySession(String sessionCookieValue) {
		
	}
	
	/**
	 *server side no session  - no implementation
	 */
	
	@SuppressWarnings("unchecked")
    public <T extends IDaoService> T getDao(Class<T> dao,
			String sessionCookieValue) {
		
		return getDao(dao);
		
	}
	
	/**
	 *server side no session - no implementation
	 */
	public <T extends IService> T getService(Class<T> service,
			String sessionCookieValue) {
		return getService(service);
	}

}
