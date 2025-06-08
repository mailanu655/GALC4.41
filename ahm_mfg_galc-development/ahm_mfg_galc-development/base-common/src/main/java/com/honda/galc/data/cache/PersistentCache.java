package com.honda.galc.data.cache;


import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class PersistentCache {
    protected  String cacheName= null;
    
    public PersistentCache() {
    }
    
    public PersistentCache(String cacheName) {
    	this.cacheName = cacheName;
    }

    protected String getCacheName() {
        return (cacheName != null) ? cacheName : 
        	getClass().getSimpleName();
    }
    
    public void put(Object key, Object value) {
    	synchronized (getCacheName()) {
	    	getCache().put(new Element(key,value));
	    	getCache().flush();
    	}
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(Object key,Class<T> c) {
        Element element = getCache().get(key);
        if(element == null) return null;
        return (T)element.getObjectValue();
    }
    
    public String getStringValue(Object key) {
        Element element = getCache().get(key);
        if(element == null) return null;
        return (String) element.getObjectValue();
    }
    
    public Integer getIntValue(Object key) {
        Element element = getCache().get(key);
        if(element == null) return null;
        return (Integer) element.getObjectValue();
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Object key, Class<T> c) {
         Element element = getCache().get(key);
         if(element == null) return new ArrayList<T>();
         return (List<T>)element.getObjectValue();
    }
    
    public void flush() {
    	getCache().flush();
    }
    
    public void flushNow() {
    	synchronized (getCacheName()) {
    		CacheFactory.flush(getCacheName());
    	}
    }
    
    public int getSize(){
        return getCache().getSize();
    }
    
	public List getKeys(){
    	return getCache().getKeys();
    }
    
    public void remove(Object key) {
    	synchronized (getCacheName()) {
    		getCache().remove(key);
    	}
	}
    
    public boolean isLoaded() {
    	return getSize() > 0 ;
    }
    
    public Cache getCache() {
    	return CacheFactory.getCache(getCacheName());
    }
    
	public boolean containsKey(Object key) {
		if (getCache().get(key) == null) {
			return false;
		} else {
			return true;
		}
	}
}
