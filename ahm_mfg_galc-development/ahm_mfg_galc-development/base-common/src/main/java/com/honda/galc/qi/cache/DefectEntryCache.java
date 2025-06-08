package com.honda.galc.qi.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.PersistentCache;
/**
 * <h3>DefectEntryCache description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectEntryCache</code> is cache class for Defect Entry Screen to maintain cache
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>17/7/2017</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class DefectEntryCache extends PersistentCache {
	
	public DefectEntryCache(String subName, int maxElementsInMemory, int maxElementsOnDisk, int timeToLive, int timeToIdle) {
		super("DefectEntryCache "+subName);
		
        if (StringUtils.isEmpty(CacheFactory.getPath()))
        	CacheFactory.setPath(System.getProperty("user.dir") + System.getProperty("file.separator") + "ehcache");

        Cache cache = new Cache(getCacheName(),
        		maxElementsInMemory,			// max elements in memory
        		MemoryStoreEvictionPolicy.LRU,  // memory store eviction policy
        		true, 							// overflowToDisk
        		CacheFactory.getPath(),			// disk store path
        		false,							// eternal
        		timeToLive,						// time to live in seconds
        		timeToIdle,						// time to idle in seconds
        		false, 							// persist the cache to disk between JVM restarts
        		120,							// disk expiry thread interval in seconds
        		null,							// registered event listeners
        		null,							// bootstrap cache loader
        		maxElementsOnDisk,				// max elements on disk
        		10,								// disk spool buffer size in Mb
        		true);							// clear on flush
        
       	CacheFactory.addCache(cache);
       	
	}
	
    protected String getCacheName() {
        return (cacheName != null) ? cacheName : getClass().getSimpleName();
    }
    
    public void evictExpiredElements() {
    	getCache().evictExpiredElements();
    }
 
    public Element get(Object key) {
    	return getCache().get(key);
    }
    
	public Cache getCache() {
    	return CacheFactory.getCache(cacheName);
    }
	
	public boolean containsKey(Object key) {
		Element element = get(key);
		if(element != null) {
			return true;
		}
		return false;
	}
	
	public void clearCache() {
		getCache().removeAll();
	}
}
