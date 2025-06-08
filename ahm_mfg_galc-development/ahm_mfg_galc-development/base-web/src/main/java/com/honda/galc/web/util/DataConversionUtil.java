package com.honda.galc.web.util;

import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author Subu Kathiresan
 * @date Jul 30, 2015
 */
public class DataConversionUtil {
	
	private static Gson gson = null;

	public static String getJson(Object obj) {
		return getGson().toJson(obj);
	}
	
	public static String getXml(Object obj) {
		StringWriter stringWriter = new StringWriter();
		getXStream().toXML(obj, stringWriter);
		return stringWriter.toString();
	}
	
	private static XStream getXStream() {
		XStream xStream = new XStream(new StaxDriver());
		xStream.registerConverter(new ReflectionConverter(xStream.getMapper(), xStream.getReflectionProvider()), XStream.PRIORITY_LOW); 
		xStream.autodetectAnnotations(Boolean.TRUE);
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.NO_REFERENCES);
		
		return xStream;
	}
	
	public static Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
				.create();
		}
		return gson;
	}
}
