package com.honda.galc.util;

import java.net.InetAddress;
import java.util.Properties;

import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationBuilder;

/**
 * 
 * <h3>AbstractPropertyConfig Class description</h3>
 * <p> AbstractPropertyConfig description </p>
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
public abstract class AbstractPropertyConfig extends PropertyList{
	
	
	/**
     * This constant is the name of the System property that is used as the "preferred suffix"
     * for retrieving the component-list and PropertyServices properties.
     */
    public String SYSTEM_PROPERTY_INIT_PREFERRED_SUFFIX = 
    	"com.honda.global.galc.system.properties.PropertyServices.InitialPreferredSuffix";

	
	
	protected String prefferedSuffix;
    
	public AbstractPropertyConfig(String configResourcePath) {
        super(configResourcePath);
        prefferedSuffix = getPreferedSuffix();
    }
	
	public AbstractPropertyConfig(String configResourcePath, String initPreferredSuffix) {
        super(configResourcePath);
        SYSTEM_PROPERTY_INIT_PREFERRED_SUFFIX = initPreferredSuffix;
        prefferedSuffix = getPreferedSuffix();
    }
	
	
	protected String getPreferedSuffix() {
		String tempPreferred = null;
		tempPreferred = System.getProperty(SYSTEM_PROPERTY_INIT_PREFERRED_SUFFIX);
		
		if(tempPreferred != null){
			log("Preferred suffix from system property is "  + tempPreferred);
			return tempPreferred;
		}
		
		try{
			tempPreferred = InetAddress.getLocalHost().getHostName();
    		
    		// In some configuration, the host name can be fully qualified
    		if (tempPreferred != null && tempPreferred.indexOf('.') > 0) {
    			int dotIndex = tempPreferred.indexOf('.');
    			tempPreferred = tempPreferred.substring(0, dotIndex);
    		}
    		log("Preferred suffix from host name is "  + tempPreferred);
    		
		}catch(Exception e){
			log("Property Services: Unable to determine Host Name. "+e.getMessage());
		}
		return tempPreferred;
        
	}
	
	public String getProperty(String propertyId) {
		
		String property = super.getProperty(propertyId + "." + this.prefferedSuffix);
		if(property != null) return property;
		
		property = super.getProperty(propertyId + "." + "DEFAULT");
		if(property != null) return property;
		
		property = super.getProperty(propertyId);
		
		return property;
		
	}
	
	public static PropertiesConfiguration getPropertiesConfiguration(Properties properties) {
		PropertiesConfigurationBuilder builder = new PropertiesConfigurationBuilder();
		builder.setRootProperties(properties);
		return builder.build();
	}

	protected void log(String message) {
		System.out.println(message);
	}
	
	
}
