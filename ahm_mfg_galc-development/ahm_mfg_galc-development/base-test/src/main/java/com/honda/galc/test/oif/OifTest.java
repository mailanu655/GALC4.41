package com.honda.galc.test.oif;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.task.AsyncTaskExecutorService;

public class OifTest {
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9084/BaseWeb/HttpServiceHandler");
		
	}
	
	@Test
	public void test(){
		AsyncTaskExecutorService oifService = ServiceFactory.getService(AsyncTaskExecutorService.class);
		
		oifService.execute("OIF_FRAME_PRIORITY_PLAN", "", "");
		
	}
	
	
		
}
