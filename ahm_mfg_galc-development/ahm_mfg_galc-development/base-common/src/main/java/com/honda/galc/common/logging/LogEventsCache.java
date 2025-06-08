package com.honda.galc.common.logging;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.PersistentCache;

/**
 * @author Subu Kathiresan
 * @Date Jul 12, 2012
 *
 */
public class LogEventsCache extends PersistentCache {
		
	public LogEventsCache(){
        super("LogEventsCache");
        
        if (StringUtils.isEmpty(CacheFactory.getPath()))
        	CacheFactory.setPath(System.getProperty("user.dir") + System.getProperty("file.separator") + "ehcache");
        
        Cache cache = new Cache(getCacheName(),
        		10,								// max elements in memory
        		MemoryStoreEvictionPolicy.FIFO, // memory store eviction policy
        		true, 							// overflowToDisk
        		CacheFactory.getPath(),			// disk store path
        		false,							// eternal?
        		100000,							// time to live in seconds
        		100000,							// time to idle in seconds
        		true, 							// persist the cache to disk between JVM restarts
        		120,							// disk expiry thread interval in seconds
        		null,							// registered event listeners
        		null,							// bootstrap cache loader
        		10000,							// max elements on disk
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
    
    /**
     * returns a list of keys that have not yet expired
     */
    @SuppressWarnings({ "rawtypes" })
	public List getValidKeys() {
    	return CacheFactory.getCache(cacheName).getKeysWithExpiryCheck();
    }
}
