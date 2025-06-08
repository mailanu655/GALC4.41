package com.honda.galc.common.logging;

import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.PersistentCache;

public class TrainingDataCache extends PersistentCache {
	
	private static TrainingDataCache instance;
	
	public static  TrainingDataCache getInstance(){
		if (instance == null) {
			instance = new TrainingDataCache();
			
		}
		return instance;
	}
	
	public TrainingDataCache() {
		super("UserTrainingCache");
		
        if (StringUtils.isEmpty(CacheFactory.getPath()))
        	CacheFactory.setPath(System.getProperty("user.dir") + System.getProperty("file.separator") + "ehcache");

        Cache cache = new Cache(getCacheName(),
        		10000,								// max elements in memory
        		MemoryStoreEvictionPolicy.FIFO, // memory store eviction policy
        		true, 							// overflowToDisk
        		CacheFactory.getPath(),			// disk store path
        		false,							// eternal?
        		21600,							// time to live in seconds
        		21600,							// time to idle in seconds
        		true, 							// persist the cache to disk between JVM restarts
        		120,							// disk expiry thread interval in seconds
        		null,							// registered event listeners
        		null,							// bootstrap cache loader
        		0,							// max elements on disk
        		0,								// disk spool buffer size in Mb
        		false);							// clear on flush
        
       	CacheFactory.addCache(cache);
       	
	}
	
	 protected String getCacheName() {
	        return (cacheName != null) ? cacheName : getClass().getSimpleName();
	    }

	    public int getDiskStoreSize() {
	    	return getCache().getDiskStoreSize();
	    }
	    
	    public void evictExpiredElements() {
	    	getCache().evictExpiredElements();
	    }
	 
	    public Element get(Object key) {
	    	return getCache().get(key);
	    }
	    
	    public void put(Object item) {
			put(Long.toString(new Date().getTime()), item);
	    }
	    
	    /**
	     * returns a list of keys that have not yet expired
	     */
		public List<?> getValidKeys() {
	    	return CacheFactory.getCache(cacheName).getKeysWithExpiryCheck();
	    }

}
