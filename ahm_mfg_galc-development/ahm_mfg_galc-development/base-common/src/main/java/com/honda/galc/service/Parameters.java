package com.honda.galc.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Parameters implements Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();

    public Parameters() {
    	
    }
    
	private Parameters(String key, Object value) {
        parameters.put(key, value);
    }

    public static Parameters with(String key, Object value) {
        return new Parameters(key, value);
    }

    public Parameters put(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * This method can be used for conditional use of parameters and parameter will be used only if :
     * <ol>
     * <li> Value is not null
     * <li> If value is a String type then value must not be blank
     * <li> If value is a Collection type then collection must not be empty
     *</ol>
     * @param key
     * @param value
     */
	public void putIfNotEmpty(String key, Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof String) {
			if (StringUtils.isBlank((String) value)) {
				return;
			}
		} else if (value instanceof Collection) {
			if (((Collection<?>) value).isEmpty()) {
				return;
			}
		}
		put(key, value);
	}

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public int size() {
        return parameters.size();
    }
    
    public Parameters putAll(Parameters params) {
        parameters.putAll(params.getParameters());
        return this;
    } 
    
    public String toString(){
    	return parameters.toString();
    }
}
