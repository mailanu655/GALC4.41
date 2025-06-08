package com.honda.galc.client;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineManifestDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.EngineShippingHelper;

public class EngineManifestTest {
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9083/BaseWeb/HttpServiceHandler");
		
	}
	
	@Test
	public void test(){
		String ppId = "AE0EN16601";
		EngineShippingHelper helper= createHelper(ppId);
		String plant ="HCM 01";
		List<EngineManifest> items = ServiceFactory.getDao(EngineManifestDao.class).findAll();
		int count = 5000;
		int i =0;
		for(EngineManifest emf : items) {
			Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(emf.getId().getEngineNo());
			if(engine==null) continue;
			helper.invokeBroadcast(emf.getId().getEngineNo(), plant, ppId);
			i++;
			if(i >=count) break;
		}
	}
	
	EngineShippingHelper createHelper(String ppId) {
		EngineShippingPropertyBean propertyBean = PropertyService.getPropertyBean(EngineShippingPropertyBean.class, ppId);
		return new EngineShippingHelper(propertyBean);
	}
	
	
		
}
