package com.honda.galc.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
//import org.junit.runners.MethodSorters;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
//import com.honda.test.util.DBUtils;
//import com.honda.test.util.TestUtils;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class BuildAttributeCacheTest {
	
	
	
	
	
	@Test
	public void test03MatchModelCodeAndTypeCodeIncorrect() {
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		buildAttributes.add(new BuildAttribute("GT6N","ATTR","BLACK"));
		buildAttributes.add(new BuildAttribute("GT6NAB6","ATTR","WHITE"));
		
		PowerMockito.mockStatic(ServiceFactory.class);
		BuildAttributeDao buildAttributeDaoMock =  PowerMockito.mock(BuildAttributeDao.class);
		when(ServiceFactory.getDao(BuildAttributeDao.class)).thenReturn(buildAttributeDaoMock);	
		when(buildAttributeDaoMock.findAllByAttribute(Matchers.anyString())).thenReturn( buildAttributes);
		
		BuildAttributeCache cache = new  BuildAttributeCache();
		
		BuildAttribute attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAC6   N478M     IN"));
		assertEquals("BLACK", attribute.getAttributeValue());
		
		attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAB6   N478M     IN"));
		assertNotNull(attribute);
		assertEquals("WHITE", attribute.getAttributeValue());
			
	}
}
