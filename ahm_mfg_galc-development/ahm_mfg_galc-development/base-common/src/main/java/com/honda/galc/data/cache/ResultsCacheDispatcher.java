package com.honda.galc.data.cache;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import net.sf.ehcache.Element;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Aug 21, 2014
 */
public class ResultsCacheDispatcher {

	public static final int DISPATCH_INTERVAL = 2000;
	private static ResultsCacheDispatcher resultsCacheDispatcher = null;
	private static Random random = new Random();
	private static int RANDOM_MIN = 100;
	private static int RANDOM_MAX = 999;
	
	private ResultsCache resultsCache;
	private Logger logger = null;

	private ResultsCacheDispatcher() {
		logger = Logger.getLogger(getLoggerName());
		startDispatcher();
	}

	public static ResultsCacheDispatcher getInstance() {
		if (resultsCacheDispatcher == null)
			resultsCacheDispatcher = new ResultsCacheDispatcher();
		
		return resultsCacheDispatcher; 
	}
	
	public static void init() {
		getInstance();
	}
	
	/**
	 * adds a ResultsCacheItem to the local disk cache
	 * @param resultsCacheItem
	 */
	public void addToCache(ResultsCacheItem resultsCacheItem) {
		String key = Long.toString(new Date().getTime()) + (random.nextInt(RANDOM_MAX - RANDOM_MIN + 1) + RANDOM_MIN);
		logger.info("Adding item to Results Cache: " + key + " - " + resultsCacheItem.getServiceName());
		getResultsCache().put(key, resultsCacheItem);
	}

	/**
	 * dispatches request to the server for the cached Result
	 * 
	 * @param cacheKey
	 * @param cacheItem
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean dispatch(ResultsCacheItem cacheItem) {
		try {
			// get dao service
			String serviceName = cacheItem.getServiceName();
			Class serviceClass = Class.forName(serviceName);
			Method method = ReflectionUtils.getMethod(ServiceFactory.getDao(serviceClass), cacheItem.getMethodName(), cacheItem.getParameters());
			
			// invoke method
			if (method != null) {
				ReflectionUtils.invoke(ServiceFactory.getDao(serviceClass), method, cacheItem.getParameters());
			} else {
				logger.warn(ReflectionUtils.getMethodSignature(serviceName, cacheItem.getMethodName(), cacheItem.getParameters()) + " does not exist");
				return false;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * removes element from cache
	 * 
	 * @param cacheKey
	 */
	private boolean removeFromCache(Object cacheKey) {
		try {
			Element element = getResultsCache().get(cacheKey);
			if (element != null) {
				getResultsCache().remove(cacheKey);
				element.setEternal(false);
				element.setTimeToLive(0);
				element.setTimeToIdle(0);
				logger.debug("ResultsCacheItem " + cacheKey + " expires at: " + new Date(element.getExpirationTime()));
			}
		} catch(Exception ex) {
			logger.debug("Failed to remove ResultsCacheItem " + cacheKey + " " + cacheKey.toString());
			return false;
		}
		return true;
	}

	/**
	 * starts the daemon process that clears the cache entries periodically
	 */
	private void startDispatcher() {
		Runnable processResultsCacheThread = new Runnable() {
			public void run() {
				while (true) {
					try {
						getResultsCache().evictExpiredElements();
						getResultsCache().flush();
						logger.debug("DiskStore size: " + getResultsCache().getDiskStoreSize());
						processCachedItems();
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.error("Failed to process cached items: " + (ex.getMessage() == null ? "" : ex.getMessage()));
					}
					sleep();
				}
			}
		};

		Thread worker = new Thread(processResultsCacheThread);
		worker.start();
	}

	@SuppressWarnings("unchecked")
	private void processCachedItems() {
		// retrieve all items in cache that is yet to expire.
		// sort them by least recent to most recent.
		// process sequentially from least recent to most recent.
		SortedArrayList<String> cacheKeys = new SortedArrayList<String>((Collection<? extends String>) getResultsCache().getValidKeys());
		for (Object cacheKey : cacheKeys) {
			Element element = getResultsCache().get(cacheKey);
			logger.info("Processing Element: " + ((ResultsCacheItem) element.getObjectValue()).serviceName);
			if (element != null) {
				if (dispatch((ResultsCacheItem) element.getObjectValue())) {
					// remove element from cache if dispatch was successful 
					removeFromCache(cacheKey);
				}
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(DISPATCH_INTERVAL);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public ResultsCache getResultsCache() {
		if (resultsCache == null)
			resultsCache = new ResultsCache();

		return resultsCache;
	}
	
	public String getLoggerName() {
		return this.getClass().getSimpleName();
	}
}