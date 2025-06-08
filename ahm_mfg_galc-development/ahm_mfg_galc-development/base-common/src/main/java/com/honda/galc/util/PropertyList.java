package com.honda.galc.util;

import java.util.Properties;

public class PropertyList {
    protected Properties properties;
    
    
    public PropertyList(){
        
    }
    
    public PropertyList(String propertyPath) {
    	try{
    		properties = PropertyLoader.loadProperties(propertyPath);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
    
    public PropertyList(Properties properties) {
        this.properties = properties;
    }
    
    public String getProperty(String propertyName) {
        if(properties == null) return null;
        return properties.getProperty(propertyName);
    }
    
    public String getProperty(String propertyName,String defaultString) {
        String str = getProperty(propertyName);
        return (str!=null)? str:defaultString;
    }

    public Integer getPropertyInt(String propertyName) {
        String propValue = getProperty(propertyName);
        if(propValue == null) return null;
        Integer num = null;
        try{
             num = Integer.parseInt(propValue);
        }catch(NumberFormatException e) {
            
        }
        return num;
    }
    
    public Integer getPropertyInt(String propertyName, int defaultValue) {
        Integer value = this.getPropertyInt(propertyName);
        return value != null?value:defaultValue;
    }
    
    public boolean getPropertyBoolean(String propertyName,boolean defaultValue) {
        String value = this.getProperty(propertyName);
        if(value == null) return defaultValue;
        return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"));
    }
}
