package com.honda.galc.rest.xml;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.honda.galc.rest.common.AbstractContentHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author Subu Kathiresan
 * @date Feb 23, 2015
 */
public class XmlContentHandler extends AbstractContentHandler {
	
	public XmlContentHandler() {}

	@SuppressWarnings("unchecked")
	public ArrayList<Object> getParametersFromRequestBody(HttpServletRequest httpRequest, String reqSignature) {
		ArrayList<Object> parameters = new ArrayList<Object>();
		try {
			XmlPayload xmlPayload = (XmlPayload) getXStream().fromXML(httpRequest.getReader());
			if (xmlPayload != null) {
				getLogger().info("REST parameters received in XML payload for "
						+ reqSignature
						+ " from " + getClientAddress() 
						+ ": " + xmlPayload.getRestParams());
				parameters = (ArrayList<Object>) xmlPayload.getRestParams();
			} else {
				getLogger().info("No Parameters in XML payload for "
						+ reqSignature
						+ " from " + getClientAddress());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Error retrieving parameters from Request body");
		}
		return parameters;
	}
	
	public String getResponse(String reqSignature, Object obj) {
		StringWriter stringWriter = new StringWriter();
		getXStream().toXML(obj, stringWriter);
		String responseString = stringWriter.toString();
		getLogger().info("REST response for " 
				+ reqSignature
				+ " sent to " + getClientAddress() 
				+ ": " + responseString);
		return responseString;
	}
	
	public XStream getXStream() {
		XStream xStream = new XStream(new StaxDriver());
		xStream.registerConverter(new ReflectionConverter(xStream.getMapper(), xStream.getReflectionProvider()), XStream.PRIORITY_LOW); 
		xStream.autodetectAnnotations(Boolean.TRUE);
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.processAnnotations(XmlPayload.class);
		
		return xStream;
	}
}
