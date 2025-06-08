package com.honda.galc.datacollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>LotControlRuleCache</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRuleCache description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jun 2, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 2, 2011
 */

public class LotControlRuleCache extends PersistentCache{
	public static LotControlRuleCache instance;
	
	private static Set<String> keys = new HashSet<String>();

	private LotControlRuleCache() {
		super();
	}

	public LotControlRuleCache(String cacheName) {
		super(cacheName);
	}

	public static LotControlRuleCache getInstance() {

		if(instance == null ) instance = new LotControlRuleCache();
		return instance;
	}
	
	
	public static List<LotControlRule> getLotControlRule(ProductSpec specCode, String processPointId) {
	
		try {
			return getOrLoadLotControlRule(specCode, processPointId);
		} catch (Throwable e) {
			Logger.getLogger().error(e, "Exception to get Lot Control Rule for " + specCode);
		}
		return null;
			
	}
	
	@SuppressWarnings("unchecked")
	public static List<LotControlRule> getOrLoadLotControlRule(BaseProductSpec specCode, String processPointId) {
		String key = getKey(specCode, processPointId);
		if (containsValidKey(key))
			return getInstance().get(key, List.class);
		else {
			return loadLotControlRule(specCode, processPointId);
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean containsValidKey(String key) {
		if(keys.contains(key)){
			try {
				List list = getInstance().get(key, List.class);
				if(list == null) removeFromCache(key);
			} catch (Exception e) {
				removeFromCache(key);
			}
		}
		return keys.contains(key);
	}

	private synchronized static void removeFromCache(String key) {
		keys.remove(key);
		getInstance().remove(key);
	}

	public static String getKey(BaseProductSpec specCode, String processPointId) {
		return (specCode != null) ? (processPointId + Delimiter.UNDERSCORE + specCode.getProductSpecCode()) : processPointId;
	}

	private static List<LotControlRule> loadLotControlRule(BaseProductSpec specCode, String processPointId) {
		if(specCode == null) return null;
		if(ServiceFactory.isServerAvailable()){
			LotControlRuleDao dao = ServiceFactory.getDao(LotControlRuleDao.class);
			//load rules only if product spec changed
			List<LotControlRule> allRules = dao.findAllByProcessPoint(processPointId);
			List<LotControlRule> rules = LotControlPartUtil.getLotControlRuleByProductSpec(specCode, allRules);
			if(!rules.isEmpty()){
				String key = getKey(specCode, processPointId);
				addToCache(key, rules);

				Logger.getLogger(PropertyService.getLoggerName(processPointId)).info("Lot Control Rules:", rules.toString(),
						" was loaded for processPoint:", processPointId);
			}
			return rules;
		}

		Logger.getLogger().warn("Service is not available.");

		return null;

	}
	
	public synchronized static void addToCache( String key, List<LotControlRule> rules) {
		keys.add(key);
		getInstance().put(key, rules);
	}
	
	public synchronized static void removeLotCtrolRules(String processPointId) {
		for(Iterator<String> iterator = keys.iterator(); iterator.hasNext();){
			String key = iterator.next();
			if(key.startsWith(processPointId))
				iterator.remove();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<LotControlRule> getLotControlRules(String processPointId) {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		for(String key : keys) {
			if(key.startsWith(processPointId)) rules.addAll(getInstance().get(key, List.class));
		}
		return rules;
	}
	
	public synchronized static void refreshLotCtrolRules(ProductSpec specCode, String processPointId){
		String key = getKey(specCode, processPointId);
		for(Iterator<String> iterator = keys.iterator(); iterator.hasNext();){
			if(key.equals(iterator.next()))
				iterator.remove();
		}
		
		loadLotControlRule(specCode, processPointId);
		
	}

	public static List<LotControlRule> getLotControlRule(ProductSpec productSpec, 
			String processPointId, Logger logger) {
		
		List<LotControlRule> ruleList = getLotControlRule(productSpec, processPointId);
		
		if(logger != null)
			logger.info("Lot Control Rule for Product Spec code:", productSpec.getProductSpecCode(),
					" was loaded to cache:", ruleList.toString());

		return ruleList;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getProcessPoints() {
		List<String> keys = getInstance().getKeys();
		Set<String> processPoints = new HashSet<String>();
		for(String key : keys) {
			int index = key.indexOf(Delimiter.UNDERSCORE);
			if(index >=0 && index + 1 <key.length()) processPoints.add(key.substring(0,index));
			else processPoints.add(key);
		}
		return new SortedArrayList<String>(processPoints);
	}
	

}
