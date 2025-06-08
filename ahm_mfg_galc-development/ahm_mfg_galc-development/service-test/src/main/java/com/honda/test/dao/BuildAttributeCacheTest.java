package com.honda.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildAttributeCacheTest {
	
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		TestUtils.startWebServer();
	}
	
	@Test
	public void test01MatchModelCode() {
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		buildAttributes.add(new BuildAttribute("GT6N","ATTR","BLACK"));
		buildAttributes.add(new BuildAttribute("GT6NAB6","ATTR","WHITE"));
		
		getDao(BuildAttributeDao.class).saveAll(buildAttributes);
		
		BuildAttributeCache cache = new  BuildAttributeCache();
		
		BuildAttribute attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAC6   N478M     IN"));
		assertNotNull(attribute);
		assertEquals("BLACK", attribute.getAttributeValue());
		
		getDao(BuildAttributeDao.class).removeAll();
		
	}
	
	@Test
	public void test02MatchModelCodeAndTypeCode() {
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		buildAttributes.add(new BuildAttribute("GT6N","ATTR","BLACK"));
		buildAttributes.add(new BuildAttribute("GT6NAB6","ATTR","WHITE"));
		
		getDao(BuildAttributeDao.class).saveAll(buildAttributes);
		
		BuildAttributeCache cache = new  BuildAttributeCache();
		
		BuildAttribute attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAB6   N478M     IN"));
		assertNotNull(attribute);
		assertEquals("WHITE", attribute.getAttributeValue());
		
		
		getDao(BuildAttributeDao.class).removeAll();
		
	}
	
	@Test
	public void test03MatchModelCodeAndTypeCodeIncorrect() {
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		buildAttributes.add(new BuildAttribute("GT6N","ATTR","BLACK"));
		buildAttributes.add(new BuildAttribute("GT6NAB6","ATTR","WHITE"));
		
		getDao(BuildAttributeDao.class).saveAll(buildAttributes);
		
		BuildAttributeCache cache = new  BuildAttributeCache();
		
		BuildAttribute attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAC6   N478M     IN"));
		
		attribute = cache.findByKey(new BuildAttributeId("ATTR","GT6NAB6   N478M     IN"));
		assertNotNull(attribute);
		assertEquals("WHITE", attribute.getAttributeValue());
		
		
		getDao(BuildAttributeDao.class).removeAll();
		
	}
}
