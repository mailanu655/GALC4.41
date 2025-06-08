package com.honda.galc.data.cache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.StringUtils;


/**
 * @author Suriya Sena
 * @date Jan 16 2014
 */
public final class OperationInfoCache extends PersistentCache {
	
	
	
	private static OperationInfoCache instance;
	
	public static  OperationInfoCache getInstance(){
		if (instance == null) {
			instance = new OperationInfoCache();
		}
		return instance;
	}
	
	private OperationInfoCache(){
        super("OperationInfoCache");
        
        if (StringUtils.isEmpty(CacheFactory.getPath()))
        	CacheFactory.setPath(System.getProperty("user.dir") + System.getProperty("file.separator") + "ehcache");
        
        Cache cache = new Cache(getCacheName(),
        		8,								// max elements in memory
        		MemoryStoreEvictionPolicy.FIFO, // memory store eviction policy
        		true, 							// overflowToDisk
        		CacheFactory.getPath(),			// disk store path
        		false,							// eternal?
        		36000,							// time to live in seconds
        		36000,							// time to idle in seconds
        		false, 							// persist the cache to disk between JVM restarts
        		120,							// disk expiry thread interval in seconds
        		null,							// registered event listeners
        		null,							// bootstrap cache loader
        		500,							// max elements on disk
        		10,								// disk spool buffer size in Mb
        		false);							// clear on flush
        
        CacheFactory.addCache(cache);
    }
	
    protected String getCacheName() {
        return (cacheName != null) ? cacheName : getClass().getSimpleName();
    }
    
    public Element get(int maintenanceId) {
        return get(maintenanceId, "-1", "-1");
     }
    
    
    public Element get(int maintenanceId, String processPointId, String productId) {
       String key = computeHashKey(maintenanceId, processPointId, productId);
	   return getCache().get(key);
    }
    
    public void put(int maintenanceId, Object value) {
    	put(maintenanceId, "-1","-1", value);
    }


    public void put(int maintenanceId, String processPointId, String productId, Object value ) {
    	String key =  computeHashKey(maintenanceId, processPointId, productId);
	    put(key,value);
    }
    
    private String computeHashKey(int maintenanceId, String processPointId, String productId) {
		String buf = String.format("%d,%s,%s",maintenanceId,processPointId, processPointId);
		return buf;
    }
}