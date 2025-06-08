package com.honda.galc.util;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServerCacheService;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>ServerInfoUtil Class description</h3>
 * <p> ServerInfoUtil description </p>
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
 * Nov 13, 2012
 *
 *
 */
public class ServerInfoUtil {
	public static final String REFRESH_PROPERTIES_URLS_COMP_ID = "PROPERTY_REFRESH_URLS";
	public static String SERVICE_HANDLER = "BaseWeb/HttpServiceHandler";
	
	public static List<String> getServerUrls(){
		List<String> urls = new ArrayList<String>();
		
		String cell = ServiceFactory.getService(DataCollectionService.class).getShortServerName();
    	if(StringUtils.isEmpty(cell)) return urls;

    	String servers = PropertyService.getProperty(REFRESH_PROPERTIES_URLS_COMP_ID, cell);
    	if(StringUtils.isEmpty(servers)) return urls;

    	String[] elems = servers.split("\\s*,\\s*");
    	if(elems == null || elems.length <=0) return urls;

    	for(String item : elems) {
    		String url = PropertyService.getProperty(REFRESH_PROPERTIES_URLS_COMP_ID,item);
    		urls.add(url + SERVICE_HANDLER);
    	}
    	
    	return urls;
	}

	public static Map<String,List<ComponentProperty>> getPropertyFromActiveServerCache(String componentId) {

		String currentUrl = HttpServiceProvider.url;
		Map<String,List<ComponentProperty>> propertyMap = new LinkedHashMap<String, List<ComponentProperty>>();
		
		try{
			Map<String,String> serverURLs  = PropertyService.getServerUrls();
			for(Entry<String, String> entry : serverURLs.entrySet()) {
				String url = entry.getValue();
				if(!StringUtils.endsWith(url, "/")) url += "/";
				List<ComponentProperty> items = getServerCacheService(url + SERVICE_HANDLER).getProperty(componentId);
				propertyMap.put(entry.getKey(), items);
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			HttpServiceProvider.setUrl(currentUrl);
		}
		return propertyMap;
		
	}
	
	public static Map<String,List<LotControlRule>> getLotControlFromActiveServerCache(String processPoint) {
		String currentUrl = HttpServiceProvider.url;
		Map<String,List<LotControlRule>> ruleMap = new LinkedHashMap<String, List<LotControlRule>>();
		
		try{
			Map<String,String> serverURLs  = PropertyService.getServerUrls();
			for(Entry<String, String> entry : serverURLs.entrySet()) {
				String url = entry.getValue();
				if(!StringUtils.endsWith(url, "/")) url += "/";
				List<LotControlRule> items = getServerCacheService(url + SERVICE_HANDLER).getLotControlRule(processPoint);
				ruleMap.put(entry.getKey(), items);
			}
		}catch(Exception e) {
			
		}finally {
			HttpServiceProvider.setUrl(currentUrl);
		}
		return ruleMap;

	}
	
	public static Map<String,Exception> refreshProperty(String componentId) {
		String currentUrl = HttpServiceProvider.url;
		Map<String,Exception> messages = new LinkedHashMap<String,Exception>();
		String url = "";
		try{
			Map<String,String> serverURLs  = PropertyService.getServerUrls();
			
			if(serverURLs != null && !StringUtils.isEmpty(componentId)){
				for(Entry<String, String> entry : serverURLs.entrySet()) {
					url = entry.getValue();
					if(!StringUtils.endsWith(url, "/")) url += "/";
					try{
						getServerCacheService(url + SERVICE_HANDLER).refreshProperty(componentId);
						messages.put(entry.getKey(), null);
					}catch(Exception e){
						messages.put(entry.getKey(), e);
					}
				}
			}
		}catch(Exception e) {
			messages.put("*", e);
		}finally {
			HttpServiceProvider.setUrl(currentUrl);
		}
		return messages;
	}
	
	public static Map<String,Exception> refreshLotControlRule(String processPointId) {
		String currentUrl = HttpServiceProvider.url;
		Map<String,Exception> messages = new LinkedHashMap<String,Exception>();
		String url = "";
		try{
			Map<String,String> serverURLs  = PropertyService.getServerUrls();
			if(serverURLs != null && !StringUtils.isEmpty(processPointId)){
				for(Entry<String, String> entry : serverURLs.entrySet()) {
					url = entry.getValue();
					if(!StringUtils.endsWith(url, "/")) url += "/";
					try{
						getServerCacheService(url + SERVICE_HANDLER).refreshLotControlRule(processPointId);
						messages.put(entry.getKey(), null);
					}catch(Exception e){
						messages.put(entry.getKey(), e);
					}
				}
			}
		}catch(Exception e) {
			messages.put("*", e);
		}finally {
			HttpServiceProvider.setUrl(currentUrl);
		}
		return messages;
	}
	
	private static ServerCacheService getServerCacheService(String url) {
		HttpServiceProvider.setUrl(url);
		return new HttpServiceProvider().getService(ServerCacheService.class);
	}
	
}
