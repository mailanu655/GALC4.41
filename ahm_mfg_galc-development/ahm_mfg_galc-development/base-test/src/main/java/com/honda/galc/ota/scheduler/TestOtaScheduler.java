package com.honda.galc.ota.scheduler;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.task.GenerateXmlForOtaProvisioning;
import com.honda.galc.service.ServiceFactory;

public class TestOtaScheduler {

	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9080/BaseWeb/HttpServiceHandler");
	}
	
	@Test
	public void testOtaProvisioningService(){
		
		GenerateXmlForOtaProvisioning scheduler = new GenerateXmlForOtaProvisioning("OIF_VEHICLE_PROVISIONING");
		Object[] args = null;
		scheduler.execute(args);
		
	}
	
	//@Test
	public void testGetVinsMethod(){
		List<Object[]> vinLst = ServiceFactory.getDao(ShippingStatusDao.class).getInvoicedVindDetails("2016/09/01 02:25:05", "2016/09/01 14:41:37","INFOTAINMENT UNIT");
		
		for (Object[] string : vinLst) {
			System.out.println("Vins : " + string[0].toString() + " , " + string[7].toString());
		}
	}

}
