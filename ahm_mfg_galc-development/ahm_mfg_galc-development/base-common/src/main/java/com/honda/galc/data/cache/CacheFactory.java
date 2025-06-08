package com.honda.galc.data.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>CacheFactory Class description</h3>
 * <p> CacheFactory description </p>
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
 * Jun 10, 2010
 *
 *
 */
public class CacheFactory {
    
    private static CacheManager cacheManager;
    private static String diskStorePath ="";
    
    public static Cache getCache(String cacheName) {
        if(getCacheManager() == null) 
        	return null;
        if(cacheManager.getCache(cacheName) == null) 
        	addCache(cacheName,!StringUtils.isEmpty(diskStorePath));
        
        return cacheManager.getCache(cacheName);
    }
    
    public static void setPath(String path){
        diskStorePath = path;
    }
    
    public static String getPath() {
    	return diskStorePath;
    }
    
    private static CacheManager getCacheManager() {
        if(cacheManager == null) createCacheManager();
        return cacheManager;
    }

    private static void createCacheManager() {
        cacheManager = new CacheManager(createConfiguration());
    }

    private static void addCache(String cacheName, boolean diskPersistent) {
        Cache cache = new Cache(cacheName,
        		10000,
        		MemoryStoreEvictionPolicy.LRU,
        		diskPersistent, // overflowToDisk
        		diskStorePath,
        		true,
        		0,
        		0,
        		diskPersistent, // diskPersistent
        		1,
        		null,
        		null,
        		0,
        		0,
        		false);
        cacheManager.addCache(cache);
    }
    
    public static void addCache(Cache cache) {
        if(getCacheManager().getCache(cache.getName()) == null) 
        	getCacheManager().addCache(cache);
    }
    
    private static Configuration createConfiguration() {
        
        Configuration configuration = new Configuration();
        CacheConfiguration defaultCacheConfiguration = new CacheConfiguration();
        defaultCacheConfiguration.setEternal(false);
        defaultCacheConfiguration.setName("defaultCache");
        configuration.addDefaultCache(defaultCacheConfiguration);
    	configuration.setUpdateCheck(false);
 
        if(!StringUtils.isEmpty(diskStorePath)) {
        	DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration();
        	diskStoreConfiguration.setPath(diskStorePath);
        	configuration.addDiskStore(diskStoreConfiguration);
        }
        return configuration; 

    }
    /**
     * mimick flushing entries into permanent disk
     * @param cacheName
     */
    public static void flush(String cacheName){
        cacheManager.removeCache(cacheName);
        addCache(cacheName,!StringUtils.isEmpty(diskStorePath));
    }
    
}
