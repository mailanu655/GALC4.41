package com.honda.galc.data;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import com.honda.galc.common.logging.Logger;

public class XmlStringApplicationContext {
	
	public static GenericApplicationContext getApplicationContext(String xmlString) {
		GenericApplicationContext ctx = null;
		try{
			ctx = new GenericApplicationContext();
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ctx);
			reader.loadBeanDefinitions(new ByteArrayResource(xmlString.getBytes()));
			ctx.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "Unable to load Application context");
		}
		return ctx;
	}
}

