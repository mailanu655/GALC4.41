package com.honda.galc.test.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemPropertyManager {
    
    private static Properties properties = null;
    private static String PROPERTY_FILE_NAME = "/system.properties";
    
    public static String getProperty(String name) {
        
        return getProperties().getProperty(name);
    }
    
    private static void loadProperties() {
        
        properties = new Properties();
        InputStream inStream1 = SystemPropertyManager.class.getResourceAsStream(PROPERTY_FILE_NAME);
        try {
            properties.load(inStream1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static Properties getProperties() {
        
        if(properties == null) loadProperties();
        
        return properties;
    }
}
