/**
 * 
 */
package com.honda.galc.data.memorymap;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Feb 22, 2013
 */
public class MemoryMapCache {
		
	private static PlcMemoryMapItemDao _plcMemoryMapItemDao;
	private static volatile ConcurrentHashMap<String, ConcurrentHashMap<String, PlcMemoryMapItem>> _cache = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlcMemoryMapItem>>();

	public static ConcurrentHashMap<String, PlcMemoryMapItem> getMap(String applicationId) {
		if (!getCache().containsKey(applicationId)) {
			addNewMap(applicationId);
		}
		return getCache().get(applicationId);
	}
	
	/**
	 * creates a map of memory locations to read/write from for the provided application
	 */
	public static void addNewMap(String applicationId) {
		ConcurrentHashMap<String, PlcMemoryMapItem> memoryMapItems = new ConcurrentHashMap<String, PlcMemoryMapItem>();
		if (!getCache().containsKey(applicationId)) {
			for(PlcMemoryMapItem item: getPlcMemoryMapItemDao().findAllByPrefix(applicationId)) {
				String metricId = StringUtils.trimToEmpty(item.getMetricId());
				if (!memoryMapItems.containsKey(metricId)) {
					memoryMapItems.put(metricId, item);
				}
			}
			getCache().put(applicationId, memoryMapItems);
		}
	}

	public static PlcMemoryMapItemDao getPlcMemoryMapItemDao() {
		if(_plcMemoryMapItemDao == null)
			_plcMemoryMapItemDao = ServiceFactory.getDao(PlcMemoryMapItemDao.class);
		return _plcMemoryMapItemDao;
	}
	
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, PlcMemoryMapItem>> getCache() {
		return _cache;
	}
}
