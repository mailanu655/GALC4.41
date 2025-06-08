package com.honda.test.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.honda.galc.data.ApplicationContextProvider;

public class GALCServer {

	public static void main(String[] args) {
		int port = 9084;
		ApplicationContext context = ApplicationContextProvider.loadFromClassPathXml("applicationContext_db2.xml");
		
		if(args.length > 0) {
			String portNumber = args[0];
			if(StringUtils.isNumeric(portNumber)) {
				port = Integer.parseInt(portNumber);
			}
		}
		
		
		ServletContainer.start(port);
		
	}
	
	

}
