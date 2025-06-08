package com.honda.galc.client.logging;

import java.util.Properties;

import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;

import com.honda.galc.common.logging.RuntimeLogConfigurator;
import com.honda.galc.property.LoggerConfigPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AbstractPropertyConfig;
/**
 * 
 * <h3>LoggerConfig Class description</h3>
 * <p> LoggerConfig description </p>
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
 * May 11, 2010
 *
 */
public class ClientSideLoggerConfig extends AbstractPropertyConfig{

	private static final String CONFIG_RESOURCE_PATH="loggerConfig.properties";
	
	private static final String LOG4J2_PROPERTY_FILE_PATH = "LOG4J2_PROPERTY_FILE_PATH";
	
    
	public ClientSideLoggerConfig() {
		super(CONFIG_RESOURCE_PATH);
	}
	
	public String getLog4jPropertyFilePath() {
		return getProperty(LOG4J2_PROPERTY_FILE_PATH);
	}
	
	
	public static void configLog4j(String applicationId) {
		ClientSideLoggerConfig config = new ClientSideLoggerConfig();
		String filePath = config.getLog4jPropertyFilePath();
		System.out.println("log4j2 configuration file path : " + filePath);
		Properties log4jProperties = RuntimeLogConfigurator.loadClientProperties(applicationId, filePath);

	
		PropertiesConfiguration configuration = getPropertiesConfiguration(log4jProperties);
        org.apache.logging.log4j.core.LoggerContext.getContext(false).start(configuration);
	}
	
	protected String getPreferedSuffix() {
		LoggerConfigPropertyBean loggerConfigProperty = PropertyService.getPropertyBean(LoggerConfigPropertyBean.class,"LOGGER");
		return loggerConfigProperty.getClientPreferredSuffix();
	}
}
