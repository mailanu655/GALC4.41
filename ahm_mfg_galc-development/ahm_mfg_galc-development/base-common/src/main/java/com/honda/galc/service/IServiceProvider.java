package com.honda.galc.service;


public interface IServiceProvider {
    @SuppressWarnings("unchecked")
    public <T extends IDaoService> T getDao(Class<T> iDao);
    
    public <T extends IService> T getService(Class<T> iService);
    
    @SuppressWarnings("unchecked")
    public <T extends IDaoService> T getDao(Class<T> iDao,String sessionCookieValue);
    
    public <T extends IService> T getService(Class<T> iService,String sessionCookieValue);
    
    public boolean isServerAvailable();
    
    public boolean isServerSide();
    
    public String createSession();
    
    public void destroySession(String sessionCookieValue);
    
}
