package com.honda.galc.common.logging;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 14, 2017
 */
public class RuntimeLogConfigurator {

	public static final String Log4J2_PROPERTY_PREFIX		= "log4j2";
	public static final String DEFAULT_CLIENT_COMPONENT_ID	= "CLIENT_LOGGER";
	public static final String DEFAULT_SERVER_COMPONENT_ID 	= "SERVER_LOGGER";
	
	public static Properties loadServerProperties(String filePath) {
		Properties properties = loadConfigFromResource(filePath);
		updateProperties(properties, DEFAULT_SERVER_COMPONENT_ID);		// override with default configuration from db	
		printConfiguration(properties);
		return properties;	
	}
	
	public static Properties loadClientProperties(String applicationId, String filePath) {
		Properties properties = loadConfigFromResource(filePath);
		updateProperties(properties, DEFAULT_CLIENT_COMPONENT_ID);		// override with default configuration from db	
		updateProperties(properties, applicationId);					// override with application specific configuration from db
		printConfiguration(properties);
		return properties;
	}

	private static void updateProperties(Properties runtimeLog4jProperties, String componentId) {
		List<ComponentProperty> properties = getComponentPropertyDao().findAllByComponentId(componentId);
		int length = Log4J2_PROPERTY_PREFIX.length() + 1;
		for(ComponentProperty property: properties) {
			if (property.getPropertyKey().startsWith(Log4J2_PROPERTY_PREFIX) && property.getPropertyKey().length() > length) {
				runtimeLog4jProperties.put(property.getPropertyKey().substring(length), property.getPropertyValue());
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void printConfiguration(Properties properties) {
		TreeMap<String, String> propertiesMap = new TreeMap(properties); 	// using TreeMap to get a sorted list
		System.out.println("Using following log4j properties..");
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
	}

	public static Properties loadConfigFromResource(String filePath) {
		ClassLoader cl = RuntimeLogConfigurator.class.getClassLoader();
		Properties log4jProperties = new Properties();
		try {
			InputStream ins = cl.getResourceAsStream(filePath);
			log4jProperties.load(ins);
		} catch (Exception ex) {
			System.out.println("Unable to load " + filePath + ". Will look for runtime log4j configuration.");
		}
		return log4jProperties;
	}
	
	private static ComponentPropertyDao getComponentPropertyDao() {
		return ServiceFactory.getDao(ComponentPropertyDao.class);
	}
}
