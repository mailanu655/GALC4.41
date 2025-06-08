package com.honda.galc.client;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;

public class BroadcastServiceTest {
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost/BaseWeb/HttpServiceHandler");
	}
	
	@Test
	public void test(){
		BroadcastService service = ServiceFactory.getService(BroadcastService.class);
	//	service.broadcast("PP10722", "19XFBDSM0CE000001");
		
		service.broadcast("AE0EN19101", "R18Z12162411");
		
	}
}
