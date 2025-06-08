package com.honda.galc.rest.common;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.rest.json.JsonContentHandler;
import com.honda.galc.rest.xml.XmlContentHandler;

/**
 * @author Subu Kathiresan
 * @date Feb 20, 2015
 */
public enum ContentType {

	XML(XmlContentHandler.class),
	JSON(JsonContentHandler.class);

	private Class<? extends IContentHandler> handler; 
	
	private ContentType(Class<? extends IContentHandler> handler){
		this.handler = handler;
	}

	public Class<? extends IContentHandler> getHandler() {
		return handler;
	}
	
	public static boolean isValid(String eventName) {
		try {
			if (ContentType.valueOf(eventName) != null) {
				return true;
			}
		} catch (Exception ex) {}
		return false;
	}

	public static ContentType get(String name) {
		try {
			name = name.replace("application/", "");
			name = name.replace("text/", "");
			return ContentType.valueOf(StringUtils.trim(name).toUpperCase());
		} catch (Exception ex) {
			return null;
		}
	}
}
