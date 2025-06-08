package com.honda.galc.common.logging;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.service.ServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class RuntimeLogConfiguratorTest {
	private static final String TEST_LOG4J2_CONFIG_FILE = "log4j2dev.properties";
	private static final String TEST_APPLICATION_ID = "TEST_APPLICATION_ID";
	private static final String TEST_RESOURCES_PATH = "src/test/resources/";
	private List<ComponentProperty> componentProperties;
	private Properties properties;
	
	@Mock
	public static ComponentPropertyDao propertyDaoMock = PowerMockito.mock(ComponentPropertyDao.class);
	
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.when(ServiceFactory.getDao(ComponentPropertyDao.class)).thenReturn(propertyDaoMock);
		properties = readProperties();
	}
	
	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Load configuration from resource file.
	 */
	@Test
	public void loadConfigurationFromFile() {
		Properties properties = RuntimeLogConfigurator.loadConfigFromResource(TEST_LOG4J2_CONFIG_FILE);
		assertTrue(!properties.isEmpty());
	}
	
	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Load and parse client properties.
	 */
	@Test
	public void loadClientProperties() {
		componentProperties = new ArrayList<ComponentProperty>();
		PowerMockito.when(propertyDaoMock.findAllByComponentId(RuntimeLogConfigurator.DEFAULT_CLIENT_COMPONENT_ID)).thenReturn(componentProperties);
		PowerMockito.when(propertyDaoMock.findAllByComponentId(TEST_APPLICATION_ID)).thenReturn(componentProperties);
		Properties loaded = RuntimeLogConfigurator.loadClientProperties(TEST_APPLICATION_ID, TEST_LOG4J2_CONFIG_FILE);
		assertTrue(loaded != null && verifyProperties(loaded));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Load and parse server properties.
	 */
	@Test
	public void loadServerProperties() {
		componentProperties = new ArrayList<ComponentProperty>();
		PowerMockito.when(propertyDaoMock.findAllByComponentId(RuntimeLogConfigurator.DEFAULT_SERVER_COMPONENT_ID)).thenReturn(componentProperties);
		Properties loaded = RuntimeLogConfigurator.loadClientProperties(TEST_APPLICATION_ID, TEST_LOG4J2_CONFIG_FILE);
		assertTrue(loaded != null && verifyProperties(loaded));
	}
	
	private boolean verifyProperties(Properties loadedProperties) {
		boolean result = true;
		if(properties.size() != loadedProperties.size()) {
			result = false;
		} else {
			for(Object key : properties.keySet()) {
				if(!loadedProperties.containsKey(key) || !loadedProperties.containsValue(properties.get(key))) {
					result = false;
				}
			}
		}
		return result;
	}

	private Properties readProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		FileReader reader = new FileReader(TEST_RESOURCES_PATH + TEST_LOG4J2_CONFIG_FILE);
		properties.load(reader);
		reader.close();
		return properties;
	}
}
