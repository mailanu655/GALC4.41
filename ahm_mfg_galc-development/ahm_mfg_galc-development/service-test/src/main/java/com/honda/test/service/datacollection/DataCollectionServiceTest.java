package com.honda.test.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.DataCollectionService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

public class DataCollectionServiceTest {
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/datacollection/engine_data_collection.sql");

	}
	
	
	@Test
	public void testMapByRule(){
		
		DataCollectionService service = getService(DataCollectionService.class);
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("PRODUCT_ID", "K20C24205985");
		dc.put("VALUE1", 5);
		dc.put("VALUE2", 5);
		dc.put("VALUE3", 5);
		dc.put("VALUE4", 5);
		dc.put("VALUE5", 5);
		dc.put("VALUE6", 5);
		dc.put("VALUE7", 5);
		dc.put("VALUE8", 5);
		dc.put("VALUE9", 5);
		
		dc.put("RESULT1", "1");
		dc.put("RESULT2", "1");
		dc.put("RESULT3", "1");
		dc.put("RESULT4", "1");
		dc.put("RESULT5", "1");
		dc.put("RESULT6", "1");
		dc.put("RESULT7", "1");
		dc.put("RESULT8", "1");
		dc.put("RESULT9", "1");
		
		
		Device device = TestUtils.prepareDevice("AE0EN14101", dc);
		service.execute(device);
		
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByProductIdAndProcessPoint("K20C24205985", "AE0EN14101");
		assertEquals(3,installedParts.size());
		
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("K20C24205985", "AE0EN14101");
		assertEquals(1,productResults.size());
		
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
}
